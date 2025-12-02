package com.aia.wallet.job;

import com.aia.wallet.common.AppConstants;
import com.aia.wallet.entity.SystemConfig;
import com.aia.wallet.repository.OperatorLogRepository;
import com.aia.wallet.repository.SystemConfigRepository;
import com.aia.wallet.service.BatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailyRewardProcessBatchJobTest {

    @Mock
    private BatchService batchService;
    @Mock
    private SystemConfigRepository systemConfigRepository;
    @Mock
    private OperatorLogRepository operatorLogRepository;

    private DailyRewardProcessBatchJob batchJob;

    @BeforeEach
    void setUp() {
        // Default setup - can be overridden in tests if needed by re-instantiating
        batchJob = new DailyRewardProcessBatchJob(batchService, systemConfigRepository, operatorLogRepository) {
            @Override
            protected LocalTime getCurrentTime() {
                return LocalTime.of(14, 0); // Default test time matches one of the configured times
            }

            @Override
            protected LocalDate getCurrentDate() {
                return LocalDate.of(2023, 10, 25);
            }
        };
    }

    @Test
    void checkAndRunBatch_ShouldRun_WhenTimeMatchesAndNoLogs() {
        // Arrange
        when(systemConfigRepository.findById(AppConstants.Batch.CONFIG_KEY_RUN_TIME))
                .thenReturn(Optional.of(new SystemConfig(AppConstants.Batch.CONFIG_KEY_RUN_TIME, "10:00,14:00")));
        
        when(operatorLogRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0L);

        // Act
        batchJob.checkAndRunBatch();

        // Assert
        verify(batchService, times(1)).processDailyRewards();
    }

    @Test
    void checkAndRunBatch_ShouldNotRun_WhenTimeMatchesButLogsExist() {
        // Arrange
        when(systemConfigRepository.findById(AppConstants.Batch.CONFIG_KEY_RUN_TIME))
                .thenReturn(Optional.of(new SystemConfig(AppConstants.Batch.CONFIG_KEY_RUN_TIME, "10:00,14:00")));
        
        when(operatorLogRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(1L);

        // Act
        batchJob.checkAndRunBatch();

        // Assert
        verify(batchService, never()).processDailyRewards();
    }

    @Test
    void checkAndRunBatch_ShouldNotRun_WhenTimeDoesNotMatch() {
        // Arrange
        // Re-instantiate with a different time
        batchJob = new DailyRewardProcessBatchJob(batchService, systemConfigRepository, operatorLogRepository) {
            @Override
            protected LocalTime getCurrentTime() {
                return LocalTime.of(15, 0); // Not 10:00 or 14:00
            }
            @Override
            protected LocalDate getCurrentDate() {
                return LocalDate.of(2023, 10, 25);
            }
        };
        
        when(systemConfigRepository.findById(AppConstants.Batch.CONFIG_KEY_RUN_TIME))
                .thenReturn(Optional.of(new SystemConfig(AppConstants.Batch.CONFIG_KEY_RUN_TIME, "10:00,14:00")));

        // Act
        batchJob.checkAndRunBatch();

        // Assert
        verify(batchService, never()).processDailyRewards();
    }
    
    @Test
    void checkAndRunBatch_ShouldRun_WhenExactMatch() {
        // Test boundary condition: exact match
         batchJob = new DailyRewardProcessBatchJob(batchService, systemConfigRepository, operatorLogRepository) {
            @Override
            protected LocalTime getCurrentTime() {
                return LocalTime.of(14, 0, 0);
            }
            @Override
            protected LocalDate getCurrentDate() {
                return LocalDate.of(2023, 10, 25);
            }
        };
        
        when(systemConfigRepository.findById(AppConstants.Batch.CONFIG_KEY_RUN_TIME))
                .thenReturn(Optional.of(new SystemConfig(AppConstants.Batch.CONFIG_KEY_RUN_TIME, "14:00")));
        when(operatorLogRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0L);

        batchJob.checkAndRunBatch();

        verify(batchService, times(1)).processDailyRewards();
    }
    
    @Test
    void checkAndRunBatch_ShouldRun_WhenInsideWindow() {
        // Test boundary condition: inside 5 min window (e.g. 14:04)
         batchJob = new DailyRewardProcessBatchJob(batchService, systemConfigRepository, operatorLogRepository) {
            @Override
            protected LocalTime getCurrentTime() {
                return LocalTime.of(14, 4, 59);
            }
            @Override
            protected LocalDate getCurrentDate() {
                return LocalDate.of(2023, 10, 25);
            }
        };
        
        when(systemConfigRepository.findById(AppConstants.Batch.CONFIG_KEY_RUN_TIME))
                .thenReturn(Optional.of(new SystemConfig(AppConstants.Batch.CONFIG_KEY_RUN_TIME, "14:00")));
        when(operatorLogRepository.countByCreatedAtBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(0L);

        batchJob.checkAndRunBatch();

        verify(batchService, times(1)).processDailyRewards();
    }
    
    @Test
    void checkAndRunBatch_ShouldNotRun_WhenOutsideWindow() {
        // Test boundary condition: outside 5 min window (e.g. 14:05)
         batchJob = new DailyRewardProcessBatchJob(batchService, systemConfigRepository, operatorLogRepository) {
            @Override
            protected LocalTime getCurrentTime() {
                return LocalTime.of(14, 5, 0);
            }
            @Override
            protected LocalDate getCurrentDate() {
                return LocalDate.of(2023, 10, 25);
            }
        };
        
        when(systemConfigRepository.findById(AppConstants.Batch.CONFIG_KEY_RUN_TIME))
                .thenReturn(Optional.of(new SystemConfig(AppConstants.Batch.CONFIG_KEY_RUN_TIME, "14:00")));

        batchJob.checkAndRunBatch();

        verify(batchService, never()).processDailyRewards();
    }
}