package com.example.demo.Service;

import com.example.demo.Repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService { //Karolina

    @Autowired
    BrandRepository brandRepository;

    public List<String> showBrandsList() { return brandRepository.showBrandsList();}
}