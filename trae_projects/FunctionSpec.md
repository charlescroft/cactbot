### **Phase 1 Function Specification (Function Spec)**

**Objective:** In Phase 1, we will focus exclusively on implementing **Step 1** and **Step 2** from the process flow diagram. The core objective is to build a robust backend system to receive, process, and store reward data, and to provide APIs for the AIA+ application to query user reward information.

---

#### **I. Scope of Phase 1**

Based on the documentation and summary, Phase 1 **includes** the following key functionalities:

*   **SFTP-based File Upload:** Admin users will upload CSV files via SFTP. No login page is required for this phase.
*   **Backend Processing & Storage:** The backend service will process and store transaction records after a specified cutoff time.
*   **Query APIs:** Backend services will support queries for rewards account balances and transaction history.
*   **Reporting Services:** Daily and monthly reports will be generated and uploaded.
*   **Operator Logs:** Detailed operator logs will be maintained for auditing purposes.

---

#### **II. Step 1: Reward Ingestion (Input Rewards)**

This step establishes a reliable mechanism to ingest daily reward data from external systems (e.g., EDW) into the AIA Wallet system.

**1. Data Source & Format**
*   **Source:** External system `EDW`.
*   **Content:** `Eligible Rewards` (e.g., from referrals, customer campaigns).
*   **Format:** `CSV` file uploaded via `SFTP`.
*   **Required CSV Fields:**
    *   `Client_ID`
    *   `Transaction_Type`
    *   `Amount`
    *   `Currency`
    *   `Requested By`
    *   `Transaction Date`
    *   `Effective Date`
    *   `Expiration Date`
    *   *(Note: Specific field definitions are detailed in the attached document.)*

**2. Data Upload & Processing Workflow**
*   **Upload Method:** Admin users will upload files via `SFTP`, managed by the Local IT team. **A dedicated login page is not required for Phase 1.**
*   **Frequency:** Once per day.
*   **Cutoff Time:** Files must be uploaded to SFTP by **5:00 PM MMT** at the latest.
*   **Processing Trigger:** The system must automatically run the batch job (`Daily Batch`) at **6:00 PM MMT**. Manual execution capability is also mandatory.
*   **Processing Logic:**
    *   Read and parse the CSV file from SFTP.
    *   Validate each record (e.g., field completeness, data type, User ID validity).
    *   Write validated reward data to the `AIA Wallet DB`, updating the user's `Reward Balance` and `Transaction History`.
    *   **Maintain Operator Logs:** Record detailed operational logs in the database, including:
        *   `Timestamp`
        *   `Operator ID`
        *   `Transaction Type`
        *   `Amount Summary`

**3. Reporting Services**
*   **Daily Report:**
    *   After processing, generate a `CSV` format daily transaction report indicating the status of each transaction (`Success` or `Failed`).
    *   Upload this report back to `SFTP` the **following day** for verification.
    *   Additionally, generate **daily consumption records** and send them to `PAS` for reconciliation purposes.
*   **Monthly Report:**
    *   Generate a monthly summary report in `CSV` format.
    *   Upload the monthly report to the `SFTP` server.

---

#### **III. Step 2: Reward Query (Use rewards)**

This step provides API endpoints for the AIA+ mobile application to query user reward balances and transaction histories.

**1. Required APIs**
The AIA Wallet backend must provide the following two core APIs for the AIA+ app:

*   **`Balance Inquiry` API**
    *   **Function:** Returns the current reward balance for a specified user.
    *   **Input Parameter:** `User_ID` (or `NRC number`, as confirmed previously).
    *   **Output:** Current reward balance amount.

*   **`Transaction History` API**
    *   **Function:** Returns the reward transaction history for a specified user.
    *   **Input Parameter:** `User_ID` (or `NRC number`).
    *   **Output:** A list of transactions, including date, amount, description, campaign code, etc.

**2. Integration**
*   The AIA+ app will consume these APIs to display reward information to end-users.
*   Authentication and authorization for API access will be handled externally via APIM (API Management Platform), which is out of scope for Phase 1 development.

### **3. API Specification**

The AIA Wallet service exposes a set of RESTful APIs to enable the AIA+ mobile application to query user reward information. For Phase 1, the focus is strictly on two core functionalities: retrieving a user's current reward balance and fetching their detailed transaction history. All authentication and authorization for these APIs are managed externally by the APIM (API Management) layer and are out of scope for this phase.

#### **3.1 Get Reward Balance**

*   **Endpoint:** `GET /v1/rewards/balance`
*   **Description:** This endpoint returns the current reward balance for a specified user.
*   **Request Parameters:**
    *   `userId` (query, required): The unique identifier for the user (e.g., NRC number or Client ID).
*   **Successful Response (200 OK):**
    ```json
    {
        "code": 200,
        "message": "Success",
        "result": {
            "userId": "MMR123456789",
            "balance": 1500.0,
            "currency": "MMK",
            "lastUpdatedAt": "2024-10-26T18:05:00Z"
        }   
    }
    ```
*   **Error Responses:**
    *   `400 Bad Request`: Returned if the `userId` parameter is missing or invalid.
    *   `404 Not Found`: Returned if no record exists for the provided `userId`.
    *   `500 Internal Server Error`: Returned for any unexpected server-side issues.

#### **3.2 Get Transaction History**

*   **Endpoint:** `GET /v1/rewards/transactions`
*   **Description:** This endpoint retrieves the complete list of reward transactions for a specified user.
*   **Request Parameters:**
    *   `userId` (query, required): The unique identifier for the user (e.g., NRC number or Client ID).
