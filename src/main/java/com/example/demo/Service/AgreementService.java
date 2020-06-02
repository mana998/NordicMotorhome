package com.example.demo.Service;

import com.example.demo.Model.Agreement;
import com.example.demo.Model.Item;
import com.example.demo.Repository.AgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class AgreementService {

    private AgreementRepository agreementRepository;

    @Autowired
    public AgreementService(AgreementRepository agreementRepository) {
        this.agreementRepository = agreementRepository;
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
