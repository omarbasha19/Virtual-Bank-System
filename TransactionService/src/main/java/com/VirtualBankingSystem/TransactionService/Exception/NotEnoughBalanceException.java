package com.VirtualBankingSystem.TransactionService.Exception;

public class NotEnoughBalanceException extends RuntimeException {
    public NotEnoughBalanceException(String message) {
        super(message);
    }
}
