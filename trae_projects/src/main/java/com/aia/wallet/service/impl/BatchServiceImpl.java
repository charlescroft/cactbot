package com.aia.wallet.service.impl;

import com.aia.wallet.common.AppConstants;
import com.aia.wallet.common.SftpUtils;
import com.aia.wallet.entity.OperatorLog;
import com.aia.wallet.entity.RewardTransaction;
import com.aia.wallet.enums.TransactionStatus;
import com.aia.wallet.enums.TransactionType;
import com.aia.wallet.repository.RewardTransactionRepository;
import com.aia.wallet.service.BatchService;
import com.aia.wallet.service.OperatorLogService;
import com.jcraft.jsch.ChannelSftp;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

@Service
public class BatchServiceImpl implements BatchService {

    private static final Logger log = LoggerFactory.getLogger(BatchServiceImpl.class);

    private final SftpUtils sftpUtils;
    private final RewardTransactionRepository rewardTransactionRepository;
    private final OperatorLogService operatorLogService;

    @Value("${app.sftp.remote-dir}")
    private String remoteDir;

    @Value("${app.sftp.download-dir}")
    private String downloadDir;

    @Value("${app.sftp.backup-dir}")
    private String backupDir;

    @Value("${app.sftp.username}")
    private String sftpUser;

    public BatchServiceImpl(SftpUtils sftpUtils, RewardTransactionRepository rewardTransactionRepository, OperatorLogService operatorLogService) {
        this.sftpUtils = sftpUtils;
        this.rewardTransactionRepository = rewardTransactionRepository;
        this.operatorLogService = operatorLogService;
    }

    @Override
    public void processDailyRewards() {
        String batchRunId = UUID.randomUUID().toString();
        log.info("Starting batch processing. Batch Run ID: {}", batchRunId);

        try {
            // 1. List files
            Vector<ChannelSftp.LsEntry> files = sftpUtils.listFiles(remoteDir);
            if (files == null || files.isEmpty()) {
                log.info("No CSV files found in {}", remoteDir);
                return;
            }

            for (ChannelSftp.LsEntry fileEntry : files) {
                String filename = fileEntry.getFilename();
                if (!filename.endsWith(AppConstants.Csv.EXTENSION)) continue;

                log.info("Processing file: {}", filename);
                processFile(filename, batchRunId);
            }

        } catch (Exception e) {
            log.error("Error during batch processing", e);
        }
    }

    private void processFile(String filename, String batchRunId) {
        List<OperatorLog> logs = new ArrayList<>();

        try {
            // 2. Download file
            sftpUtils.downloadFile(remoteDir, downloadDir, filename);
            File localFile = new File(downloadDir + File.separator + filename);

            // 3. Parse and Validate
            List<RewardTransaction> transactions = parseCsv(localFile, batchRunId, logs);

            // 4. Save to DB
            saveTransactions(transactions);

            // 5. Save Logs
            operatorLogService.saveBatch(logs);

            // 6. Move to backup (Remote)
            // Ideally we move the remote file to a backup folder
            sftpUtils.moveFile(remoteDir + "/" + filename, backupDir + "/" + filename);
            
            // Generate Report (Skipped for brevity, would be similar to CSV writing)

        } catch (Exception e) {
            log.error("Failed to process file: " + filename, e);
        }
    }

    @Transactional
    public void saveTransactions(List<RewardTransaction> transactions) {
        rewardTransactionRepository.saveAll(transactions);
    }

    private List<RewardTransaction> parseCsv(File file, String batchRunId, List<OperatorLog> logs) throws Exception {
        List<RewardTransaction> list = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        try (Reader reader = new FileReader(file);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord record : csvParser) {
                OperatorLog opLog = new OperatorLog();
                opLog.setFilename(file.getName());
                opLog.setBatchRunId(batchRunId);
                opLog.setCreatedAt(LocalDateTime.now());
                opLog.setTransactionDate(LocalDate.now()); // Default

                try {
                    RewardTransaction tx = new RewardTransaction();
                    
                    // 1. Client ID
                    String clientId = null;
                    if (record.isMapped(AppConstants.Csv.HEADER_CLIENT_ID)) {
                        clientId = record.get(AppConstants.Csv.HEADER_CLIENT_ID);
                    } else if (record.isMapped(AppConstants.Csv.HEADER_USER_ID)) {
                        clientId = record.get(AppConstants.Csv.HEADER_USER_ID);
                    }
                    if (clientId == null || clientId.trim().isEmpty()) {
                        throw new IllegalArgumentException(AppConstants.Messages.ERROR_MISSING_CLIENT_ID);
                    }
                    tx.setClientId(clientId);
                    opLog.setClientId(clientId); // Set early for logging

                    // 2. User Name (Optional in logic but mapped)
                    if (record.isMapped(AppConstants.Csv.HEADER_USER_NAME)) {
                        tx.setUserName(record.get(AppConstants.Csv.HEADER_USER_NAME));
                    }

                    // 3. Transaction Type
                    String typeStr = record.get(AppConstants.Csv.HEADER_TRANSACTION_TYPE);
                    if (typeStr == null || typeStr.trim().isEmpty()) {
                        throw new IllegalArgumentException(AppConstants.Messages.ERROR_MISSING_TRANSACTION_TYPE);
                    }
                    boolean typeFound = false;
                    for (TransactionType t : TransactionType.values()) {
                        if (t.name().equalsIgnoreCase(typeStr)) {
                            tx.setTransactionType(t);
                            typeFound = true;
                            break;
                        }
                    }
                    if (!typeFound) {
                         throw new IllegalArgumentException(AppConstants.Messages.ERROR_INVALID_TRANSACTION_TYPE + typeStr);
                    }
                    opLog.setTransactionType(tx.getTransactionType());

                    // 4. Amount
                    if (!record.isMapped(AppConstants.Csv.HEADER_AMOUNT) || record.get(AppConstants.Csv.HEADER_AMOUNT).trim().isEmpty()) {
                         throw new IllegalArgumentException(AppConstants.Messages.ERROR_MISSING_AMOUNT);
                    }
                    tx.setAmount(new BigDecimal(record.get(AppConstants.Csv.HEADER_AMOUNT)));
                    opLog.setAmount(tx.getAmount());

                    // 5. Currency
                    if (!record.isMapped(AppConstants.Csv.HEADER_CURRENCY) || record.get(AppConstants.Csv.HEADER_CURRENCY).trim().isEmpty()) {
                         throw new IllegalArgumentException(AppConstants.Messages.ERROR_MISSING_CURRENCY);
                    }
                    tx.setCurrency(record.get(AppConstants.Csv.HEADER_CURRENCY));
                    opLog.setCurrency(tx.getCurrency());
                    
                    // 6. Transaction Date
                    String dateStr = record.get(AppConstants.Csv.HEADER_TRANSACTION_DATE);
                    if (dateStr == null || dateStr.trim().isEmpty()) {
                        throw new IllegalArgumentException(AppConstants.Messages.ERROR_MISSING_DATE);
                    }
                    tx.setTransactionDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT_ISO)));
                    opLog.setTransactionDate(tx.getTransactionDate());

