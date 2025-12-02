package com.aia.wallet.common;

public class AppConstants {

    private AppConstants() {
        // Private constructor to prevent instantiation
    }

    public static final String CURRENCY_MMK = "MMK";
    public static final String DATE_FORMAT_ISO = "yyyy-MM-dd";
    public static final String TIME_FORMAT_HH_MM = "HH:mm";

    public static class Csv {
        public static final String EXTENSION = ".csv";
        public static final String HEADER_CLIENT_ID = "Client ID";
        public static final String HEADER_USER_ID = "User_ID"; // Keep for backward compatibility if needed, or remove
        public static final String HEADER_TRANSACTION_TYPE = "Transaction Type";
        public static final String HEADER_AMOUNT = "Amount";
        public static final String HEADER_CURRENCY = "Currency";
        public static final String HEADER_TRANSACTION_DATE = "Transaction Date";
        public static final String HEADER_REQUESTED_BY = "Requested By";
        public static final String HEADER_USER_NAME = "User Name";
        public static final String HEADER_DESCRIPTION = "Campaign Description"; // Updated to match file
        public static final String HEADER_CAMPAIGN_CODE = "Campaign Code"; // Updated to match file
        public static final String HEADER_EFFECTIVE_DATE = "Effective Date";
        public static final String HEADER_EXPIRATION_DATE = "Expiration Date";

        // Report Headers
        public static final String[] REPORT_HEADERS = {
            "No", "Client ID", "User Name", "Transaction Type", "Amount", "Currency", 
            "Current Balance", "Campaign Description", "Campign Code", "Transaction Date", "Requested By", 
            "Result", "Error Message"
        };
    }

    public static class Report {
        public static final String DAILY_PREFIX = "Daily_Report_";
        public static final String MONTHLY_PREFIX = "Monthly_Report_";
        public static final String DAILY_PATH = "/reports/daily";
        public static final String MONTHLY_PATH = "/reports/monthly";
        
        public static final String REPORT_TITLE_MONTHLY = "AIA MYANMAR,AIA Monthly Wallet Listing";
        public static final String REPORT_TITLE_DAILY = "AIA MYANMAR,AIA Daily Wallet Listing";
    }

    public static class Batch {
        public static final String CONFIG_KEY_RUN_TIME = "BATCH_RUN_TIME";
        public static final String DEFAULT_RUN_TIME = "18:00";
        public static final String CHECK_INTERVAL_MS_STRING = "60000"; // For @Scheduled annotation
    }

    public static class Messages {
        public static final String ERROR_CLIENT_ID_REQUIRED = "clientId is required";
        public static final String ERROR_USER_NOT_FOUND = "User not found";
        public static final String ERROR_INVALID_TRANSACTION_TYPE = "Invalid Transaction Type: ";
        public static final String ERROR_MISSING_TRANSACTION_TYPE = "Missing Transaction Type";
        public static final String ERROR_MISSING_DATE = "Missing Transaction Date";
        public static final String ERROR_MISSING_REQUESTED_BY = "Missing Requested By / User Name";
        public static final String ERROR_MISSING_CLIENT_ID = "Missing Client ID";
        public static final String ERROR_MISSING_AMOUNT = "Missing Amount";
        public static final String ERROR_MISSING_CURRENCY = "Missing Currency";
    }
    
    public static class Api {
        public static final String PARAM_CLIENT_ID = "clientId";
    }
}
