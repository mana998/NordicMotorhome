package com.example.demo.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BrandRepository { //Karolina

    @Autowired
    JdbcTemplate template;

    public List<String> showBrandsList() {
        String sql = "SELECT brand_name FROM brand";
        RowMapper<String> rowMapper = new SingleColumnRowMapper<>(String.class);
        return template.query(sql, rowMapper);
    }
}