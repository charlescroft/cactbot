package com.aia.wallet.service.impl;

import com.aia.wallet.common.AppConstants;
import com.aia.wallet.common.SftpUtils;
import com.aia.wallet.entity.OperatorLog;
import com.aia.wallet.entity.UserBalanceView;
import com.aia.wallet.enums.TransactionStatus;
import com.aia.wallet.repository.OperatorLogRepository;
import com.aia.wallet.repository.UserBalanceViewRepository;
import com.aia.wallet.service.ReportService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final OperatorLogRepository operatorLogRepository;
    private final SftpUtils sftpUtils;
    private final UserBalanceViewRepository userBalanceViewRepository;

    public ReportServiceImpl(OperatorLogRepository operatorLogRepository, SftpUtils sftpUtils, UserBalanceViewRepository userBalanceViewRepository) {
        this.operatorLogRepository = operatorLogRepository;
        this.sftpUtils = sftpUtils;
        this.userBalanceViewRepository = userBalanceViewRepository;
    }

    @Override
    public void generateAndUploadDailyReport(LocalDate date) {
        try {
            List<OperatorLog> logs = operatorLogRepository.findAll().stream()
                    .filter(l -> l.getTransactionDate() != null && l.getTransactionDate().equals(date))
                    .collect(Collectors.toList());
            
            String fileName = AppConstants.Report.DAILY_PREFIX + date.toString() + AppConstants.Csv.EXTENSION;
            File file = generateCsv(logs, fileName, AppConstants.Report.REPORT_TITLE_DAILY);
            
            sftpUtils.uploadFile(AppConstants.Report.DAILY_PATH, file.getAbsolutePath(), fileName);
            Files.deleteIfExists(file.toPath()); // Cleanup
            
        } catch (Exception e) {
            log.error("Failed to generate daily report", e);
        }
    }

    @Override
    public void generateAndUploadMonthlyReport(LocalDate month) {
        try {
            LocalDate start = month.withDayOfMonth(1);
            LocalDate end = month.withDayOfMonth(month.lengthOfMonth());
            
            List<OperatorLog> logs = operatorLogRepository.findByTransactionDateBetween(start, end);

            String fileName = AppConstants.Report.MONTHLY_PREFIX + month.getMonth().toString() + AppConstants.Csv.EXTENSION;
            File file = generateCsv(logs, fileName, AppConstants.Report.REPORT_TITLE_MONTHLY);

            sftpUtils.uploadFile(AppConstants.Report.MONTHLY_PATH, file.getAbsolutePath(), fileName);
            Files.deleteIfExists(file.toPath());

        } catch (Exception e) {
            log.error("Failed to generate monthly report", e);
        }
    }

    private File generateCsv(List<OperatorLog> logs, String fileName, String title) throws Exception {
        File file = new File(System.getProperty("java.io.tmpdir"), fileName);
        
        // Pre-fetch balances for all users in the report
        List<String> clientIds = logs.stream()
                .map(OperatorLog::getClientId)
                .distinct()
                .collect(Collectors.toList());
        Map<String, BigDecimal> balanceMap = userBalanceViewRepository.findAllById(clientIds).stream()
                .collect(Collectors.toMap(UserBalanceView::getClientId, UserBalanceView::getBalance));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            // Print Title
            csvPrinter.printRecord(title);
            
            // Print Header
            csvPrinter.printRecord((Object[]) AppConstants.Csv.REPORT_HEADERS);

            int no = 1;
            for (OperatorLog log : logs) {
                String result = log.getStatus() == TransactionStatus.SUCCESS ? "Passed" : 
                                (log.getStatus() == TransactionStatus.FAILED ? "Failed" : log.getStatus().name());
                
                BigDecimal currentBalance = balanceMap.getOrDefault(log.getClientId(), BigDecimal.ZERO);

                csvPrinter.printRecord(
                        no++,
                        log.getClientId(),
                        "", // User Name (Not available)
                        log.getTransactionType(),
                        log.getAmount(),
                        log.getCurrency(),
                        currentBalance,
                        log.getCampaignDescription(),
                        log.getCampaignCode(),
                        log.getTransactionDate(),
                        log.getOperatorName(),
                        result,
                        log.getErrorMessage()
                );
            }
            csvPrinter.flush();
        }
        return file;
    }
}
