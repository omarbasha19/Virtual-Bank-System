package com.VirtualBankingSystem.UserService.Service;

import com.VirtualBankingSystem.UserService.DTO.Response.CreateAndLoginUserResponseDTO;
import com.VirtualBankingSystem.UserService.Entity.User;
import org.springframework.http.ResponseEntity;

public class UtilityService {
    private UtilityService() {
        // Private constructor to prevent instantiation
    }

    public static ResponseEntity<CreateAndLoginUserResponseDTO> createRegisterAndLoginResponse
            (User user, String message) {
        CreateAndLoginUserResponseDTO response = new CreateAndLoginUserResponseDTO();
        response.setUsername(user.getUsername());
        response.setUserId(user.getId());
        response.setMessage(message);

        return ResponseEntity.ok(response);
    }
}
