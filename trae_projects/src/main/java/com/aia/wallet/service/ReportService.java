package com.aia.wallet.service;

import java.time.LocalDate;

public interface ReportService {
    void generateAndUploadDailyReport(LocalDate date);
    void generateAndUploadMonthlyReport(LocalDate month);
}
