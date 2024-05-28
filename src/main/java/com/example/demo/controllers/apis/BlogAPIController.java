package com.example.demo.controllers.apis;

import com.example.demo.DTOs.UtilDTOs;
import com.example.demo.DTOs.UtilDTOs.ReviewWasMade;
import com.example.demo.DTOs.UtilDTOs.Success;
import com.example.demo.DTOs.blog.CreateBlogDTO;
import com.example.demo.DTOs.blog.EditBlogDTO;
import com.example.demo.DTOs.blog.MakeReviewDTO;
import com.example.demo.services.BlogService;
import com.example.demo.utils.exceptions.BaseException;
import jakarta.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("api/blog")
public class BlogAPIController {

    @Autowired
    private BlogService blogService;

    @PostMapping("create")
    public ResponseEntity<Success> createBlog(@Valid @RequestBody CreateBlogDTO createBlogDTO, Principal principal)
        throws BaseException, IOException {
        blogService.createNewBlog(createBlogDTO, principal);
        return ResponseEntity.status(200).body(new Success(true));
    }

    @GetMapping("search")
    public ResponseEntity<List<UtilDTOs.SearchPosts>> searchBlog(
        @RequestParam(name = "title", defaultValue = "") String title) {
        List<UtilDTOs.SearchPosts> posts = blogService.searchPosts(title);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("like-post/{postId}")
    public ResponseEntity<UtilDTOs.Success> likePost(@PathVariable("postId") String postId, Principal principal) {
        blogService.likePost(Long.parseLong(postId), principal.getName());
        return ResponseEntity.ok(new UtilDTOs.Success(true));
    }

    @DeleteMapping("cancel-like-post/{postId}")
    public ResponseEntity<UtilDTOs.Success> cancelLikePost(@PathVariable("postId") String postId, Principal principal) {
        blogService.cancelLikePost(Long.parseLong(postId), principal.getName());
        return ResponseEntity.ok(new UtilDTOs.Success(true));
    }

    @PostMapping("make-review/{postId}")
    public ResponseEntity<ReviewWasMade> makeReview(@PathVariable("postId") String postId, Principal principal,
        @NonNull @Valid @RequestBody MakeReviewDTO makeReviewDTO) {
        ReviewWasMade review =
            blogService.makeReview(Long.parseLong(postId), principal.getName(), makeReviewDTO.getComment());
        return ResponseEntity.ok(review);
    }

    @PostMapping("edit-blog/{postId}")
    public ResponseEntity<UtilDTOs.Success> editBlog(@PathVariable("postId") String postId, Principal principal,
        @NonNull @Valid @RequestBody EditBlogDTO editBlogDTO) {
        blogService.editBlog(Long.parseLong(postId), editBlogDTO);
        return ResponseEntity.ok(new UtilDTOs.Success(true));
    }

    @DeleteMapping("delete-blog/{postId}")
    public ResponseEntity<UtilDTOs.Success> deleteBlog(@PathVariable("postId") String postId, Principal principal) {
        blogService.deletePost(Long.parseLong(postId));
        return ResponseEntity.ok(new UtilDTOs.Success(true));
    }
}
