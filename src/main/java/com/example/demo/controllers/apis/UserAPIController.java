package com.example.demo.controllers.apis;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.DTOs.UtilDTOs;
import com.example.demo.DTOs.auth.LoginRequestDTO;
import com.example.demo.DTOs.user.UpdatePasswordDTO;
import com.example.demo.services.AuthService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/user")
public class UserAPIController {

    @Autowired
    private AuthService authService;
 
    @Autowired
    private UserService userService;

    @PutMapping("update-password")
    public ResponseEntity<UtilDTOs.Success> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO,
        Principal principal) {
        String userId = principal.getName();
        authService.login(new LoginRequestDTO(userId, updatePasswordDTO.getOldPassword()));
        userService.updatePassword(userId, updatePasswordDTO);
        return ResponseEntity.ok(new UtilDTOs.Success(true));
    }
}
