package com.example.demo.controllers;

import com.example.demo.DTOs.UtilDTOs;
import com.example.demo.configs.props.AppInfoProps;
import com.example.demo.models.Post;
import com.example.demo.services.BlogService;
import com.example.demo.utils.client.ClientGlobalVarNames;
import com.example.demo.utils.client.ClientPages;
import com.example.demo.utils.exceptions.BaseException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private AppInfoProps appInfoProps;

    @Autowired
    private BlogService blogService;

    @GetMapping("create")
    public String createBlogPostPage(Model model, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
        return ClientPages.createBlogPage;
    }

    @GetMapping("search")
    public String searchBlogPage(Model model, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, principal != null);
        return ClientPages.searchBlogPage;
    }

    @GetMapping("view-blog/{postId}")
    public String viewBlogPage(Model model, Principal principal, @PathVariable("postId") String postId)
        throws BaseException {
        UtilDTOs.PostForViewing blog =
            blogService.findAPostForViewing(postId, principal == null ? null : principal.getName());
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, principal != null);
        model.addAttribute("blog", blog);
        return ClientPages.viewBlogPage;
    }

    @GetMapping("my-posts")
    public String postssOfUserPage(Model model, Principal principal) {
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
        List<UtilDTOs.PostsOfUser> posts = blogService.findPostsOfUser(principal.getName());
        model.addAttribute("posts", posts);
        model.addAttribute("totalAmount", posts.size());
        return ClientPages.postsOfUserPage;
    }

    @GetMapping("edit-blog/{postId}")
    public String editBlogPage(Model model, Principal principal, @PathVariable("postId") String postId)
        throws BaseException {
        Post post = blogService.findPost(Long.parseLong(postId));
        model.addAttribute(ClientGlobalVarNames.appName, appInfoProps.getAppName());
        model.addAttribute(ClientGlobalVarNames.isAuthenticated, true);
        model.addAttribute("post", post);
        return ClientPages.editBlogPage;
    }
}
