package com.example.demo.services;

import com.example.demo.DAOs.UserDAO;
import com.example.demo.DTOs.auth.CreateAccountDTO;
import com.example.demo.DTOs.user.UpdatePasswordDTO;
import com.example.demo.DTOs.user.UpdatePersonalInfoDTO;
import com.example.demo.models.User;
import com.example.demo.utils.exceptions.AuthException;
import com.example.demo.utils.messages.AuthMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    private String encodePassword(String original_password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(original_password);
    }

    public int countUsers() {
        return userDAO.count();
    }

    public void updatePassword(String userId, UpdatePasswordDTO updatePasswordDTO) {
        User user = new User();
        user.setPassword(encodePassword(updatePasswordDTO.getNewPassword()));
        user.setEmail(userId);
        userDAO.updatePassword(user);
    }

    public void updateProfile(String userId, UpdatePersonalInfoDTO updateProfileDTO) {
        User user = new User();
        user.setEmail(userId);
        user.setFullName(updateProfileDTO.getFullName());
        user.setGender(updateProfileDTO.getGender());
        userDAO.updateById(user);
    }

    public void createAccount(@NonNull CreateAccountDTO createAccountDTO) throws AuthException {
        User exitingUser = findUserByEmail(createAccountDTO.getEmail());
        if (exitingUser != null) {
            throw new AuthException(AuthMessage.EXISTING_EMAIL);
        }

        User user = new User();
        user.setEmail(createAccountDTO.getEmail());
        user.setFullName(createAccountDTO.getFullName());
        user.setGender(createAccountDTO.getGender());
        user.setPassword(encodePassword(createAccountDTO.getPassword()));
        user.setRoleID("user");

        userDAO.create(user);
    }

    public User findUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }
}
