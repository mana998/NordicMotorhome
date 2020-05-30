package com.example.demo.Model;

import com.example.demo.Repository.AgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ItemConverter implements Converter<String, Item> {

    @Autowired
    private AgreementRepository agreementRepository;

    @Override
    public Item convert(String item) {
        //retrieve return from toString method
        //split it into array
        String[] split = item.split(",");
        //create new object based on the array elements
        return new Item(Integer.parseInt(split[1]),split[2],Integer.parseInt(split[3]),Integer.parseInt(split[4]));
    }
}