                    // 7. Requested By
                    String requestedBy = record.get(AppConstants.Csv.HEADER_REQUESTED_BY);
                    if (requestedBy == null || requestedBy.trim().isEmpty()) {
                        throw new IllegalArgumentException(AppConstants.Messages.ERROR_MISSING_REQUESTED_BY);
                    }
                    tx.setRequestedBy(requestedBy);
                    opLog.setOperatorName(requestedBy);
                    
                    // 8. Effective Date
                    if (record.isMapped(AppConstants.Csv.HEADER_EFFECTIVE_DATE)) {
                        String effDate = record.get(AppConstants.Csv.HEADER_EFFECTIVE_DATE);
                        if (effDate != null && !effDate.trim().isEmpty()) {
                            tx.setEffectiveDate(LocalDate.parse(effDate, DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT_ISO)));
                        } else {
                            // Spec implies required? "Required CSV Fields: ... Effective Date"
                            throw new IllegalArgumentException("Missing Effective Date");
                        }
                    } else {
                        throw new IllegalArgumentException("Missing Effective Date");
                    }

                    // 9. Expiration Date
                    if (record.isMapped(AppConstants.Csv.HEADER_EXPIRATION_DATE)) {
                        String expDate = record.get(AppConstants.Csv.HEADER_EXPIRATION_DATE);
                        if (expDate != null && !expDate.trim().isEmpty()) {
                            tx.setExpirationDate(LocalDate.parse(expDate, DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT_ISO)));
                        } else {
                            throw new IllegalArgumentException("Missing Expiration Date");
                        }
                    } else {
                        throw new IllegalArgumentException("Missing Expiration Date");
                    }
                    
                    // Optional fields
                    if (record.isMapped(AppConstants.Csv.HEADER_DESCRIPTION)) {
                        tx.setCampaignDescription(record.get(AppConstants.Csv.HEADER_DESCRIPTION));
                        opLog.setCampaignDescription(tx.getCampaignDescription());
                    }
                    if (record.isMapped(AppConstants.Csv.HEADER_CAMPAIGN_CODE)) {
                        tx.setCampaignCode(record.get(AppConstants.Csv.HEADER_CAMPAIGN_CODE));
                        opLog.setCampaignCode(tx.getCampaignCode());
                    }
                    
                    tx.setBatchRunId(batchRunId);
                    tx.setStatus(TransactionStatus.SUCCESS);
                    tx.setOperatorId(sftpUser); // Use SFTP user as Operator ID

                    list.add(tx);
                    successCount++;

                    // Success Log
                    opLog.setStatus(TransactionStatus.SUCCESS);
                    opLog.setProcessedAt(LocalDateTime.now());
                    
                    logs.add(opLog);

                } catch (Exception e) {
                    failureCount++;
                    log.error("Failed to process record number {}: {}", record.getRecordNumber(), e.getMessage());
                    
                    // Failure Log
                    opLog.setStatus(TransactionStatus.FAILED);
                    opLog.setErrorMessage(e.getMessage());
                    // Try to populate what we can for the log if it wasn't set
                    if (opLog.getClientId() == null && record.isMapped(AppConstants.Csv.HEADER_CLIENT_ID)) {
                         opLog.setClientId(record.get(AppConstants.Csv.HEADER_CLIENT_ID));
                    }
                    logs.add(opLog);
                }
            }
        }
        log.info("Batch processing completed. Success: {}, Failure: {}", successCount, failureCount);
        return list;
    }
}
