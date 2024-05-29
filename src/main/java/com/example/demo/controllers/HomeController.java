package com.example.demo.controllers;

import com.example.demo.DTOs.UtilDTOs;
import com.example.demo.configs.props.AppInfoProps;
import com.example.demo.services.BlogService;
import com.example.demo.utils.client.ClientGlobalVarNames;
import com.example.demo.utils.client.ClientPages;
import com.example.demo.utils.constants.Pagination;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private AppInfoProps appInfoService;

    @Autowired
    private BlogService blogService;

    @GetMapping({"/", "/home"})
    public String homePage(Model model, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoService.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, principal != null);
        List<UtilDTOs.MostVisitedPostForViewing> posts =
            blogService.findTopMostVisitedPosts(Pagination.amountOfAPagePagination);
        model.addAttribute("posts", posts);
        return ClientPages.homePage;
    }

    @GetMapping("/contact")
    public String contactPage(Model model, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoService.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, principal != null);
        return ClientPages.contactPage;
    }
}
