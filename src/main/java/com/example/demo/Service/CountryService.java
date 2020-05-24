package com.example.demo.Service;

import com.example.demo.Repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService { //Marianna

    @Autowired
    CountryRepository countryRepository;

    public List<String> showCountriesList() { return countryRepository.showCountriesList();}
}
