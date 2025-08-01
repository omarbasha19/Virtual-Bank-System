package com.VirtualBankingSystem.UserService.Exceptions;

public class UserNotFound extends BaseServiceException {
    public UserNotFound(String message) {
        super(ErrorType.USER_NOT_FOUND, message);
    }
}
