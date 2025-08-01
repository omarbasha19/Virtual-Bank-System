package com.VirtualBankingSystem.UserService.Exceptions;

public enum ErrorType {
    USER_ALREADY_EXISTS(409, "Conflict", "A user with the same username or email already exists. Please choose a different username or email."),
    INVALID_CREDENTIALS(401, "Unauthorized", "The username or password provided is incorrect. Please try again."),
    USER_NOT_FOUND(404, "Not Found", "The requested user could not be found."),
    VALIDATION_FAILED(400, "Bad Request", "Validation failed for one or more fields."),
    TYPE_MISMATCH(400, "Bad Request", "Expected format: UUID"),
    INTERNAL_ERROR(500, "Internal Server Error", "An unexpected error occurred.");

    private final int status;
    private final String title;
    private final String defaultDetail;

    ErrorType(int status, String title, String defaultDetail) {
        this.status = status;
        this.title = title;
        this.defaultDetail = defaultDetail;
    }

    public int getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getDefaultDetail() {
        return defaultDetail;
    }
}
