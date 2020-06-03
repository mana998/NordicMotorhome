package com.example.demo.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator{ //Ilias

    // Encodes a word to a hash representation to save in the DB in encoded form
    public static String passGenerator(String word) {
        //Part of Spring Security
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(word);
    }

////   Main method to manually encrypt words to hash
//    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String rawPassword = "word";
//        System.out.println(encoder.encode(rawPassword));
//    }
}

