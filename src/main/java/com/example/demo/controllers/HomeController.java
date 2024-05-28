package com.example.demo.controllers;

import com.example.demo.configs.props.AppInfoProps;
import com.example.demo.utils.client.ClientGlobalVarNames;
import com.example.demo.utils.client.ClientPages;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private AppInfoProps appInfoService;

    @GetMapping({"/", "/home"})
    public String homePage(Model model, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoService.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, principal != null);
        return ClientPages.homePage;
    }

    @GetMapping("/contact")
    public String contactPage(Model model, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoService.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, principal != null);
        return ClientPages.contactPage;
    }
}
