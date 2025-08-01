package com.VirtualBankingSystem.UserService.Exceptions;

import com.VirtualBankingSystem.UserService.DTO.Response.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return buildResponse(ErrorType.USER_ALREADY_EXISTS, ex);
    }

    @ExceptionHandler(InvalidUsernameOrPassword.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidUsernameOrPassword(InvalidUsernameOrPassword ex) {
        return buildResponse(ErrorType.INVALID_CREDENTIALS, ex);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFound ex) {
        return buildResponse(ErrorType.USER_NOT_FOUND, ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();

        ErrorResponseDTO error = ErrorResponseDTO.of(ErrorType.VALIDATION_FAILED, "Validation failed", errorMessages);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String fieldName = ex.getName();
        Object invalidValue = ex.getValue();
        String message = "Invalid value for '" + fieldName + "': " + invalidValue;

        ErrorResponseDTO error = ErrorResponseDTO.of(ErrorType.TYPE_MISMATCH, message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleJsonParseException(HttpMessageNotReadableException ex) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                400,
                "Bad Request",
                "Malformed JSON request",
                List.of(ex.getMessage() != null ? ex.getMessage() :
                        "The request body is not readable or is malformed. Please check the JSON format.")
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneralException(Exception ex) {
        ErrorResponseDTO error = ErrorResponseDTO.of(ErrorType.INTERNAL_ERROR, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponseDTO> buildResponse(ErrorType errorType, Exception ex) {
        ErrorResponseDTO error = ErrorResponseDTO.of(errorType, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.valueOf(errorType.getStatus()));
    }
}
