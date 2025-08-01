# Troubleshooting SSL Certificate Issues

If you're encountering the error: 
> "Connection to API Gateway failed. Please check if the server is running and SSL certificate is accepted."

This guide will help you resolve it.

## The Problem

The Virtual Banking System uses WSO2 API Gateway with a self-signed SSL certificate. Web browsers don't trust self-signed certificates by default, which causes connection failures.

## Solution: Accept the Self-Signed Certificate

### Step 1: Open the API Gateway URL

Open a new browser tab and navigate to the API Gateway base URL:
```
https://localhost:8243/vbank/1.0.0
```

### Step 2: Accept the Certificate Warning

1. You'll see a security warning page that says something like "Your connection is not private" or "Potential Security Risk Ahead"

2. Click on "Advanced" or "Details" (wording varies by browser)

3. Click on "Proceed to localhost (unsafe)" or "Accept the Risk and Continue"

### Step 3: Return to the Application

1. After accepting the certificate, return to the banking application
2. Refresh the page
3. The application should now connect to the API Gateway successfully

## Verifying the Solution

To verify that the certificate is accepted:

1. Open your browser's Developer Tools (F12 or right-click and select "Inspect")
2. Go to the "Console" tab
3. Check that there are no SSL certificate errors

## Common Issues

### Certificate Warning Keeps Appearing

If you continue to see certificate warnings:

- Make sure you're accepting the certificate on exactly `https://localhost:8243`
- Try clearing your browser cache and cookies
- Try using a different browser

### API Gateway Not Running

If you've accepted the certificate but still see connection errors:

1. Verify the API Gateway is running by checking if `https://localhost:8243/vbank/1.0.0` loads in your browser
2. Check that the backend services are running
3. Ensure the API key in `api-service.js` is valid and not expired

## For Developers

In a production environment, you would use properly signed SSL certificates from a trusted Certificate Authority. This self-signed certificate issue only affects development environments.
