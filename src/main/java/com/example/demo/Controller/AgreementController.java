package com.example.demo.Controller;

import com.example.demo.Model.Agreement;
import com.example.demo.Model.Item;
import com.example.demo.Model.Renter;
import com.example.demo.Model.Vehicle;
import com.example.demo.Service.AgreementService;
import com.example.demo.Service.CountryService;
import com.example.demo.Service.RenterService;
import com.example.demo.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/agreements")
public class AgreementController {

    private AgreementService agreementService;
    private VehicleService vehicleService;
    private RenterService renterService;
    private CountryService countryService;

    @Autowired
    public AgreementController(AgreementService agreementService, VehicleService vehicleService,
                               RenterService renterService, CountryService countryService) {
        this.agreementService = agreementService;
        this.vehicleService = vehicleService;
        this.renterService = renterService;
        this.countryService = countryService;
    }

    // create a mapping for "/list"
    @GetMapping("/viewAgreements")
    public String listAgreements(Model model) {
        List<Agreement> theAgreements = agreementService.findAll();
        model.addAttribute("agreements", theAgreements);
        return "agreements/viewAgreements";
    }

    @GetMapping("/view/{id}")
    public String viewSingleAgreement(@PathVariable ("id") int id) {
        return null;
    }

    @GetMapping("/create/selectDates")
    public String showDateForm(Model theModel) {
        Agreement agreement = new Agreement();
        theModel.addAttribute("agreement", agreement);
        theModel.addAttribute("now", java.time.LocalDate.now());
        return "agreements/addAgreementSelectDates";
    }

    @PostMapping("/create/selectDates")
    public String saveDates(@ModelAttribute Agreement agreement, Model theModel) {

        // if the end date is before the start date, then switch the dates
        if (agreement.getEndDate().isBefore(agreement.getStartDate())) {
            LocalDate tempDate = agreement.getEndDate();
            agreement.setEndDate(agreement.getStartDate());
            agreement.setStartDate(tempDate);
        }

        theModel.addAttribute("startDate", agreement.getStartDate());
        theModel.addAttribute("endDate", agreement.getEndDate());

        List<Vehicle> availableVehicles = vehicleService.showVehicleList(); // add agreement as parameter!!!

        theModel.addAttribute("agreement", agreement);
        theModel.addAttribute("availableVehicles", availableVehicles);
        return "agreements/addAgreementShowAvailableVehicles";
    }

    @GetMapping("/create/{startDate}/{endDate}/selectVehicle/{vehicleId}")
    public String showRenterPage(@PathVariable ("startDate") String startDate,
                                 @PathVariable ("endDate") String endDate,
                                 @PathVariable ("vehicleId") int vehicleId,
                                 Model theModel) {
        theModel.addAttribute("startDate", startDate);
        theModel.addAttribute("endDate", endDate);
        theModel.addAttribute("carId", vehicleId);
        return "agreements/addAgreementNewOrExistingRenter";
    }

    @GetMapping("/create/{startDate}/{endDate}/selectVehicle/{vehicleId}/new-renter")
    public String showRenterForm(@PathVariable ("startDate") String startDate,
                                 @PathVariable ("endDate") String endDate,
                                 @PathVariable ("vehicleId") int vehicleId,
                                 Model theModel) {
        Agreement agreement = new Agreement();
        Renter theRenter = new Renter();
        theModel.addAttribute("renter", theRenter);
        theModel.addAttribute("startDate", startDate);
        theModel.addAttribute("endDate", endDate);
        theModel.addAttribute("vehicleId", vehicleId);
        theModel.addAttribute("agreement", agreement);
        List<Item> itemsList = agreementService.findAllItems();
        theModel.addAttribute("itemsList", itemsList);

        List<String> countries = countryService.showCountriesList();
        theModel.addAttribute("countries", countries);

        // in case we want to add a list of countries for the dropdown?
        //theModel.addAttribute("countryList", countryList.getCountries());
        return "agreements/addAgreementNewRenter";
    }

