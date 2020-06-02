package com.example.demo.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepository { //Marianna

    @Autowired
    JdbcTemplate template;

    //return list of country names from database
    public List<String> showCountriesList() {
        String sql = "SELECT name FROM country";
        RowMapper<String> rowMapper = new SingleColumnRowMapper<>(String.class);
        return template.query(sql, rowMapper);
    }
}