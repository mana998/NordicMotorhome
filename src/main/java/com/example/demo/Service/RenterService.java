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

    public Renter findRenterById(int id) { return renterRepository.findRenterById(id);}

    public void updateRenter(Renter renter) {renterRepository.updateRenter(renter);}

    public boolean deleteRenter(int id) { return renterRepository.deleteRenter(id);}

    // Dimitrios
    public List<Renter> findByDriverLicenseNumber(String driverLicenseNumber) {
        return renterRepository.findByDriverLicenseNumber(driverLicenseNumber);
    }

    // Dimitrios
    public int findMaxRenterId() {
        return renterRepository.findMaxRenterId();
    }
}
