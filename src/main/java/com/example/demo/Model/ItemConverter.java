package com.example.demo.Model;

import com.example.demo.Repository.AgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemConverter implements Converter<String, Item> {

    @Autowired
    private AgreementRepository agreementRepository;

    @Override
    public Item convert(String id) {
        System.out.println("Trying to convert " + id + " into to item.");
        int parseId = Integer.parseInt(id);
        Item item = agreementRepository.findItemById(parseId);
        System.out.println(item);
        return item;
    }
}
