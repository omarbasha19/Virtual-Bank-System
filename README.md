# Ejada Virtual Banking System - Setup Guide for Windows

This guide provides step-by-step instructions for setting up and running the Ejada Virtual Banking System on Windows. The system is built as a microservices architecture using Spring Boot.

## Prerequisites

- **JDK 21**: [Download and install Java Development Kit 21](https://www.oracle.com/java/technologies/downloads/#java21)
- **MySQL 8**: [Download and install MySQL 8.0](https://dev.mysql.com/downloads/installer/)
- **Apache Kafka**: [Download Kafka](https://kafka.apache.org/downloads) (Only required if you want to use the logging features)
- **Web Browser**: Chrome, Firefox, or Edge for accessing the frontend

## Database Setup

1. **Create the MySQL databases**:
   - Open MySQL Workbench or MySQL Command Line Client
   - Log in with your root credentials
   - Execute the following commands:

```sql
CREATE DATABASE user_service_db;
CREATE DATABASE account_service_db;
CREATE DATABASE transaction_service_db;
CREATE DATABASE log_service_db;

CREATE USER 'Ejada-Virtual-Banking'@'localhost' IDENTIFIED BY 'tony1944';
GRANT ALL PRIVILEGES ON user_service_db.* TO 'Ejada-Virtual-Banking'@'localhost';
GRANT ALL PRIVILEGES ON account_service_db.* TO 'Ejada-Virtual-Banking'@'localhost';
GRANT ALL PRIVILEGES ON transaction_service_db.* TO 'Ejada-Virtual-Banking'@'localhost';
GRANT ALL PRIVILEGES ON log_service_db.* TO 'Ejada-Virtual-Banking'@'localhost';
FLUSH PRIVILEGES;
```

## Kafka Setup (Optional - Required for Logging Service)

1. **Download and Extract Kafka**:
   - Extract the downloaded Kafka archive to a location of your choice (e.g., `C:\kafka`)

2. **Start Zookeeper**:
   - Open a PowerShell window and navigate to the Kafka directory
   - Run:
```powershell
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```

3. **Start Kafka Server**:
   - Open another PowerShell window and navigate to the Kafka directory
   - Run:
```powershell
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

4. **Create the Kafka Topic**:
   - Open another PowerShell window and navigate to the Kafka directory
   - Run:
```powershell
.\bin\windows\kafka-topics.bat --create --topic vbank-logs-topic --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1
```

## Building and Running Services

### 1. Build All Services

Open PowerShell and navigate to the project directory:

```powershell
cd "C:\Users\meton\Downloads\Ejada Project"
```

Build each service individually using the Maven wrapper provided:

```powershell
# Eureka Server
cd .\Eureka-Server\
.\mvnw.cmd clean install
cd ..

# User Service
cd .\UserService\
.\mvnw.cmd clean install
cd ..

# Account Service
cd .\AccountService\
.\mvnw.cmd clean install
cd ..

# Transaction Service
cd .\TransactionService\
.\mvnw.cmd clean install
cd ..

# Logging Service
cd .\LoggingService\
.\mvnw.cmd clean install
cd ..

# BFF Service
cd .\BFFService\
.\mvnw.cmd clean install
cd ..
```

### 2. Run Services in the Correct Order

#### Step 1: Start the Eureka Server (Service Discovery)

Open a new PowerShell window:

```powershell
cd "C:\Users\meton\Downloads\Ejada Project\Eureka-Server"
.\mvnw.cmd spring-boot:run
```

Wait until you see the message indicating that the Eureka Server has started successfully. You can verify by accessing the Eureka dashboard at `http://localhost:8761` in your browser.

#### Step 2: Start the User Service

Open a new PowerShell window:

```powershell
cd "C:\Users\meton\Downloads\Ejada Project\UserService"
.\mvnw.cmd spring-boot:run
```

Wait for it to register with Eureka.

#### Step 3: Start the Account Service

Open a new PowerShell window:

```powershell
cd "C:\Users\meton\Downloads\Ejada Project\AccountService"
.\mvnw.cmd spring-boot:run
```

Wait for it to register with Eureka.

#### Step 4: Start the Transaction Service

Open a new PowerShell window:

```powershell
cd "C:\Users\meton\Downloads\Ejada Project\TransactionService"
.\mvnw.cmd spring-boot:run
```

Wait for it to register with Eureka.

#### Step 5: Start the Logging Service (Optional)

Only required if Kafka is running:

Open a new PowerShell window:

```powershell
cd "C:\Users\meton\Downloads\Ejada Project\LoggingService"
.\mvnw.cmd spring-boot:run
```

#### Step 6: Start the BFF Service

Open a new PowerShell window:

```powershell
cd "C:\Users\meton\Downloads\Ejada Project\BFFService"
.\mvnw.cmd spring-boot:run
```

### 3. Verify All Services Are Running

Open your web browser and navigate to `http://localhost:8761`. You should see all services registered in the Eureka dashboard:

- UserService (port 9090)
- AccountService (port 9091)  
- TransactionService (port 9094)
- LoggingService (port 9095)
- BFFService (port 9099)

## Accessing the Front-End

The front-end HTML files are located in the `front-end/front-end` directory. To access the application:

1. Navigate to the front-end directory:
```
C:\Users\meton\Downloads\Ejada Project\front-end\front-end
```

2. Open the `Ejada Login Page.html` file in your web browser.

3. Use the login form to access the banking application.

## Service Endpoints

- **Eureka Server**: http://localhost:8761
- **User Service**: http://localhost:9090
- **Account Service**: http://localhost:9091
- **Transaction Service**: http://localhost:9094
- **Logging Service**: http://localhost:9095
- **BFF Service**: http://localhost:9099

## Troubleshooting

### Database Connection Issues

- Verify your MySQL server is running
- Check that the database credentials match those in the `application.properties` files
- Ensure the databases have been created correctly

### Service Registration Issues

- Make sure Eureka Server is running before starting other services
- Check if the service is trying to connect to Eureka at the correct address (default: `http://localhost:8761/eureka`)
- Verify there are no port conflicts with other applications

### Kafka Issues

- Ensure both Zookeeper and Kafka servers are running
- Verify the topic `vbank-logs-topic` has been created
- Check that Kafka is running on the default port (9092)

### JDK Version Issues

- Verify you have JDK 21 installed
- Check that JAVA_HOME environment variable is set correctly

## Shutting Down the System

To shut down the system, press `Ctrl+C` in each service's PowerShell window.

---
@echo off
title Ejada Virtual Banking - Run & Test
color 0A

echo =========================================
echo   Ejada Virtual Banking - Run & Test
echo =========================================

:: 1. Configure WSO2
echo [1/7] Configure WSO2 API Gateway
echo - Open WSO2 Management Console
echo - Make sure all API Endpoints are correctly configured and working
pause

:: 2. Generate Access Key
echo [2/7] Generate Access Key from WSO2 Developer Portal
echo - Open Developer Portal
echo - Select the published API Product
echo - Generate an Access Key and keep it for testing
pause

:: 3. Test APIs with Postman
echo [3/7] Test APIs using Postman
echo - Open Postman
echo - Add the generated key in the correct header (Authorization or Key)
echo - Test all endpoints and verify the responses
pause

:: 4. Start Backend Microservices
echo [4/7] Starting Backend Services...
start powershell -NoExit -Command "cd 'Eureka-Server'; .\mvnw.cmd spring-boot:run"
timeout /t 10 >nul
start powershell -NoExit -Command "cd 'UserService'; .\mvnw.cmd spring-boot:run"
timeout /t 5 >nul
start powershell -NoExit -Command "cd 'AccountService'; .\mvnw.cmd spring-boot:run"
timeout /t 5 >nul
start powershell -NoExit -Command "cd 'TransactionService'; .\mvnw.cmd spring-boot:run"
timeout /t 5 >nul
start powershell -NoExit -Command "cd 'LoggingService'; .\mvnw.cmd spring-boot:run"
timeout /t 5 >nul
start powershell -NoExit -Command "cd 'BFFService'; .\mvnw.cmd spring-boot:run"

echo - Check http://localhost:8761 to make sure all services are registered
pause

:: 5. Open Frontend Login Page
echo [5/7] Opening Frontend Login Page...
start "" "front-end\front-end\Ejada Login Page.html"
pause

:: 6. Enable CORS Unblock Extension
echo [6/7] Enable CORS Unblock in your browser
echo - Open the CORS Unblock extension in your browser toolbar
echo - Check all important options (Access-Control-Allow-Origin, Methods, Headers, etc.)
echo - Click START to enable
echo Extension link: https://chromewebstore.google.com/detail/cors-unblock/lfhmikememgdcahcdlaciloancbhjino?hl=en
pause

:: 7. Test Login Integration
echo [7/7] Test the login page with backend integration
echo - Enter valid login credentials
echo - Verify that requests reach the backend without CORS issues
pause

echo =========================================
echo All steps completed. The system is ready.
echo =========================================
pause

