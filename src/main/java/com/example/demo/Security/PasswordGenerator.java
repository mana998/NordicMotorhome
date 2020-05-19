package com.example.demo.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static String passGenerator(String word) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(word);
    }

//   Main method to manually encrypt words to hash
//    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String rawPassword = "Word";
//        System.out.println(encoder.encode(rawPassword));
//    }


}
