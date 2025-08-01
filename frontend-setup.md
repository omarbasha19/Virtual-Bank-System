# Frontend Setup & Integration Guide

This guide explains how to set up and run the frontend application with the backend API Gateway.

## Prerequisites

- Web server (such as [Live Server](https://marketplace.visualstudio.com/items?itemName=ritwickdey.LiveServer) extension for VS Code)
- Modern web browser (Chrome, Firefox, Edge)
- WSO2 API Gateway running locally (see Backend Setup section)

## Frontend Files

The frontend consists of the following key files:

- **Login Page**: `Ejada Login Form.html` - User authentication
- **Signup Page**: `signup.html` - New user registration
- **Account Details**: `Account Details Page.html` - Dashboard view of accounts and transactions
- **Create Account**: `Create Account Page.html` - Form to create a new bank account
- **Transfer Page**: `Ejada Transfer Page.html` - Money transfer between accounts
- **Transfer Confirmation**: `Transfer Confirmation Page.html` - Receipt page after transfer

## API Service

The `js/api-service.js` file contains all the necessary code for communicating with the backend API. It includes:

- API request handling with proper error management
- Service-specific API functions (UserService, AccountService, etc.)
- Authentication utilities
- UI helpers for consistent user experience

## SSL Certificate Setup (Development Only)

Since the API Gateway uses a self-signed SSL certificate (https://localhost:8243), you'll need to accept this certificate in your browser:

1. Open your browser and navigate to: `https://localhost:8243/vbank/1.0.0`
2. You'll see a security warning. Click "Advanced" and then "Proceed to localhost" (wording may vary by browser)
3. You may need to do this once per browser session

## Running the Application

1. Open the project in Visual Studio Code
2. If using Live Server, right-click on `Ejada Login Form.html` and select "Open with Live Server"
3. The application will open in your default browser
4. For testing, you can create a new user account or use the default credentials:
   - Username: `admin`
   - Password: `admin123`

## Integration with Backend

The frontend integrates with the following backend microservices through the API Gateway:

- **User Service**: Authentication, registration, and user profile management
- **Account Service**: Account creation and management
- **Transaction Service**: Money transfers and transaction history
- **BFF Service**: Aggregated data for the dashboard

## Troubleshooting

If you encounter connection issues:

1. **API Gateway Connection**:
   - Verify that the API Gateway is running
   - Check browser console for SSL certificate errors
   - Ensure the API key in `api-service.js` is valid

2. **CORS Issues**:
   - If using a different origin for frontend and backend, ensure CORS is properly configured
   - When testing locally, running both frontend and backend on localhost helps avoid CORS problems

3. **Authentication Problems**:
   - Check browser console for specific error messages
   - Ensure the login credentials are correct
   - Session expiration is set to 8 hours, after which you'll need to login again

## Default Development Configuration

- API Gateway URL: `https://localhost:8243/vbank/1.0.0`
- SSL: Self-signed certificate (development only)
- Authentication: Bearer token with API key
