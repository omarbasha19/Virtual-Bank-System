package com.VirtualBankingSystem.TransactionService.Exception;

public class ServiceInternalErrorException extends RuntimeException {
    public ServiceInternalErrorException(String message) {
        super(message);
    }
}
