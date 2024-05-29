package com.example.demo.controllers;

import com.example.demo.DTOs.UtilDTOs;
import com.example.demo.configs.props.AppInfoProps;
import com.example.demo.services.BlogService;
import com.example.demo.services.UserService;
import com.example.demo.utils.client.ClientGlobalVarNames;
import com.example.demo.utils.client.ClientPages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private AppInfoProps appInfoProps;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @GetMapping()
    public String dashboardPage(Model model) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
        int totalOfPosts = blogService.countPosts();
        int totalOfUsers = userService.countUsers();
        UtilDTOs.Metrics metrics = new UtilDTOs.Metrics(totalOfPosts, totalOfUsers);
        model.addAttribute("metrics", metrics);
        return ClientPages.AdminPages.dashboardPage;
    }
}
