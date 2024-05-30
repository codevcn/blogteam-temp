package com.example.demo.services;

import com.example.demo.DAOs.InteractionDAO;
import com.example.demo.DAOs.PostDAO;
import com.example.demo.DAOs.ReviewDAO;
import com.example.demo.DAOs.UserDAO;
import com.example.demo.DAOs.VisitDAO;
import com.example.demo.DTOs.UtilDTOs;
import com.example.demo.DTOs.UtilDTOs.ReviewWasMade;
import com.example.demo.DTOs.blog.CreateBlogDTO;
import com.example.demo.DTOs.blog.EditBlogDTO;
import com.example.demo.models.Interaction;
import com.example.demo.models.Post;
import com.example.demo.models.Review;
import com.example.demo.models.User;
import com.example.demo.models.advance.MostVisitedPost;
import com.example.demo.utils.Helpers;
import com.example.demo.utils.constants.Pagination;
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

    @Autowired
    private VisitDAO visitDAO;

    public int countPosts() {
        return postDAO.count();
    }

    public void visitPost(Long postId) {
        visitDAO.create(postId);
    }

    public Post findPost(Long postId) throws BaseException {
        Post post = postDAO.findById(postId);
        if (post == null) {
            throw new BaseException("Không tìm thấy bài đăng");
        }
        return post;
    }

    public void deletePost(Long postId) {
        postDAO.deleteById(postId);
    }

    public List<UtilDTOs.MostVisitedPostForViewing> findTopMostVisitedPosts(int amount) {
        List<MostVisitedPost> mostVisitedPosts = visitDAO.findTopMostVisitedPosts(amount);
        List<UtilDTOs.MostVisitedPostForViewing> posts = new ArrayList<>();
        for (MostVisitedPost mostVisitedPost : mostVisitedPosts) {
            Post post = postDAO.findById(mostVisitedPost.getPostID());
            if (post != null) {
                String[] datePatterns = {"yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss.SS", "yyyy-MM-dd HH:mm:ss.S"};
                String createdAt = helpers.formatDateString(datePatterns, post.getCreatedAt(), "dd/MM/yyyy HH:mm");
                post.setCreatedAt(createdAt);
                User user = userDAO.findByEmail(post.getUserID());
                posts.add(new UtilDTOs.MostVisitedPostForViewing(post,
                    new UtilDTOs.UserOfAPost(user.getAvatar(), user.getFullName()), mostVisitedPost.getVisitsCount()));
            }
        }
        return posts;
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
        interaction.setPostID(postId);
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

    public UtilDTOs.SearchPosts searchPosts(String keyword, int page, int amountOfAPage) {
        int offsetPage = (page - 1) * amountOfAPage;
        List<Post> posts = postDAO.findByKeyword(keyword, offsetPage, amountOfAPage);
        List<UtilDTOs.PostForSearching> searchPostsList = new ArrayList<>();
        for (Post post : posts) {
            User user = userDAO.findByEmail(post.getUserID());
            searchPostsList.add(new UtilDTOs.PostForSearching(post.getId(), post.getTitle(), post.getCreatedAt(),
                post.getBackground(), user, post.getMainContent(), post.getIsPrivate(), post.getHashtag(),
                post.getDeleted(), post.getUpdateAt(), interactionDAO.countByPostId(post.getId()),
                reviewDAO.countByPostId(post.getId())));
        }
        int totalOfPosts = postDAO.count();
        int pagesCount = 0;
        int pagesCountCaculation = totalOfPosts / Pagination.amountOfAPagePagination;
        if (totalOfPosts % Pagination.amountOfAPagePagination == 0) {
            pagesCount = pagesCountCaculation;
        } else {
            pagesCount = pagesCountCaculation + 1;
        }
        return new UtilDTOs.SearchPosts(pagesCount, searchPostsList);
    }

    public UtilDTOs.PostForViewing findAPostForViewing(String postId, String userId) throws BaseException {
        Post post = postDAO.findById(Long.parseLong(postId));
        if (post == null) {
            throw new BaseException("Không tìm thấy bài đăng");
        }

        // interaction (like)
        List<Interaction> interactions = interactionDAO.findPosts(Long.parseLong(postId));
        int likesCount = 0;
        boolean liked = false;
        for (Interaction interaction : interactions) {
            if (interaction.getLiked()) {
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
