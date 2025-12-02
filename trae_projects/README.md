# AIA Wallet Backend Service

This is the backend service for the AIA Wallet application, built with Spring Boot 3.
It handles reward data ingestion via SFTP, processes transactions, and provides APIs for the AIA+ app.

## Tech Stack

*   **Java**: 17
*   **Framework**: Spring Boot 3.2.0
*   **Database**: MySQL (Development), MSSQL (Production/Azure)
*   **ORM**: Spring Data JPA
*   **Tools**: Docker, Commons-CSV, JSch (SFTP)

## Project Structure

*   `src/main/java/com/aia/wallet/`
    *   `controller`: REST API Controllers
    *   `service`: Business Logic (Reward, Batch, Report)
    *   `mapper`: MyBatis Mappers
    *   `entity`: Database Entities
    *   `job`: Scheduled Tasks (`BatchJob`)
    *   `common`: Utilities (`SftpUtils`)
    *   `config`: Configuration classes (if any)
*   `src/main/resources/`
    *   `application.yml`: Application configuration
    *   `schema.sql`: Database schema initialization
    *   `data.sql`: Initial data

## Features

1.  **SFTP Integration**: Automatically downloads daily reward CSV files from a configured SFTP server.
2.  **Batch Processing**: Parses CSV files, validates data, and updates reward balances.
3.  **REST APIs**:
    *   `GET /v1/rewards/balance?userId={userId}`: Get user reward balance.
    *   `GET /v1/rewards/transactions?userId={userId}`: Get user transaction history.
4.  **Reporting**: Generates Daily and Monthly reports and uploads them to SFTP.
5.  **Scheduling**: Configurable batch run time (default 18:00 daily).

## Configuration

### Database
By default, the application connects to a local MySQL database.
Update `src/main/resources/application.yml` or set environment variables:
*   `DB_URL`
*   `DB_USERNAME`
*   `DB_PASSWORD`

### SFTP
Configure SFTP connection in `application.yml` or via env vars:
*   `SFTP_HOST`
*   `SFTP_PORT`
*   `SFTP_USERNAME`
*   `SFTP_PASSWORD`
*   `SFTP_REMOTE_DIR` (Upload location for inputs)
*   `SFTP_DOWNLOAD_DIR` (Local temp download path)

### System Configuration (Database)
The application uses a `system_configs` table for dynamic configuration.

**Batch Job Run Time**
*   Key: `BATCH_RUN_TIME`
*   Value: Comma-separated times in `HH:mm` format (e.g., `10:00,15:00,18:00`).
*   Description: Controls when the daily reward processing batch job runs. The system checks every minute and triggers the job if the current time matches one of the configured times (within a 5-minute window).

**SQL Example:**
```sql
-- Set batch job to run at 10:00 AM and 6:00 PM daily
INSERT INTO system_configs (config_key, config_value) 
VALUES ('BATCH_RUN_TIME', '10:00,18:00') 
ON DUPLICATE KEY UPDATE config_value='10:00,18:00';
```

## Running the Application

### Locally
1.  Ensure MySQL is running and a database `aia_wallet` exists.
2.  Run with Maven:
    ```bash
    mvn spring-boot:run
    ```

### Docker
1.  Build the image:
    ```bash
    docker build -t aia-wallet-backend .
    ```
2.  Run the container:
    ```bash
    docker run -d -p 8080:8080 \
      -e DB_URL=jdbc:mysql://host.docker.internal:3306/aia_wallet \
      -e DB_USERNAME=root \
      -e DB_PASSWORD=password \
      -e SFTP_HOST=sftp.example.com \
      aia-wallet-backend
    ```

## Database Schema
The schema is automatically initialized using `schema.sql` if using MySQL.
For MSSQL, please convert the schema accordingly (e.g., change `AUTO_INCREMENT` to `IDENTITY`).

## API Documentation

### Get Balance
`GET /v1/rewards/balance?userId=MMR123`

Response:
```json
{
    "code": 200,
    "message": "Success",
    "result": {
        "userId": "MMR123",
        "balance": 1000.00,
        "currency": "MMK",
        "lastUpdatedAt": "2024-10-27T10:00:00"
    }
}
```

### Get Transactions
`GET /v1/rewards/transactions?userId=MMR123`

Response:
```json
{
    "code": 200,
    "message": "Success",
    "result": [
        {
            "transactionId": "1",
            "userId": "MMR123",
            "amount": 500.00,
            "type": "Credit",
            ...
        }
    ]
}
```
