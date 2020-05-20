package com.example.demo;

import com.example.demo.Model.Agreement;
import com.example.demo.Repository.AgreementRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.w3c.dom.ls.LSOutput;

import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.valueOf;

@SpringBootApplication
public class NordicMotorhomeApplication {

    public static void main(String[] args) {

        double price = 1;
        price *= price * 0.6;
        System.out.println(price);

        //SpringApplication.run(NordicMotorhomeApplication.class, args);
    }

}
