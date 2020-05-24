package com.example.demo.Service;

import com.example.demo.Repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService { //Karolina

    @Autowired
    ModelRepository modelRepository;

    public List<String> showModelsList() { return modelRepository.showModelsList();}
}