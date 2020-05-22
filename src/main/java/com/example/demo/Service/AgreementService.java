package com.example.demo.Service;

import com.example.demo.Model.Agreement;
import com.example.demo.Model.Item;
import com.example.demo.Model.Renter;
import com.example.demo.Model.Vehicle;
import com.example.demo.Repository.AgreementRepository;
import com.example.demo.Repository.RenterRepository;
import com.example.demo.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgreementService {

    private AgreementRepository agreementRepository;
    private VehicleRepository vehicleRepository;
    private RenterRepository renterRepository;

    @Autowired
    public AgreementService(AgreementRepository agreementRepository, VehicleRepository vehicleRepository, RenterRepository renterRepository) {
        this.agreementRepository = agreementRepository;
        this.vehicleRepository = vehicleRepository;
        this.renterRepository = renterRepository;
    }

    public List<Agreement> findAll() {
        List<Agreement> agreementList=agreementRepository.findAll();
        for (Agreement agreement:agreementList) {
            Renter renter = renterRepository.findRenterById(agreement.getRenter().getId());
            agreement.setRenter(renter);
            Vehicle vehicle = vehicleRepository.findVehicleById(agreement.getVehicle().getVehicleID());
            agreement.setVehicle(vehicle);
            List<Item> itemList=agreementRepository.getAllLineItems(agreement.getId());
            agreement.setItems(itemList);
        }
        return agreementList;
    }

    public List<Item> findAllItems() {
        return agreementRepository.findAllItems();
    }


    public void addAgreement(Agreement agreement) {
        agreementRepository.addAgreement(agreement);
    }

    public void addItems(Agreement agreement) {
        int agreementId = findMaxAgreementId();
        for (Item item : agreement.getItems()) {
            agreementRepository.addItems(agreementId, item.getId());
        }
    }

    public int findMaxAgreementId() {
        return agreementRepository.findMaxAgreementId();
    }

    public Agreement findById(int agreementId) {
        return agreementRepository.findById(agreementId);
    }

    public void updateAgreement(Agreement theAgreement) {
        agreementRepository.updateAgreement(theAgreement);
    }
}
