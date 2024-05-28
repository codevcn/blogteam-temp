package com.example.demo.DAOs;

import com.example.demo.models.Interaction;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class InteractionDAO {

    private final String tableName = "interactions";

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

    public List<Interaction> findAll() {
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Interaction.class));
    }

    public int deleteById(Long post_id, String user_id) {
        String sql = "DELETE FROM " + tableName + " WHERE postID = ? AND userID = ?";
        return jdbcTemplate.update(sql, post_id, user_id);
    }

    public int create(final @NonNull Interaction interaction) {
        String sql = "INSERT INTO " + tableName + "(postID, userID, liked)" + " VALUES (?, ?, ?)";
        return jdbcTemplate.update(sql, interaction.getPostID(), interaction.getUserID(), interaction.isLiked());
    }

    public List<Interaction> findPosts(Long post_id) {
        String sql = "SELECT * FROM " + tableName + " WHERE postID = ?";
        List<Interaction> interactions =
            jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Interaction.class), post_id);
        return interactions;
    }

    public Interaction findOneWithUser(Long post_id, String user_id) {
        String sql = "SELECT * FROM " + tableName + " WHERE postID = ? AND userID = ?";
        List<Interaction> interactions =
            jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Interaction.class), post_id, user_id);
        return interactions.size() == 0 ? null : interactions.get(0);
    }
}
