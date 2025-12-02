package com.aia.wallet.job;

import com.aia.wallet.common.AppConstants;
import com.aia.wallet.entity.OperatorLog;
import com.aia.wallet.entity.SystemConfig;
import com.aia.wallet.repository.OperatorLogRepository;
import com.aia.wallet.repository.SystemConfigRepository;
import com.aia.wallet.service.BatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class DailyRewardProcessBatchJob {

    private static final Logger log = LoggerFactory.getLogger(DailyRewardProcessBatchJob.class);

    private final BatchService batchService;
    private final SystemConfigRepository systemConfigRepository;
    private final OperatorLogRepository operatorLogRepository;

    public DailyRewardProcessBatchJob(BatchService batchService, SystemConfigRepository systemConfigRepository, OperatorLogRepository operatorLogRepository) {
        this.batchService = batchService;
        this.systemConfigRepository = systemConfigRepository;
        this.operatorLogRepository = operatorLogRepository;
    }

    @Scheduled(fixedDelayString = AppConstants.Batch.CHECK_INTERVAL_MS_STRING) // Check every minute
    public void checkAndRunBatch() {
        SystemConfig config = systemConfigRepository.findById(AppConstants.Batch.CONFIG_KEY_RUN_TIME).orElse(null);
        String runTimeStr = (config != null) ? config.getConfigValue() : AppConstants.Batch.DEFAULT_RUN_TIME;
        
        String[] runTimes = runTimeStr.split(",");
        LocalTime now = getCurrentTime();
        LocalDate today = getCurrentDate();

        for (String timeStr : runTimes) {
            try {
                LocalTime runTime = LocalTime.parse(timeStr.trim(), DateTimeFormatter.ofPattern(AppConstants.TIME_FORMAT_HH_MM));

                // Run if current time is within [runTime, runTime + 5 mins]
                // Using !now.isBefore(runTime) to include exact match
                if (!now.isBefore(runTime) && now.isBefore(runTime.plusMinutes(5))) {
                    
                    // Check if already run for this specific slot today
                    // We check if any log was created between runTime and runTime + 30 mins (generous window)
                    LocalDateTime slotStart = LocalDateTime.of(today, runTime);
                    LocalDateTime slotEnd = LocalDateTime.of(today, runTime.plusMinutes(30));
                    
                    long count = operatorLogRepository.countByCreatedAtBetween(slotStart, slotEnd);
                    
                    if (count == 0) {
                        log.info("Triggering Daily Batch Job for slot {} at {}", runTime, now);
                        batchService.processDailyRewards();
                        return; // Process only one slot per execution tick
                    }
                }
            } catch (Exception e) {
                log.error("Error processing batch run time: {}", timeStr, e);
            }
        }
    }

    protected LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    protected LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
