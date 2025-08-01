package com.VirtualBankingSystem.UserService.DTO.Response;

import com.VirtualBankingSystem.UserService.Exceptions.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {
    private int status;
    private String errorType;
    private String message;
    private List<String> details;

    public static ErrorResponseDTO of(ErrorType type, String message) {
        return new ErrorResponseDTO(
                type.getStatus(),
                type.getTitle(),
                message,
                List.of(type.getDefaultDetail())
        );
    }

    public static ErrorResponseDTO of(ErrorType type, String message, List<String> details) {
        return new ErrorResponseDTO(
                type.getStatus(),
                type.getTitle(),
                message,
                details == null ? Collections.emptyList() : details
        );
    }
}
