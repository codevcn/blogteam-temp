package com.example.demo.DTOs;

import com.example.demo.models.Post;
import com.example.demo.models.User;
import java.util.List;

public class UtilDTOs {

    public record Success(boolean success) {
    }

    public record Metrics(int totalOfPosts, int totalOfUsers) {
    }

    public record UserOfAPost(String avatar, String fullName) {
    }

    public record MostVisitedPostForViewing(Post post, UserOfAPost user, Long visitsCount) {
    }

    public record Result(boolean error, boolean success, String message) {
    }

    public record PostForSearching(Long id, String title, String createdAt, String background, User user,
        String mainContent, Boolean isPrivate, String hashtag, Boolean deleted, String updateAt, int likesCount,
        int reviewsCount) {
    }

    public record SearchPosts(int pagesCount, List<UtilDTOs.PostForSearching> posts) {
    }

    public record PostsOfUser(Long id, String title, String createdAt, String background, User user, String mainContent,
        Boolean isPrivate, String hashtag, Boolean deleted, String updateAt, int likesCount, int reviewsCount) {
    }

    public record UserOfAReview(String avatar, String fullName) {
    }

    public record ReviewWithUser(Long postID, UserOfAReview user, String comment, String createdAt) {
    }

    public record PostForViewing(Long id, String title, String createdAt, String background, User user,
        String mainContent, Boolean isPrivate, String hashtag, Boolean deleted, String updateAt, int likesCount,
        int reviewsCount, List<ReviewWithUser> reviews, boolean liked, boolean madeReview) {
    }

    public record ReviewWasMade(Long postID, UserOfAReview user, String comment, String createdAt) {
    }
}
