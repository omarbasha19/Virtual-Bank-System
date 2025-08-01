package com.VirtualBankingSystem.AccountService.Exceptions;

public class ServiceInternalErrorException extends RuntimeException {
    public ServiceInternalErrorException(String message) {
        super(message);
    }
}
