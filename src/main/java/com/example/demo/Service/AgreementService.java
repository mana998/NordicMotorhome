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
import java.time.LocalDate;
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
        return agreementRepository.findAll();
    }

    public List<Item> findAllItems() {
        return agreementRepository.findAllItems();
    }

    public void addAgreement(Agreement agreement) {
        agreementRepository.addAgreement(agreement);
    }

    public void addItems(Agreement agreement, List<Item> itemList) {
        //gets the id of the latest agreement (one that should be assigned with items)
        int agreementId = agreementRepository.findMaxAgreementId();
        agreementRepository.addItems(agreementId, itemList);
    }

    public void updateItems(Agreement agreement, List<Item> itemList) {
        //gets the id of the current agreement (one that should be assigned with items)
        agreementRepository.updateItems(agreement.getId(), itemList);
    }

    public Agreement findById(int agreementId) {
        // find agreement by the agreement id
        Agreement agreement = agreementRepository.findById(agreementId);
        // find and set the vehicle for this agreement
        int vehicleId = agreement.getVehicle().getVehicleID();
        if (vehicleId != 0) {
            Vehicle vehicle = vehicleRepository.findVehicleById(vehicleId);
            agreement.setVehicle(vehicle);
        }
        // find and set the renter for this agreement
        int renterId = agreement.getRenter().getId();
        if (renterId != 0) {
            Renter renter = renterRepository.findRenterById(renterId);
            agreement.setRenter(renter);
        }
        return agreement;
    }

    public List<Item> findItemsForAgreement(int agreementId) {
        return agreementRepository.findItemsForAgreement(agreementId);
    }

    public void updateAgreement(Agreement theAgreement) {
        agreementRepository.updateAgreement(theAgreement);
    }

    public void endAgreement(Agreement agreement) {
        agreementRepository.generateInvoice(agreement);
    }

    public void cancelAgreement(int id) {
        agreementRepository.cancelAgreement(id);
    }

    public List<Agreement> getSpecificAgreements(String addition) {
        return agreementRepository.getSpecificAgreements(addition);
    }

    public List<Agreement> findByEndDate(LocalDate endDate) {
        return agreementRepository.findByEndDate(endDate);
    }
}
