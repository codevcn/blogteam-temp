package com.example.demo.DAOs;

import com.example.demo.models.Post;
import com.example.demo.models.joins.PostWithCategory;
import com.example.demo.utils.exceptions.BaseException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class PostDAO {

    private final String tableName = "posts";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int count() {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count == null ? 0 : count;
    }

    public List<Post> findByUserId(String userId) {
        String sql = "SELECT * FROM " + tableName + " WHERE deleted = 0 AND userID = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Post.class), userId);
    }

    public List<Post> findAll() {
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Post.class));
    }

    public List<Post> findByKeyword(String keyword, int offsetPage, int amountOfAPage) {
        String sql = "SELECT * FROM " + tableName + " WHERE deleted = 0 AND (title LIKE ? OR hashtag = ?)"
            + " ORDER BY createdAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String likeSearchTerm = "%" + keyword + "%";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Post.class), likeSearchTerm, keyword,
            offsetPage, amountOfAPage);
    }

    public int create(@NonNull Post post) throws BaseException {
        String sql =
            "INSERT INTO " + tableName + "(title, userID, hashtag, mainContent, isPrivate)" + " VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getUserID());
            ps.setString(3, post.getHashtag());
            ps.setString(4, post.getMainContent());
            ps.setBoolean(5, post.getIsPrivate());
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        if (key == null) {
            throw new BaseException("Can't create new post");
        }
        return key.intValue();
    }

    public Post findById(Long post_id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        List<Post> posts = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Post.class), post_id);
        return posts.size() == 0 ? null : posts.get(0);
    }

    public int deleteById(Long post_id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        return jdbcTemplate.update(sql, post_id);
    }

    public void updateById(Long post_id, Post post) {
        String sql = "UPDATE " + tableName + " SET title = ?" + ", hashtag = ?" + ", mainContent = ?"
            + ", isPrivate = ?" + " WHERE id = ?";
        jdbcTemplate.update(sql, post.getTitle(), post.getHashtag(), post.getMainContent(), post.getIsPrivate(),
            post_id);
    }

    // hashtag
    public void updateCategory(Long post_id, String category) {
        String sql = "UPDATE " + tableName + " SET categoryID = ?" + " WHERE id = ?";
        jdbcTemplate.update(sql, category, post_id);
    }

    public List<PostWithCategory> findPostWithHashtag(String hashtag) {
        String sql = "SELECT [posts].title, [posts].userID, [posts].hashtag"
            + " FROM [posts] INNER JOIN [categories] ON [posts].[categoryID]=[categories].[id]"
            + " WHERE [posts].id = ?";
        List<PostWithCategory> post =
            jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(PostWithCategory.class), hashtag);
        return post;
    }

    public int deleteByCategory(String category) {
        String sql = "DELETE FROM " + tableName + " WHERE categoryID = ?";
        return jdbcTemplate.update(sql, category);
    }
}
