package com.aia.wallet.service;

import com.aia.wallet.dto.BalanceResponse;
import com.aia.wallet.dto.TransactionResponse;
import com.aia.wallet.entity.RewardTransaction;
import com.aia.wallet.enums.TransactionStatus;
import com.aia.wallet.enums.TransactionType;
import com.aia.wallet.repository.RewardTransactionRepository;
import com.aia.wallet.repository.UserBalanceViewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDateTime;

import com.aia.wallet.entity.UserBalanceView;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class RewardServiceTest {

    @Autowired
    private RewardService rewardService;

    @Autowired
    private RewardTransactionRepository rewardTransactionRepository;

    @Autowired
    private UserBalanceViewRepository userBalanceViewRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        rewardTransactionRepository.deleteAll();

        // 1. Initial Credit: +1000
        RewardTransaction tx1 = new RewardTransaction();
        tx1.setClientId("user1");
        tx1.setTransactionType(TransactionType.Credit);
        tx1.setAmount(new BigDecimal("1000.00"));
        tx1.setCurrency("MMK");
        tx1.setTransactionDate(LocalDate.now().minusDays(2));
        tx1.setStatus(TransactionStatus.SUCCESS);
        tx1.setRequestedBy("Admin");
        rewardTransactionRepository.saveAndFlush(tx1);

        // 2. Debit: 200 (Logic will treat it as negative)
        RewardTransaction tx2 = new RewardTransaction();
        tx2.setClientId("user1");
        tx2.setTransactionType(TransactionType.Debit);
        tx2.setAmount(new BigDecimal("200.00"));
        tx2.setCurrency("MMK");
        tx2.setTransactionDate(LocalDate.now().minusDays(1));
        tx2.setStatus(TransactionStatus.SUCCESS);
        tx2.setRequestedBy("User");
        rewardTransactionRepository.saveAndFlush(tx2);

        // 3. Failed Transaction (Should be ignored)
        RewardTransaction tx3 = new RewardTransaction();
        tx3.setClientId("user1");
        tx3.setTransactionType(TransactionType.Credit);
        tx3.setAmount(new BigDecimal("500.00"));
        tx3.setCurrency("MMK");
        tx3.setTransactionDate(LocalDate.now());
        tx3.setStatus(TransactionStatus.FAILED);
        tx3.setRequestedBy("Admin");
        rewardTransactionRepository.saveAndFlush(tx3);
        
        // 4. Another user (Should not affect user1)
        RewardTransaction tx4 = new RewardTransaction();
        tx4.setClientId("user2");
        tx4.setTransactionType(TransactionType.Credit);
        tx4.setAmount(new BigDecimal("999.00"));
        tx4.setCurrency("MMK");
        tx4.setStatus(TransactionStatus.SUCCESS);
        tx4.setRequestedBy("Admin");
        rewardTransactionRepository.saveAndFlush(tx4);
        
        System.out.println("DEBUG: All Transactions: " + rewardTransactionRepository.findAll().size());
        rewardTransactionRepository.findAll().forEach(t -> System.out.println("Tx: " + t.getClientId() + " " + t.getTransactionType() + " " + t.getAmount() + " " + t.getStatus()));
        
        Object viewCount = entityManager.createNativeQuery("SELECT count(*) FROM user_balance_view").getSingleResult();
        System.out.println("DEBUG: Native View Count: " + viewCount);

        List<Object[]> nativeTx = entityManager.createNativeQuery("SELECT client_id, transaction_type, status, expiration_date FROM reward_transactions").getResultList();
        nativeTx.forEach(row -> System.out.println("Native Tx: " + row[0] + ", " + row[1] + ", " + row[2] + ", " + row[3]));

        System.out.println("DEBUG: All Views: " + userBalanceViewRepository.findAll().size());
        userBalanceViewRepository.findAll().forEach(v -> System.out.println("View: " + v.getClientId() + " " + v.getBalance() + " " + v.getCurrency() + " " + v.getLastUpdatedAt()));
        
        // Debug view definition
        try {
            Object viewDef = entityManager.createNativeQuery("SELECT VIEW_DEFINITION FROM INFORMATION_SCHEMA.VIEWS WHERE TABLE_NAME = 'USER_BALANCE_VIEW'").getSingleResult();
            System.out.println("DEBUG: View Definition: " + viewDef);
        } catch (Exception e) {
            System.out.println("DEBUG: Error getting view definition: " + e.getMessage());
        }
        
        // Direct view select simulation
        List<Object[]> directView = entityManager.createNativeQuery(
            "SELECT client_id, " +
            "SUM(CASE WHEN transaction_type = 'Debit' THEN -amount ELSE amount END) as balance, " +
            "MAX(currency) as currency, " +
            "MAX(updated_at) as last_updated_at " +
            "FROM reward_transactions " +
            "WHERE status = 'SUCCESS' AND (expiration_date IS NULL OR expiration_date >= CURRENT_DATE) " +
            "GROUP BY client_id"
        ).getResultList();
        System.out.println("DEBUG: Direct View Query Count: " + directView.size());
        directView.forEach(row -> System.out.println("Direct View Row: " + row[0] + ", " + row[1] + ", " + row[2] + ", " + row[3]));
        
        // List tables and views
        List<String> tables = (List<String>) entityManager.createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'").getResultList();
        System.out.println("DEBUG: Base Tables: " + tables);
        
        List<String> views = (List<String>) entityManager.createNativeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.VIEWS").getResultList();
        System.out.println("DEBUG: Views: " + views);
    }

    @Test
    void testGetBalance() {
        BalanceResponse balance = rewardService.getBalance("user1");
        
        assertNotNull(balance);
        assertEquals("user1", balance.getClientId());
        assertEquals(new BigDecimal("800.00"), balance.getBalance()); // 1000 - 200
        assertEquals("MMK", balance.getCurrency());
        assertNotNull(balance.getLastUpdatedAt());
    }
    
    @Test
    void testGetBalance_UserNotFound() {
        BalanceResponse balance = rewardService.getBalance("non_existent_user");
        
        assertNotNull(balance);
        assertEquals("non_existent_user", balance.getClientId());
        assertEquals(BigDecimal.ZERO, balance.getBalance());
        assertNull(balance.getLastUpdatedAt());
    }

    @Test
    void testGetBalance_WithExpiredTransactions() {
        // 1. Valid Credit: +1000
        RewardTransaction tx1 = new RewardTransaction();
        tx1.setClientId("user_expired");
        tx1.setTransactionType(TransactionType.Credit);
        tx1.setAmount(new BigDecimal("1000.00"));
        tx1.setCurrency("MMK");
        tx1.setStatus(TransactionStatus.SUCCESS);
        tx1.setRequestedBy("Admin");
        tx1.setUpdatedAt(LocalDateTime.now());
        rewardTransactionRepository.saveAndFlush(tx1);

        // 2. Expired Credit: +500 (Should be ignored)
        RewardTransaction tx2 = new RewardTransaction();
        tx2.setClientId("user_expired");
        tx2.setTransactionType(TransactionType.Credit);
        tx2.setAmount(new BigDecimal("500.00"));
        tx2.setCurrency("MMK");
        tx2.setStatus(TransactionStatus.SUCCESS);
        tx2.setExpirationDate(LocalDate.now().minusDays(1)); // Expired yesterday
        tx2.setRequestedBy("Admin");
        tx2.setUpdatedAt(LocalDateTime.now());
        rewardTransactionRepository.saveAndFlush(tx2);
        
        // 3. Future Expired Credit: +300 (Should be included)
        RewardTransaction tx3 = new RewardTransaction();
        tx3.setClientId("user_expired");
        tx3.setTransactionType(TransactionType.Credit);
        tx3.setAmount(new BigDecimal("300.00"));
        tx3.setCurrency("MMK");
        tx3.setStatus(TransactionStatus.SUCCESS);
        tx3.setExpirationDate(LocalDate.now().plusDays(1)); // Expires tomorrow
        tx3.setRequestedBy("Admin");
        tx3.setUpdatedAt(LocalDateTime.now());
        rewardTransactionRepository.saveAndFlush(tx3);
        
        // Additional debug for this test
        List<Object[]> testFilteredTx = entityManager.createNativeQuery(
            "SELECT client_id, transaction_type, amount, status, expiration_date, updated_at " +
            "FROM reward_transactions " +
            "WHERE client_id = 'user_expired' AND status = 'SUCCESS' AND (expiration_date IS NULL OR expiration_date >= CURRENT_DATE)"
        ).getResultList();
        System.out.println("DEBUG in Test: Filtered Transactions Count for user_expired: " + testFilteredTx.size());
        testFilteredTx.forEach(row -> System.out.println("Test Filtered Tx: " + row[0] + ", " + row[1] + ", " + row[2] + ", " + row[3] + ", " + row[4] + ", " + row[5]));
        
        List<UserBalanceView> userView = userBalanceViewRepository.findById("user_expired").stream().collect(Collectors.toList());
        if (!userView.isEmpty()) {
            System.out.println("DEBUG in Test: View for user_expired: " + userView.get(0).getBalance());
        } else {
            System.out.println("DEBUG in Test: No view data for user_expired");
        }

        BalanceResponse balance = rewardService.getBalance("user_expired");

        assertNotNull(balance);
        assertEquals("user_expired", balance.getClientId());
        // 1000 + 300 = 1300. The 500 expired one should be ignored.
        // Use compareTo for BigDecimal equality to ignore scale differences if any, 
        // though assertions usually handle it, safe side.
        assertEquals(0, new BigDecimal("1300.00").compareTo(balance.getBalance()));
    }

    @Test
    void testGetTransactionHistory() {
        List<TransactionResponse> history = rewardService.getTransactionHistory("user1");
        
        assertNotNull(history);
        assertEquals(2, history.size()); // Only 2 SUCCESS transactions
        
        // Verify order (DESC by date/update)
        // Note: Our service sorts by TransactionDate DESC.
        // tx2 is yesterday, tx1 is 2 days ago. So tx2 should be first.
        
        TransactionResponse first = history.get(0);
        assertEquals(new BigDecimal("200.00"), first.getAmount());
        assertEquals("Debit", first.getTransactionType());
        
        TransactionResponse second = history.get(1);
        assertEquals(new BigDecimal("1000.00"), second.getAmount());
        assertEquals("Credit", second.getTransactionType());
    }
}
