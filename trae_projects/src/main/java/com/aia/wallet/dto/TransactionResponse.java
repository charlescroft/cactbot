package com.aia.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionResponse {
    private String transactionId;
    private String clientId;
    private String transactionType;
    private BigDecimal amount;
    private String currency;
    private LocalDate transactionDate;
    private String requestedBy;
    private String campaignDescription;
    private String campaignCode;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;

    public TransactionResponse() {
    }

    public TransactionResponse(String transactionId, String clientId, String transactionType, BigDecimal amount, String currency, LocalDate transactionDate, String requestedBy, String campaignDescription, String campaignCode, LocalDate effectiveDate, LocalDate expirationDate) {
        this.transactionId = transactionId;
        this.clientId = clientId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.currency = currency;
        this.transactionDate = transactionDate;
        this.requestedBy = requestedBy;
        this.campaignDescription = campaignDescription;
        this.campaignCode = campaignCode;
        this.effectiveDate = effectiveDate;
        this.expirationDate = expirationDate;
    }

    public static TransactionResponseBuilder builder() {
        return new TransactionResponseBuilder();
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getCampaignDescription() {
        return campaignDescription;
    }

    public void setCampaignDescription(String campaignDescription) {
        this.campaignDescription = campaignDescription;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public LocalDate getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public static class TransactionResponseBuilder {
        private String transactionId;
        private String clientId;
        private String transactionType;
        private BigDecimal amount;
        private String currency;
        private LocalDate transactionDate;
        private String requestedBy;
        private String campaignDescription;
        private String campaignCode;
        private LocalDate effectiveDate;
        private LocalDate expirationDate;

        TransactionResponseBuilder() {
        }

        public TransactionResponseBuilder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public TransactionResponseBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public TransactionResponseBuilder transactionType(String transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public TransactionResponseBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public TransactionResponseBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public TransactionResponseBuilder transactionDate(LocalDate transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public TransactionResponseBuilder requestedBy(String requestedBy) {
            this.requestedBy = requestedBy;
            return this;
        }

        public TransactionResponseBuilder campaignDescription(String campaignDescription) {
            this.campaignDescription = campaignDescription;
            return this;
        }

        public TransactionResponseBuilder campaignCode(String campaignCode) {
            this.campaignCode = campaignCode;
            return this;
        }

        public TransactionResponseBuilder effectiveDate(LocalDate effectiveDate) {
            this.effectiveDate = effectiveDate;
            return this;
        }

        public TransactionResponseBuilder expirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public TransactionResponse build() {
            return new TransactionResponse(transactionId, clientId, transactionType, amount, currency, transactionDate, requestedBy, campaignDescription, campaignCode, effectiveDate, expirationDate);
        }
    }
}
