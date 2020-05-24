package com.example.demo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class LoginConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    protected void configureAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        //uses jdbc authentication
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                //Encodes the password to hash
                .passwordEncoder(new BCryptPasswordEncoder())
                //finds the users inside our database
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT username, role FROM users WHERE username = ?");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and() //used to chain methods
                //basic login form
                .formLogin().permitAll()
                .and() //used to chain methods
                .logout().permitAll();
    }



}