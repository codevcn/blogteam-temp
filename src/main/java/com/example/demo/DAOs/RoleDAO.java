package com.example.demo.DAOs;

import com.example.demo.models.Role;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAO {

    private final String tableName = "roles";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int count() {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        Integer count = jdbcTemplate.queryForObject(
            sql, Integer.class
        );
        return count == null ? 0 : count;
    }

    public List<Role> findAll() {
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.query(
            sql, BeanPropertyRowMapper.newInstance(
                Role.class
            )
        );
    }

    public int create(final @NonNull Role role) {
        String sql = "INSERT INTO " + tableName + "(id, [description])" + " VALUES (?, ?)";
        return jdbcTemplate.update(
            sql, role.getId(), role.getDescription()
        );
    }

    public Role findById(String category_id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";
        List<Role> categories = jdbcTemplate.query(
            sql, BeanPropertyRowMapper.newInstance(
                Role.class
            ), category_id
        );
        return categories.size() == 0 ? null
            : categories.get(
                0
            );
    }

    public int deleteById(String category_id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        return jdbcTemplate.update(
            sql, category_id
        );
    }
}
