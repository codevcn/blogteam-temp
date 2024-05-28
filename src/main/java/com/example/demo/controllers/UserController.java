package com.example.demo.controllers;

import java.io.IOException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.DTOs.UtilDTOs;
import com.example.demo.DTOs.user.UpdatePersonalInfoDTO;
import com.example.demo.configs.props.AppInfoProps;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import com.example.demo.utils.client.ClientGlobalVarNames;
import com.example.demo.utils.client.ClientPages;
import com.example.demo.utils.exceptions.BaseException;
import jakarta.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private AppInfoProps appInfoProps;

    @Autowired
    private UserService userService;

    @GetMapping("account")
    public String accountPage(Model model, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("nameOfUser", user.getFullName());
        return ClientPages.accountPage;
    }

    @GetMapping("account/profile")
    public String editProfile(Model model, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute("user", user);
        UtilDTOs.Result result = new UtilDTOs.Result(false, false, null);
        model.addAttribute(ClientGlobalVarNames.result, result);
        return ClientPages.profilePage;
    }

    @PostMapping("account/update-personal-info")
    public String updatePersonalInfo(@NonNull @Valid UpdatePersonalInfoDTO updateProfileDTO,
        BindingResult bindingResult, Model model, Principal principal) throws BaseException, IOException {
        String userId = principal.getName();

        if (bindingResult.hasErrors()) {
            User user = userService.findUserByEmail(userId);
            model.addAttribute("user", user);
            UtilDTOs.Result result =
                new UtilDTOs.Result(true, false, bindingResult.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute(ClientGlobalVarNames.result, result);
            return ClientPages.profilePage;
        }

        userService.updateProfile(userId, updateProfileDTO);
        User user = userService.findUserByEmail(userId);
        model.addAttribute("user", user);

        UtilDTOs.Result result = new UtilDTOs.Result(false, true, "Chỉnh sửa thông tin người dùng thành công");
        model.addAttribute(ClientGlobalVarNames.result, result);

        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);

        return ClientPages.profilePage;
    }
}
