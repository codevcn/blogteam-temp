package com.example.demo.services;

import com.example.demo.DAOs.InteractionDAO;
import com.example.demo.DAOs.PostDAO;
import com.example.demo.DAOs.ReviewDAO;
import com.example.demo.DAOs.UserDAO;
import com.example.demo.DTOs.UtilDTOs;
import com.example.demo.DTOs.UtilDTOs.ReviewWasMade;
import com.example.demo.DTOs.blog.CreateBlogDTO;
import com.example.demo.DTOs.blog.EditBlogDTO;
import com.example.demo.models.Interaction;
import com.example.demo.models.Post;
import com.example.demo.models.Review;
import com.example.demo.models.User;
import com.example.demo.utils.Helpers;
import com.example.demo.utils.exceptions.BaseException;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private InteractionDAO interactionDAO;

    @Autowired
    private ReviewDAO reviewDAO;

    @Autowired
    private Helpers helpers;

    public Post findPost(Long postId) {
        return postDAO.findById(postId);
    }

    public void deletePost(Long postId) {
        postDAO.deleteById(postId);
    }

    public void editBlog(Long postId, EditBlogDTO editBlogDTO) {
        Post post = new Post();
        post.setTitle(editBlogDTO.getTitle());
        post.setMainContent(editBlogDTO.getMainContent());
        post.setHashtag(editBlogDTO.getHashtag());
        post.setIsPrivate(editBlogDTO.getIsPrivate());
        postDAO.updateById(postId, post);
    }

    public ReviewWasMade makeReview(Long postId, String userId, String comment) {
        Review review = new Review();
        review.setComment(comment);
        review.setPostID(postId);
        review.setUserID(userId);
        reviewDAO.create(review);
        Review newReview = reviewDAO.findOne(postId, userId);
        User user = userDAO.findByEmail(userId);
        ReviewWasMade reviewWasMade = new ReviewWasMade(postId,
            new UtilDTOs.UserOfAReview(user.getAvatar(), user.getFullName()), comment, newReview.getCreatedAt());
        return reviewWasMade;
    }

    public void cancelLikePost(Long postId, String userId) {
        interactionDAO.deleteById(postId, userId);
    }

    public void likePost(Long postId, String userId) {
        Interaction interaction = new Interaction();
        interaction.setLiked(true);
        interaction.setPostID(postId.intValue());
        interaction.setUserID(userId);
        interactionDAO.create(interaction);
    }

    public void createNewBlog(CreateBlogDTO createBlogDTO, Principal principal) throws BaseException, IOException {
        Post post = new Post();

        post.setHashtag(createBlogDTO.getHashtag());
        post.setMainContent(createBlogDTO.getMainContent());
        post.setTitle(createBlogDTO.getTitle());
        post.setUserID(principal.getName());
        post.setIsPrivate(false);

        postDAO.create(post);
    }

    public List<UtilDTOs.PostsOfUser> findPostsOfUser(String userId) {
        List<Post> posts = postDAO.findByUserId(userId);
        List<UtilDTOs.PostsOfUser> searchPostsList = new ArrayList<>();
        for (Post post : posts) {
            User user = userDAO.findByEmail(post.getUserID());
            searchPostsList.add(new UtilDTOs.PostsOfUser(post.getId(), post.getTitle(), post.getCreatedAt(),
                post.getBackground(), user, post.getMainContent(), post.getIsPrivate(), post.getHashtag(),
                post.getDeleted(), post.getUpdateAt(), interactionDAO.countByPostId(post.getId()),
                reviewDAO.countByPostId(post.getId())));
        }
        return searchPostsList;
    }

    public List<UtilDTOs.SearchPosts> searchPosts(String keyword) {
        List<Post> posts = postDAO.findByKeyword(keyword);
        List<UtilDTOs.SearchPosts> searchPostsList = new ArrayList<UtilDTOs.SearchPosts>();
        for (Post post : posts) {
            User user = userDAO.findByEmail(post.getUserID());
            searchPostsList.add(new UtilDTOs.SearchPosts(post.getId(), post.getTitle(), post.getCreatedAt(),
                post.getBackground(), user, post.getMainContent(), post.getIsPrivate(), post.getHashtag(),
                post.getDeleted(), post.getUpdateAt(), interactionDAO.countByPostId(post.getId()),
                reviewDAO.countByPostId(post.getId())));
        }
        return searchPostsList;
    }

    public UtilDTOs.PostForViewing findAPostForViewing(String postId, String userId) {
        Post post = postDAO.findById(Long.parseLong(postId));

        // interaction (like)
        List<Interaction> interactions = interactionDAO.findPosts(Long.parseLong(postId));
        int likesCount = 0;
        boolean liked = false;
        for (Interaction interaction : interactions) {
            if (interaction.isLiked()) {
                likesCount++;
            }
            if (userId != null && interaction.getUserID().equals(userId)) {
                liked = true;
            }
        }

        // made review
        boolean madeReview = false;

        // reviews
        List<Review> reviews = reviewDAO.findPosts(Long.parseLong(postId));
        List<UtilDTOs.ReviewWithUser> reviewWithUsers = new ArrayList<>();
        String[] datePatterns = {"yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss.SS", "yyyy-MM-dd HH:mm:ss.S"};
        for (Review review : reviews) {
            User user = userDAO.findByEmail(review.getUserID());
            if (user.getEmail().equals(userId)) {
                madeReview = true;
            }
            String createdAt = helpers.formatDateString(datePatterns, review.getCreatedAt(), "dd/MM/yyyy HH:mm");
            reviewWithUsers.add(new UtilDTOs.ReviewWithUser(review.getPostID(),
                new UtilDTOs.UserOfAReview(user.getAvatar(), user.getFullName()), review.getComment(), createdAt));
        }

        // user info
        User user = userDAO.findByEmail(post.getUserID());
        String createdAt = helpers.formatDateString(datePatterns, post.getCreatedAt(), "dd/MM/yyyy HH:mm");
        String updateAt = helpers.formatDateString(datePatterns, post.getUpdateAt(), "dd/MM/yyyy HH:mm");

        // final data
        UtilDTOs.PostForViewing postForViewing = new UtilDTOs.PostForViewing(post.getId(), post.getTitle(), createdAt,
            post.getBackground(), user, post.getMainContent(), post.getIsPrivate(), post.getHashtag(),
            post.getDeleted(), updateAt, likesCount, reviews.size(), reviewWithUsers, liked, madeReview);

        return postForViewing;
    }
}
