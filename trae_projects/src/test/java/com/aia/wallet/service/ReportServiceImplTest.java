package com.aia.wallet.service;

import com.aia.wallet.common.AppConstants;
import com.aia.wallet.common.SftpUtils;
import com.aia.wallet.entity.OperatorLog;
import com.aia.wallet.entity.UserBalanceView;
import com.aia.wallet.enums.TransactionStatus;
import com.aia.wallet.enums.TransactionType;
import com.aia.wallet.repository.OperatorLogRepository;
import com.aia.wallet.repository.UserBalanceViewRepository;
import com.aia.wallet.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReportServiceImplTest {

    @Test
    void testGenerateAndUploadMonthlyReport_WithContentCheck() throws Exception {
        // 1. Proxy for OperatorLogRepository
        OperatorLogRepository stubLogRepo = (OperatorLogRepository) Proxy.newProxyInstance(
            OperatorLogRepository.class.getClassLoader(),
            new Class[] { OperatorLogRepository.class },
            (proxy, method, args) -> {
                if (method.getName().equals("findByTransactionDateBetween")) {
                    OperatorLog log = new OperatorLog();
                    log.setClientId("user1");
                    log.setTransactionType(TransactionType.Credit);
                    log.setAmount(new BigDecimal("100"));
                    log.setCurrency("MMK");
                    log.setTransactionDate(LocalDate.of(2025, 11, 24));
                    log.setStatus(TransactionStatus.SUCCESS);
                    log.setOperatorName("Admin");
                    log.setCampaignDescription("Desc");
                    log.setCampaignCode("Code");
                    return Collections.singletonList(log);
                }
                return null;
            }
        );

        // 2. Proxy for UserBalanceViewRepository
        UserBalanceViewRepository stubViewRepo = (UserBalanceViewRepository) Proxy.newProxyInstance(
            UserBalanceViewRepository.class.getClassLoader(),
            new Class[] { UserBalanceViewRepository.class },
            (proxy, method, args) -> {
                if (method.getName().equals("findAllById")) {
                    UserBalanceView view = new UserBalanceView();
                    view.setClientId("user1");
                    view.setBalance(new BigDecimal("500.00"));
                    return Collections.singletonList(view);
                }
                return Collections.emptyList();
            }
        );

        // 3. Stub SftpUtils (Class, so cannot use Proxy unless interface, but SftpUtils is a class)
        // We can extend it since it's not final.
        final StringBuilder fileContent = new StringBuilder();
        SftpUtils stubSftp = new SftpUtils() {
            @Override
            public void uploadFile(String remoteDir, String localFilePath, String remoteFileName) throws Exception {
                try (BufferedReader reader = new BufferedReader(new FileReader(localFilePath))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.append(line).append("\n");
                    }
                }
            }
        };

        // 4. Execute
        ReportServiceImpl service = new ReportServiceImpl(stubLogRepo, stubSftp, stubViewRepo);
        service.generateAndUploadMonthlyReport(LocalDate.of(2025, 11, 1));

        // 5. Verify
        String csv = fileContent.toString();
        System.out.println(csv);
        
        assertTrue(csv.contains("Current Balance"), "Header should contain Current Balance");
        assertTrue(csv.contains("500.00"), "Content should contain balance 500.00");
    }
}
