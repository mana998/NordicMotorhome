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
    public Item convert(String id) {
        int parseId = Integer.parseInt(id);
        Item item = agreementRepository.findItemById(parseId);
        return item;
    }
}
