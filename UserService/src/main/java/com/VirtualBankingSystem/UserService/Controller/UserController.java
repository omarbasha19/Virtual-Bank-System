package com.VirtualBankingSystem.UserService.Controller;

import com.VirtualBankingSystem.UserService.Adapter.UserAdapter;
import com.VirtualBankingSystem.UserService.DTO.Request.CreateUserRequestDTO;
import com.VirtualBankingSystem.UserService.DTO.Request.UserLoginRequestDTO;
import com.VirtualBankingSystem.UserService.DTO.Response.CreateAndLoginUserResponseDTO;
import com.VirtualBankingSystem.UserService.DTO.Response.UserProfileResponseDTO;
import com.VirtualBankingSystem.UserService.Entity.User;
import com.VirtualBankingSystem.UserService.Exceptions.InvalidUsernameOrPassword;
import com.VirtualBankingSystem.UserService.Exceptions.UserAlreadyExistsException;
import com.VirtualBankingSystem.UserService.Service.UserService;
import com.VirtualBankingSystem.UserService.Service.UtilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CreateAndLoginUserResponseDTO> createUser(@RequestBody @Valid CreateUserRequestDTO user) {
        User newUser = UserAdapter.convertCreateUserRequestToUser(user);
        userService.saveUser(newUser);
        return UtilityService.createRegisterAndLoginResponse(newUser, "User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<CreateAndLoginUserResponseDTO> loginUser(@RequestBody @Valid UserLoginRequestDTO user) {
        User existingUser = userService.loginUser(user.getUsername(), user.getPassword());
        return UtilityService.createRegisterAndLoginResponse(existingUser, "User logged in successfully");
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponseDTO> getUserProfile(@PathVariable UUID userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(UserAdapter.convertUserToUserProfileResponse(user));
    }
}
