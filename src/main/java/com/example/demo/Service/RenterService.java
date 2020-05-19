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

    public void addRenter(Renter renter) {renterRepository.addRenter(renter);}


<<<<<<< HEAD
    public Renter findRenterById(int id) { return renterRepository.findRenterById(id);}
=======
    public Renter findRenterById(int id) { return renterRepository.getRenter(id);}
>>>>>>> 6bade741a56ad1abe43e6ffead2ab9d09cbf3b42

}