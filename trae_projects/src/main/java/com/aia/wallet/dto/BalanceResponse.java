package com.aia.wallet.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BalanceResponse {
    private String clientId;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime lastUpdatedAt;

    public BalanceResponse() {
    }

    public BalanceResponse(String clientId, BigDecimal balance, String currency, LocalDateTime lastUpdatedAt) {
        this.clientId = clientId;
        this.balance = balance;
        this.currency = currency;
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public static BalanceResponseBuilder builder() {
        return new BalanceResponseBuilder();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public static class BalanceResponseBuilder {
        private String clientId;
        private BigDecimal balance;
        private String currency;
        private LocalDateTime lastUpdatedAt;

        BalanceResponseBuilder() {
        }

        public BalanceResponseBuilder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public BalanceResponseBuilder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public BalanceResponseBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public BalanceResponseBuilder lastUpdatedAt(LocalDateTime lastUpdatedAt) {
            this.lastUpdatedAt = lastUpdatedAt;
            return this;
        }

        public BalanceResponse build() {
            return new BalanceResponse(clientId, balance, currency, lastUpdatedAt);
        }
    }
}