    @PostMapping("/create/{startDate}/{endDate}/selectVehicle/{vehicleId}/new-renter")
    public String saveNewRenter(@PathVariable ("startDate") String startDate,
                                @PathVariable ("endDate") String endDate,
                                @PathVariable ("vehicleId") int vehicleId,
                                Model theModel,
                                @ModelAttribute Renter renter, @ModelAttribute Agreement agreement) {

        renterService.addRenter(renter);
        int renterId = renterService.findMaxRenterId();
        renter.setId(renterId);
        agreement.setRenter(renter);
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
        agreement.setVehicle(vehicle);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateConverted = LocalDate.parse(startDate, formatter);
        LocalDate endDateConverted = LocalDate.parse(endDate, formatter);
        agreement.setStartDate(startDateConverted);
        agreement.setEndDate(endDateConverted);
        agreementService.addAgreement(agreement);
        agreementService.addItems(agreement);
        theModel.addAttribute(agreement);
        return "agreements/addAgreementShowCharges";
    }

    @GetMapping("create/{startDate}/{endDate}/selectVehicle/{vehicleId}/existing-renter")
    public String listRenters(@PathVariable ("startDate") String startDate,
                              @PathVariable ("endDate") String endDate,
                              @PathVariable ("vehicleId") int vehicleId,
                              Model theModel, @RequestParam (defaultValue = "") String driverLicenseNumber) {
        theModel.addAttribute("startDate", startDate);
        theModel.addAttribute("endDate", endDate);
        theModel.addAttribute("vehicleId", vehicleId);
        List<Renter> renters = renterService.findByDriverLicenseNumber(driverLicenseNumber);
        System.out.println(renters);
        theModel.addAttribute("renters", renters);
        return "agreements/addAgreementExistingRenter";
    }

    @GetMapping("/create/{startDate}/{endDate}/selectVehicle/{vehicleId}/existing-renter/{renterId}")
    public String showAgreementDetails(@PathVariable ("startDate") String startDate,
                                     @PathVariable ("endDate") String endDate,
                                     @PathVariable ("vehicleId") int vehicleId,
                                     @PathVariable ("renterId") int renterId,
                                     Model model) {
        Agreement agreement = new Agreement();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateConverted = LocalDate.parse(startDate, formatter);
        LocalDate endDateConverted = LocalDate.parse(endDate, formatter);
        agreement.setStartDate(startDateConverted);
        agreement.setEndDate(endDateConverted);
        agreement.setRenter(renterService.findRenterById(renterId));
        agreement.setVehicle(vehicleService.findVehicleById(vehicleId));
        model.addAttribute("agreement", agreement);
        agreementService.addAgreement(agreement);
        System.out.println(agreement);
        int agreementId = agreementService.findMaxAgreementId();
        model.addAttribute("agreementId", agreementId);
        List<Item> itemList = agreementService.findAllItems();
        model.addAttribute("itemList", itemList);
        return "agreements/addAgreementAddDetails";
    }

    @PostMapping("/create/addAgreementDetails/{agreementId}")
    public String saveContractInfo(@PathVariable ("agreementId") int agreementId, @ModelAttribute Agreement agreement,
                                   @ModelAttribute ArrayList<Item> itemList, Model model) {
        System.out.println("inside post method!");
        Agreement theAgreement = agreementService.findById(agreementId);
        theAgreement.setPickUpPoint(agreement.getPickUpPoint());
        theAgreement.setDropOffPoint(agreement.getDropOffPoint());
        theAgreement.setItems(agreement.getItems());
        theAgreement.setId(agreementId);
        System.out.println(theAgreement);
        double transferCost = theAgreement.calculateTransferCost();
        double vehicleCost = theAgreement.calculateVehicleCost();
        double itemsCost = theAgreement.calculateItemsCost();
        double totalCost = transferCost + vehicleCost + itemsCost;
        theAgreement.setTotalCost(totalCost);
        agreementService.addItems(theAgreement);
        agreementService.updateAgreement(theAgreement);
        model.addAttribute("agreement", theAgreement);
        return "agreements/addAgreementShowCharges";
    }
}