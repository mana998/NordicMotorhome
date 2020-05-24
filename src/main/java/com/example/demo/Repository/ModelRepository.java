package com.example.demo.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ModelRepository { //Karolina

    @Autowired
    JdbcTemplate template;

    public List<String> showModelsList() {
        String sql = "SELECT model_name FROM model";
        RowMapper<String> rowMapper = new SingleColumnRowMapper<>(String.class);
        return template.query(sql, rowMapper);
    }
}