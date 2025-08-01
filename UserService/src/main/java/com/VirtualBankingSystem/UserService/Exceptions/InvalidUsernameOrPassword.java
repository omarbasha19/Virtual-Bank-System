package com.VirtualBankingSystem.UserService.Exceptions;

public class InvalidUsernameOrPassword extends BaseServiceException {
    public InvalidUsernameOrPassword(String message) {
        super(ErrorType.INVALID_CREDENTIALS, message);
    }
}
