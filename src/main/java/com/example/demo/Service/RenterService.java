package com.example.demo.Service;

import com.example.demo.Repository.RenterRepository;
import com.example.demo.Model.Renter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RenterService { //Marianna
    @Autowired
    RenterRepository renterRepository;

    public List<Renter> showRentersList() {
        return renterRepository.showRentersList();
    }

    public Renter getRenter(int id) { return renterRepository.getRenter(id);}

}