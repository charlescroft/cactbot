CREATE TABLE IF NOT EXISTS reward_transactions (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    client_id VARCHAR(255) NOT NULL,
    user_name VARCHAR(255),
    transaction_type VARCHAR(50) NOT NULL,
    amount DECIMAL(18,2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    transaction_date DATE NOT NULL,
    requested_by VARCHAR(255) NOT NULL,
    campaign_description VARCHAR(500),
    campaign_code VARCHAR(100),
    effective_date DATE,
    expiration_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    operator_id VARCHAR(255),
    batch_run_id VARCHAR(255),
    status VARCHAR(20) DEFAULT 'PENDING'
);

CREATE TABLE IF NOT EXISTS operator_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_date DATE,
    operator_name VARCHAR(255),
    transaction_type VARCHAR(50),
    amount DECIMAL(18, 2),
    client_id VARCHAR(255),
    status VARCHAR(20),
    filename VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    batch_run_id VARCHAR(255),
    processed_at TIMESTAMP,
    error_message TEXT,
    currency VARCHAR(3),
    campaign_description VARCHAR(500),
    campaign_code VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS system_configs (
    config_key VARCHAR(255) PRIMARY KEY,
    config_value VARCHAR(255)
);

CREATE OR REPLACE VIEW user_balance_view AS
SELECT
    client_id,
    SUM(CASE WHEN transaction_type = 'Debit' THEN -amount ELSE amount END) as balance,
    MAX(currency) as currency,
    MAX(updated_at) as last_updated_at
FROM reward_transactions
WHERE status = 'SUCCESS'
  AND (expiration_date IS NULL OR expiration_date >= CURRENT_DATE)
GROUP BY client_id;
