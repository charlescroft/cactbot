package com.aia.wallet.service;

import com.aia.wallet.common.AppConstants;
import com.aia.wallet.common.SftpUtils;
import com.aia.wallet.entity.OperatorLog;
import com.aia.wallet.entity.RewardTransaction;
import com.aia.wallet.enums.TransactionStatus;
import com.aia.wallet.repository.OperatorLogRepository;
import com.aia.wallet.repository.RewardTransactionRepository;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class BatchReportIntegrationTest {

    // Manual Mock Implementation to avoid Mockito/Spring conflicts
    static class FakeSftpUtils extends SftpUtils {
        private final Map<String, String> fileContents = new HashMap<>();
        private final Map<String, String> uploadedFiles = new HashMap<>();
        private final List<String> remoteFiles = new ArrayList<>();
        
        // Configuration methods for tests
        public void addRemoteFile(String filename, String content) {
            remoteFiles.add(filename);
            fileContents.put(filename, content);
        }
        
        public Map<String, String> getUploadedFiles() {
            return uploadedFiles;
        }

        @Override
        public void downloadFile(String remoteDir, String localDir, String fileName) throws Exception {
            if (fileContents.containsKey(fileName)) {
                File file = new File(localDir, fileName);
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(fileContents.get(fileName));
                }
            } else {
                throw new FileNotFoundException("Remote file not found: " + fileName);
            }
        }

        @Override
        public void uploadFile(String remoteDir, String localFilePath, String remoteFileName) throws Exception {
            File localFile = new File(localFilePath);
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(localFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            uploadedFiles.put(remoteFileName, content.toString());
            System.out.println("Mock Upload: " + localFilePath + " to " + remoteDir + "/" + remoteFileName);
        }

        @Override
        public Vector<ChannelSftp.LsEntry> listFiles(String remoteDir) throws Exception {
            Vector<ChannelSftp.LsEntry> entries = new Vector<>();
            for (String filename : remoteFiles) {
                entries.add(createLsEntry(filename));
            }
            return entries;
        }

        @Override
        public void moveFile(String srcPath, String destPath) throws Exception {
            // Do nothing
        }

        private ChannelSftp.LsEntry createLsEntry(String filename) {
            try {
                ChannelSftp channel = new ChannelSftp();
                Class<?> c = ChannelSftp.LsEntry.class;
                java.lang.reflect.Constructor<?>[] ctors = c.getDeclaredConstructors();
                for (java.lang.reflect.Constructor<?> ctor : ctors) {
                    ctor.setAccessible(true);
                    // LsEntry is non-static inner class, so first arg is ChannelSftp instance
                    // Constructor signature usually: (ChannelSftp, String, String, SftpATTRS)
                    if (ctor.getParameterCount() == 4) {
                        return (ChannelSftp.LsEntry) ctor.newInstance(channel, filename, filename, null);
                    }
                }
                throw new RuntimeException("Could not find suitable LsEntry constructor");
            } catch (Exception e) {
                throw new RuntimeException("Failed to create LsEntry", e);
            }
        }
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public SftpUtils sftpUtils() {
            return new FakeSftpUtils();
        }
    }

    @Autowired
    private BatchService batchService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private RewardTransactionRepository rewardTransactionRepository;

    @Autowired
    private OperatorLogRepository operatorLogRepository;

    @Autowired
    private SftpUtils sftpUtils; 

    @Value("${app.sftp.download-dir}")
    private String downloadDir;


    @BeforeEach
    void setUp() {
        rewardTransactionRepository.deleteAll();
        operatorLogRepository.deleteAll();
        
        // Reset fake sftp state
        if (sftpUtils instanceof FakeSftpUtils) {
            FakeSftpUtils fake = (FakeSftpUtils) sftpUtils;
            fake.remoteFiles.clear();
            fake.fileContents.clear();
            fake.uploadedFiles.clear();
        }
    }

    @Test
    void testDailyBatchAndMonthlyReport() throws Exception {
        // ==========================================
        // 1. Prepare Mock Data (Daily CSV File)
        // ==========================================
        String filename = "Reward_2025-11-24.csv";
        String csvContent = "Client ID,User Name,Transaction Type,Amount,Currency,Transaction Date,Requested By,Campaign Description,Campaign Code,Effective Date,Expiration Date\n" +
                "20097740,Hsu Myat,Credit,500,MMK,2025-11-24,Pyae Thuta,Cashback,Q3 promotional campaign reward,2025-11-24,2025-12-24\n" +
                "20097740,Hsu Myat,Debit,200,MMK,2025-11-24,Pyae Thuta,Cashback,Q3 promotional campaign reward,2025-11-24,2025-12-24\n" +
                "INVALID_USER,Bad User,UnknownType,100,MMK,2025-11-24,Admin,Error,Test,2025-11-24,2025-12-24\n"; // Failed Record

        FakeSftpUtils fakeSftp = (FakeSftpUtils) sftpUtils;
        fakeSftp.addRemoteFile(filename, csvContent);

        // ==========================================
        // 2. Run Batch Processing
        // ==========================================
        batchService.processDailyRewards();

        // ==========================================
        // 3. Verify Data Storage (DB)
        // ==========================================
        
        // Verify RewardTransactions (Only Success records)
        List<RewardTransaction> transactions = rewardTransactionRepository.findAll();
        assertEquals(2, transactions.size(), "Should have 2 successful transactions");
        
        RewardTransaction creditTx = transactions.stream().filter(t -> t.getTransactionType().name().equals("Credit")).findFirst().orElseThrow();
        assertEquals(new BigDecimal("500.00"), creditTx.getAmount());
        
        RewardTransaction debitTx = transactions.stream().filter(t -> t.getTransactionType().name().equals("Debit")).findFirst().orElseThrow();
        assertEquals(new BigDecimal("200.00"), debitTx.getAmount());

        // Verify OperatorLogs (Success + Failed)
        List<OperatorLog> logs = operatorLogRepository.findAll();
        assertEquals(3, logs.size(), "Should have 3 logs (2 success + 1 failed)");
        
        long successCount = logs.stream().filter(l -> l.getStatus() == TransactionStatus.SUCCESS).count();
        assertEquals(2, successCount);
        
        long failedCount = logs.stream().filter(l -> l.getStatus() == TransactionStatus.FAILED).count();
        assertEquals(1, failedCount);
        
        OperatorLog failedLog = logs.stream().filter(l -> l.getStatus() == TransactionStatus.FAILED).findFirst().orElseThrow();
        assertNotNull(failedLog.getErrorMessage());
        assertTrue(failedLog.getErrorMessage().contains("Invalid Transaction Type"), "Error message should indicate invalid type");

        // ==========================================
        // 4. Run Monthly Report Generation
        // ==========================================
        LocalDate reportMonth = LocalDate.of(2025, 11, 1);
        reportService.generateAndUploadMonthlyReport(reportMonth);

        // ==========================================
        // 5. Verify Report Content
        // ==========================================
        Map<String, String> uploads = fakeSftp.getUploadedFiles();
        assertFalse(uploads.isEmpty(), "Should have uploaded a file");
        
        // Find the uploaded file (name might vary with timestamp)
        String uploadedContent = uploads.values().iterator().next();
        
        assertTrue(uploadedContent.contains("AIA MYANMAR,AIA Monthly Wallet Listing"));
        assertTrue(uploadedContent.contains("No,Client ID,User Name,Transaction Type,Amount,Currency,Current Balance"));
        // We expect the successful transactions to be in the report
        assertTrue(uploadedContent.contains("20097740"));
        assertTrue(uploadedContent.contains("500.00"));
    }
    
    @Test
    void testReportContentVerification() throws Exception {
        // Setup Data directly in DB
        OperatorLog log1 = new OperatorLog();
        log1.setClientId("20097740");
        log1.setTransactionType(com.aia.wallet.enums.TransactionType.Credit);
        log1.setAmount(new BigDecimal("500.00"));
        log1.setCurrency("MMK");
        log1.setTransactionDate(LocalDate.of(2025, 11, 24));
        log1.setStatus(TransactionStatus.SUCCESS);
        log1.setOperatorName("Pyae Thuta");
        log1.setCampaignDescription("Cashback");
        log1.setCampaignCode("Q3");
        operatorLogRepository.save(log1);

        // Also save RewardTransaction to populate view so balance is correct
        RewardTransaction tx1 = new RewardTransaction();
        tx1.setClientId("20097740");
        tx1.setTransactionType(com.aia.wallet.enums.TransactionType.Credit);
        tx1.setAmount(new BigDecimal("500.00"));
        tx1.setCurrency("MMK");
        tx1.setStatus(TransactionStatus.SUCCESS);
        tx1.setRequestedBy("Pyae Thuta");
        tx1.setUpdatedAt(java.time.LocalDateTime.now());
        rewardTransactionRepository.saveAndFlush(tx1);

        OperatorLog log2 = new OperatorLog();
        log2.setClientId("20097740");
        log2.setTransactionType(com.aia.wallet.enums.TransactionType.Debit);
        log2.setAmount(new BigDecimal("500.00")); // Note: Log stores raw amount
        log2.setCurrency("MMK");
        log2.setTransactionDate(LocalDate.of(2025, 11, 24));
        log2.setStatus(TransactionStatus.FAILED); // Failed record
        log2.setOperatorName("Pyae Thuta");
        log2.setErrorMessage("Some Error");
        operatorLogRepository.save(log2);

        reportService.generateAndUploadMonthlyReport(LocalDate.of(2025, 11, 1));

        FakeSftpUtils fakeSftp = (FakeSftpUtils) sftpUtils;
        String csv = fakeSftp.getUploadedFiles().values().iterator().next();
        System.out.println("Generated CSV:\n" + csv);

        assertTrue(csv.contains("AIA MYANMAR,AIA Monthly Wallet Listing"));
        assertTrue(csv.contains("No,Client ID,User Name,Transaction Type,Amount,Currency,Current Balance"));
        assertTrue(csv.contains("Passed"));
        assertTrue(csv.contains("Failed"));
        assertTrue(csv.contains("Some Error"));
        
        // Verify balance is present (500.00)
        // Passed record: ...,Credit,500.00,MMK,500.00,...
        assertTrue(csv.contains(",Credit,500.00,MMK,500.00,"), "CSV should contain correct balance");
    }

    @Test
    void testProcessFromResourceFile() throws Exception {
        // ==========================================
        // 1. Read CSV Content from Resources
        // ==========================================
        String resourceFilename = "mock_daily_report.csv";
        StringBuilder csvContentBuilder = new StringBuilder();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourceFilename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                csvContentBuilder.append(line).append("\n");
            }
        }
        String csvContent = csvContentBuilder.toString();
        assertFalse(csvContent.isEmpty(), "Resource file should not be empty");

        String filename = "Reward_Resource_Test.csv";

        FakeSftpUtils fakeSftp = (FakeSftpUtils) sftpUtils;
        fakeSftp.addRemoteFile(filename, csvContent);

        // ==========================================
        // 2. Run Batch Processing
        // ==========================================
        batchService.processDailyRewards();

        // ==========================================
        // 3. Verify Data Storage
        // ==========================================
        
        // Check Transactions (Success records from mock_daily_report.csv)
        // The file has 2 success records: Credit 500, Debit 200
        List<RewardTransaction> transactions = rewardTransactionRepository.findAll();
        assertEquals(2, transactions.size());
        
        RewardTransaction creditTx = transactions.stream()
                .filter(t -> t.getTransactionType().name().equals("Credit"))
                .findFirst().orElseThrow();
        assertEquals(new BigDecimal("500.00"), creditTx.getAmount());
        
        // Check Logs (Success + Failed)
        // The file has 1 failed record
        List<OperatorLog> logs = operatorLogRepository.findAll();
        assertEquals(3, logs.size());
        
        long failedCount = logs.stream().filter(l -> l.getStatus() == TransactionStatus.FAILED).count();
        assertEquals(1, failedCount);
    }
}
