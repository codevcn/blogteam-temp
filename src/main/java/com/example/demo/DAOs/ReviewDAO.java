package com.example.demo.DAOs;

import com.example.demo.models.Review;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class ReviewDAO {

    private final String tableName = "reviews";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int count() {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count == null ? 0 : count;
    }

    public int countByPostId(Long postId) {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE postID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId);
        return count == null ? 0 : count;
    }

    public List<Review> findAll() {
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Review.class));
    }

    public int create(final @NonNull Review review) {
        String sql = "INSERT INTO " + tableName + "(postID, userID, comment)" + " VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, review.getPostID(), review.getUserID(), review.getComment());
    }

    public Review findOne(Long post_id, String user_id) {
        String sql = "SELECT * FROM " + tableName + " WHERE postID = ? AND userID = ?";
        List<Review> reviews =
            jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Review.class), post_id, user_id);
        return reviews.size() == 0 ? null : reviews.get(0);
    }

    public int deleteById(Long post_id, String user_id) {
        String sql = "DELETE FROM " + tableName + " WHERE postID = ? AND userID = ?";
        return jdbcTemplate.update(sql, post_id, user_id);
    }

    public List<Review> findPosts(Long post_id) {
        String sql = "SELECT * FROM " + tableName + " WHERE postID = ? AND deleted = 0";
        List<Review> interactions = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Review.class), post_id);
        return interactions;
    }
}
