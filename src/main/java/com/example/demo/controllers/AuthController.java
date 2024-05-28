package com.example.demo.controllers;

import com.example.demo.DTOs.auth.CreateAccountDTO;
import com.example.demo.DTOs.auth.LoginRequestDTO;
import com.example.demo.configs.props.AppInfoProps;
import com.example.demo.services.AuthService;
import com.example.demo.services.CookieService;
import com.example.demo.services.JWTService;
import com.example.demo.services.UserService;
import com.example.demo.utils.client.ClientGlobalVarNames;
import com.example.demo.utils.client.ClientPages;
import com.example.demo.utils.exceptions.AuthException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private AppInfoProps appInfoProps;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private CookieService cookieService;

    @GetMapping("login")
    public String loginPage(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
            return "redirect:/account";
        }
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        return ClientPages.loginPage;
    }

    @PostMapping("login")
    public String login(@NonNull @Valid LoginRequestDTO loginRequestDTO, BindingResult bindingResult, Model model,
        @NonNull HttpServletResponse response, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());

        if (principal != null) {
            model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
            return "redirect:/account";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute(ClientGlobalVarNames.error, bindingResult.getAllErrors().get(0).getDefaultMessage());
            return ClientPages.loginPage;
        }

        try {
            authService.login(loginRequestDTO);
        } catch (AuthenticationException exception) {
            model.addAttribute(ClientGlobalVarNames.error,
                "Đăng nhập thất bại, sai tên người dùng hoặc mật khẩu (" + exception.getMessage() + ")");
            return ClientPages.loginPage;
        }

        String jwt = jwtService.generateToken(loginRequestDTO.getEmail());

        cookieService.setJWTCookieAtClient(response, jwt);

        return "redirect:/account";
    }

    @GetMapping("register")
    public String registerPage(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
            return "redirect:/account";
        }
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        return ClientPages.registerPage;
    }

    @PostMapping("register")
    public String createAccount(@NonNull @Valid CreateAccountDTO createAccountDTO, BindingResult bindingResult,
        Model model, @NonNull HttpServletResponse response, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());

        if (principal != null) {
            model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
            return "redirect:/account";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute(ClientGlobalVarNames.error, bindingResult.getAllErrors().get(0).getDefaultMessage());
            model.addAttribute("fullName", createAccountDTO.getFullName());
            model.addAttribute("email", createAccountDTO.getEmail());
            model.addAttribute("gender", createAccountDTO.getGender());
            return ClientPages.registerPage;
        }

        try {
            userService.createAccount(createAccountDTO);
        } catch (AuthException exception) {
            model.addAttribute(ClientGlobalVarNames.error, exception.getMessage());
            model.addAttribute("fullName", createAccountDTO.getFullName());
            model.addAttribute("email", createAccountDTO.getEmail());
            model.addAttribute("gender", createAccountDTO.getGender());
            return ClientPages.registerPage;
        }

        String jwt = jwtService.generateToken(createAccountDTO.getEmail());

        cookieService.setJWTCookieAtClient(response, jwt);

        return "redirect:/account";
    }
}