*   **Successful Response (200 OK):**
    An array of transaction objects, each containing the following fields:
    ```json
    {
        "code": 200,
        "message": "Success",
        "result": [
            {
                "transactionId": "TXN000001",
                "userId": "MMR123456789",
                "transactionType": "Credit",
                "amount": 500.0,
                "currency": "MMK",
                "transactionDate": "2024-10-25",
                "requestedBy": "Aung San",
                "campaignDescription": "Cashback",
                "campaignCode": "Q3 promotional campaign reward",
                "effectiveDate": "2024-11-01",
                "expirationDate": "2025-10-25"
            },
      ...
    ]
    ```
*   **Error Responses:**
    *   `400 Bad Request`: Returned if the `userId` parameter is missing or invalid.
    *   `404 Not Found`: Returned if no record exists for the provided `userId`.
    *   `500 Internal Server Error`: Returned for any unexpected server-side issues.

---

### **4. Database Design**

The backend data persistence layer is designed around two primary tables to support the ingestion and querying of reward data. The design prioritizes data integrity, auditability, and efficient querying for the Phase 1 requirements.

#### **4.1 `reward_transactions` Table**

This table serves as the system's source of truth for all reward transactions. It stores every record ingested from the daily CSV files, providing a complete audit trail.

| Column Name          | Data Type         | Null? | Default Value | Description                                                                 |
|----------------------|-------------------|-------|---------------|-----------------------------------------------------------------------------|
| `transaction_id`     | BIGINT            | NO    | AUTO_INCREMENT| Primary key. System-generated unique identifier for each transaction.       |
| `user_id`            | VARCHAR(255)      | NO    |               | Unique identifier for the user (corresponds to 'User Name' in CSV).         |
| `transaction_type`   | ENUM('Credit','Debit') | NO |               | Type of transaction. Mandatory field.                                       |
| `amount`             | DECIMAL(18,2)     | NO    |               | The monetary value of the transaction. Positive for credits, negative for debits. |
| `currency`           | VARCHAR(3)        | NO    |               | Currency code (e.g., 'MMK'). Mandatory field.                               |
| `transaction_date`   | DATE              | NO    |               | Date the transaction occurred. Mandatory field.                             |
| `requested_by`       | VARCHAR(255)      | NO    |               | Name of the requester/operator who initiated the transaction. Mandatory field. |
| `campaign_description`| VARCHAR(500)     | YES   | NULL          | Human-readable description of the associated campaign. Optional.             |
| `campaign_code`      | VARCHAR(100)      | YES   | NULL          | Unique code for the campaign. Optional.                                     |
| `effective_date`     | DATE              | YES   | NULL          | Date the reward becomes active. Optional.                                   |
| `expiration_date`    | DATE              | YES   | NULL          | Date the reward expires. Optional.                                          |
| `created_at`         | TIMESTAMP         | NO    | CURRENT_TIMESTAMP | Timestamp when the record was created.                                      |
| `updated_at`         | TIMESTAMP         | NO    | CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Timestamp of the last update to the record.                                |
| `operator_id`        | VARCHAR(255)      | NO    |               | The ID of the operator (LandID) who uploaded the batch file. Critical for auditing. |
| `batch_run_id`       | VARCHAR(255)      | YES   | NULL          | Unique identifier for the batch processing run that created this record.     |
| `status`             | ENUM('PENDING','SUCCESS','FAILED') | NO | 'PENDING' | Processing status of the individual record within its batch.                |

#### **4.2 `Operator_logs` Table**

This table serves as the system's source of truth for all reward transactions. It stores every record ingested from the daily CSV files, providing a complete audit trail.

CREATE TABLE operator_logs (
    -- (Primary Key)
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '自增主键，唯一标识每条操作日志',
    transaction_date DATE NOT NULL COMMENT '交易发生的日期，对应CSV中的Transaction Date',
    operator_name VARCHAR(255) NOT NULL COMMENT '操作员姓名，对应CSV中的Requested By或User Name',
    transaction_type ENUM('Credit', 'Debit') NOT NULL COMMENT '交易类型，对应CSV中的Transaction Type',
    amount DECIMAL(18, 2) NOT NULL COMMENT '交易金额，对应CSV中的Amount列',
    client_id VARCHAR(255) NOT NULL COMMENT '客户ID，对应CSV中的Client ID',
    status ENUM('SUCCESS', 'FAILED', 'PENDING') NOT NULL COMMENT '本次操作的存储状态。SUCCESS=成功写入DB，FAILED=处理失败，PENDING=待处理',
    filename VARCHAR(500) NOT NULL COMMENT '上传的原始CSV文件名，用于追踪来源',

    -- 系统元数据字段 (System Metadata Fields)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '日志记录创建时间',
    batch_run_id VARCHAR(255) NOT NULL COMMENT '本次批处理作业的唯一ID，用于关联同一批次的所有日志',
    processed_at TIMESTAMP NULL COMMENT '本条日志对应的交易被处理完成的时间',
    error_message TEXT NULL COMMENT '如果status为FAILED，此处记录具体的错误信息'
);

---

#### **IV. Summary**

The primary focus of Phase 1 is to build a robust backend service with the following responsibilities:
1.  **Ingesting and processing** daily reward data files from EDW.
2.  **Securely storing** user reward balances and transaction histories.
3.  **Providing standardized APIs** for the AIA+ application to query user reward data.
4.  **Generating and uploading** daily and monthly reports.
5.  **Maintaining detailed operator logs** for auditability and traceability.
