/**
 * API Service for Virtual Banking System
 * Handles all communication with the backend API through the WSO2 API Gateway
 */

const API_BASE_URL = 'https://localhost:8243/vbank/1.0.0';
const API_KEY = 'Bearer eyJ4NXQiOiJNV1V4WW1Oa1pXSXdZVFEyTkRjeU1UVXdZelUxTlRReVlUbGpZekF5WmpNNU5EZ3haVFZrWkRGbE5tVmhORGt6WXpneVlqQXlNMk5pWlRBellqUTBZdyIsImtpZCI6Ik1XVXhZbU5rWldJd1lUUTJORGN5TVRVd1l6VTFOVFF5WVRsall6QXlaak01TkRneFpUVmtaREZsTm1WaE5Ea3pZemd5WWpBeU0yTmlaVEF6WWpRMFl3X1JTMjU2IiwidHlwIjoiYXQrand0IiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJjODAzOGNlNS0zNjEwLTQ3NmYtYmZjZi1jNjZiMmFlMzgzNjkiLCJhdXQiOiJBUFBMSUNBVElPTiIsImF1ZCI6Imk3WGRYYnpVQXFuOFpPS2lHT1dFdlNmWFBoUWEiLCJuYmYiOjE3NTM5MjIyNDksImF6cCI6Imk3WGRYYnpVQXFuOFpPS2lHT1dFdlNmWFBoUWEiLCJzY29wZSI6ImRlZmF1bHQiLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdDo5NDQzL29hdXRoMi90b2tlbiIsImV4cCI6MTc1MzkyNTg0OSwiaWF0IjoxNzUzOTIyMjQ5LCJqdGkiOiJjYTJhNGM5Zi02OGZlLTQ1ZjctODQ0YS1hOGQ2OGJkNzNmYjYiLCJjbGllbnRfaWQiOiJpN1hkWGJ6VUFxbjhaT0tpR09XRXZTZlhQaFFhIn0.eOFfRKJ2FV_AWdIKAgt4VwXE0myDLO330Mn2g1Myk0lwAk0q5t3YIPu1FjVgUbC5KLDuKg3-tXncIXsa86DEMuASy2FVcIEEnW5o9Acog--8HvcE9Vc3JrOLugcOSd5O8vNQ0hFaP4odLgjwaAllw7Td9ePcMqjaqR6IFRg-UuK8Mw6xZmxIvP0dc_0vOCAzBi3qJ6s4Q1reNGjEZGW3gChex9HFGfJE4yZspHLYSKBNcYdEonD0oPjwoIPWc6PqPtELbpYkY2BItQKnvkhraanpkOblvgdmUsbhmPpzsFXnLekXXF16wjU1pc12Sjfqv2V4eG4J3-uoKCnvL2EvrA';

/**
 * Makes a request to the API with the proper headers
 * @param {string} endpoint - The API endpoint to call
 * @param {Object} options - Fetch options
 * @returns {Promise<any>} The response data
 */
async function apiRequest(endpoint, options = {}) {
    // Add authorization header and content type
    const headers = {
        'Authorization': API_KEY,
        'Content-Type': 'application/json',
        ...options.headers
    };

    try {
        // For development environment, we need to handle self-signed SSL certificate
        const fetchOptions = {
            ...options,
            headers,
            mode: 'cors', // Explicitly set CORS mode
        };
        
        // Make the fetch call
        const response = await fetch(`${API_BASE_URL}${endpoint}`, fetchOptions);

        // Parse the response
        let data;
        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        } else {
            data = await response.text();
        }

        // Check if the response is successful
        if (!response.ok) {
            const errorMsg = data.message || data.error || (typeof data === 'string' ? data : 'An error occurred');
            throw new Error(errorMsg);
        }

        return data;
    } catch (error) {
        console.error('API request failed:', error);
        // Handle specific network errors (like SSL certificate issues)
        if (error.name === 'TypeError' && error.message.includes('Failed to fetch')) {
            localStorage.setItem('sslErrorDetected', 'true');
            const errorMessage = 'Connection to API Gateway failed. Please check if the server is running and SSL certificate is accepted.\n\n' +
                'To fix this issue:\n' +
                '1. Open a new tab and navigate to https://localhost:8243/vbank/1.0.0\n' +
                '2. Click "Advanced" and then "Proceed to localhost (unsafe)"\n' +
                '3. Return to this page and refresh';
            throw new Error(errorMessage);
        }
        throw error;
    }
}

// User Service APIs
const UserService = {
    // Register a new user
    register: async (userData) => {
        return apiRequest('/users/register', {
            method: 'POST',
            body: JSON.stringify(userData)
        });
    },

    // Login a user
    login: async (credentials) => {
        return apiRequest('/users/login', {
            method: 'POST',
            body: JSON.stringify(credentials)
        });
    },

    // Get user profile
    getUserProfile: async (userId) => {
        return apiRequest(`/users/${userId}/profile`, {
            method: 'GET'
        });
    }
};

