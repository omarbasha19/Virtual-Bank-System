package com.VirtualBankingSystem.UserService.Exceptions;

public class UserAlreadyExistsException extends BaseServiceException {
    public UserAlreadyExistsException(String message) {
        super(ErrorType.USER_ALREADY_EXISTS, message);
    }
}