// Account Service APIs
const AccountService = {
    // Get account details
    getAccountDetails: async (accountId) => {
        return apiRequest(`/accounts/${accountId}`, {
            method: 'GET'
        });
    },

    // Get user accounts
    getUserAccounts: async (userId) => {
        return apiRequest(`/users/${userId}/accounts`, {
            method: 'GET'
        });
    },

    // Create a new account
    createAccount: async (accountData) => {
        return apiRequest('/accounts', {
            method: 'POST',
            body: JSON.stringify(accountData)
        });
    },

    // Transfer money between accounts
    transferMoney: async (transferData) => {
        return apiRequest('/accounts/transfer', {
            method: 'POST',
            body: JSON.stringify(transferData)
        });
    }
};

// Transaction Service APIs
const TransactionService = {
    // Get account transactions
    getAccountTransactions: async (accountId) => {
        return apiRequest(`/accounts/${accountId}/transactions`, {
            method: 'GET'
        });
    },

    // Initiate a transaction (two-phase transaction process)
    initiateTransaction: async (transactionData) => {
        return apiRequest('/transactions/transfer/initiation', {
            method: 'POST',
            body: JSON.stringify(transactionData)
        });
    },

    // Execute a transaction (second phase of transaction)
    executeTransaction: async (transactionData) => {
        return apiRequest('/transactions/transfer/execution', {
            method: 'POST',
            body: JSON.stringify(transactionData)
        });
    }
};

// BFF (Backend For Frontend) Service APIs
const BFFService = {
    // Get dashboard data (aggregates user, accounts, transactions)
    getDashboard: async (userId) => {
        return apiRequest(`/bff/dashboard/${userId}`, {
            method: 'GET'
        });
    }
};

// Authentication and Utility functions
const AuthUtils = {
    // Save user data to local storage with session expiration
    saveUserData: (userData) => {
        // Handle cases where the whole login response is passed, by extracting the actual user object
        const userToSave = (userData && userData.user && typeof userData.user === 'object') ? userData.user : userData;

        // Add session expiration timestamp (8 hours from login)
        const sessionData = {
            user: userToSave,
            expiresAt: Date.now() + (8 * 60 * 60 * 1000) // 8 hours in milliseconds
        };
        localStorage.setItem('userSession', JSON.stringify(sessionData));
    },

    // Get user data from local storage (with expiration check)
    getUserData: () => {
        const sessionDataString = localStorage.getItem('userSession');
        if (!sessionDataString) return null;
        
        const sessionData = JSON.parse(sessionDataString);
        
        // Check if session has expired
        if (Date.now() > sessionData.expiresAt) {
            AuthUtils.clearUserData();
            return null;
        }
        
        const user = sessionData.user;
        
        // Handle nested user object, which might occur from incorrect saving
        if (user && user.user && typeof user.user === 'object') {
            return user.user;
        }
        
        return user;
    },

    // Clear user data (logout)
    clearUserData: () => {
        localStorage.removeItem('userSession');
    },

    // Check if user is logged in with valid session
    isLoggedIn: () => {
        const userData = AuthUtils.getUserData();
        return !!userData;
    },
    
    // Format account number for display (hide all but last 4 digits)
    formatAccountNumber: (accountNumber) => {
        if (!accountNumber) return 'N/A';
        return `****${accountNumber.slice(-4)}`;
    },
    
    // Format currency amount
    formatCurrency: (amount) => {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    },
    
    // Format date for display
    formatDate: (dateString) => {
        return new Date(dateString).toLocaleString();
    },
    
    // Redirect to login if not authenticated
    requireAuth: () => {
        if (!AuthUtils.isLoggedIn()) {
            alert('Please log in to access this page');
            window.location.href = 'Ejada Login Form.html';
            return false;
        }
        return true;
    },
    
    // Extend session expiration
    extendSession: () => {
        const userData = AuthUtils.getUserData();
        if (userData) {
            AuthUtils.saveUserData(userData);
        }
    }
};

// Utility for showing notifications and handling UI states
const UIUtils = {
    // Show status message with appropriate styling
    showStatus: (element, message, type = 'info') => {
        if (typeof element === 'string') {
            element = document.getElementById(element);
        }
        
        if (!element) return;
        
        element.textContent = message;
        element.className = type;
        element.style.display = 'block';
    },
    
    // Set loading state for a button
    setButtonLoading: (button, isLoading, originalText, loadingText = 'Processing...') => {
        if (typeof button === 'string') {
            button = document.getElementById(button);
        }
        
        if (!button) return;
        
        button.disabled = isLoading;
        button.textContent = isLoading ? loadingText : originalText;
    },
    
    // Format account type for display
    formatAccountType: (type) => {
        if (!type) return 'Standard Account';
        
        // Remove underscores and capitalize
        return type.replace(/_/g, ' ')
            .split(' ')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
            .join(' ');
    },
    
    // Handle API errors
    handleApiError: (error, statusElement) => {
        console.error('API error:', error);
        const errorMessage = error.message || 'An unexpected error occurred. Please try again.';
        
        if (statusElement) {
            UIUtils.showStatus(statusElement, errorMessage, 'error');
        } else {
            alert(errorMessage);
        }
    }
};
