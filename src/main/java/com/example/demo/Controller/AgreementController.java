package com.example.demo.Controller;

import com.example.demo.Model.*;
import com.example.demo.Service.AgreementService;
import com.example.demo.Service.CountryService;
import com.example.demo.Service.RenterService;
import com.example.demo.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/agreement")
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
        // find all agreements
        List<Agreement> agreements = agreementService.findAll();
        // and pass it in the model
        model.addAttribute("agreements", agreements);
        return "viewAgreements";
    }

    // mapping for showing page for selecting the dates of the agreement
    @GetMapping("/create/selectDates")
    public String showDateForm(Model model) {
        // passes agreement object to handle date input for starting and ending date
        Agreement agreement = new Agreement();
        model.addAttribute("agreement", agreement);
        // passes vehicle object to handle input for number of beds and vehicle price
        Vehicle vehicle = new Vehicle();
        model.addAttribute(vehicle);
        // passes the current date to the model in order to handle the minimum accepted date in the form
        model.addAttribute("now", LocalDate.now());
        return "addAgreementSelectDates";
    }

    // mapping for posting the two dates and finding available vehicles
    @PostMapping("/create/selectDates")
    public String saveDates(@ModelAttribute Agreement agreement, @ModelAttribute Vehicle vehicle, Model model) {
        // if the end date is before the start date, then switch the dates
        if (agreement.getEndDate().isBefore(agreement.getStartDate())) {
            LocalDate tempDate = agreement.getEndDate();
            agreement.setEndDate(agreement.getStartDate());
            agreement.setStartDate(tempDate);
        }
        model.addAttribute("startDate", agreement.getStartDate());
        model.addAttribute("endDate", agreement.getEndDate());
        List<Vehicle> availableVehicles = vehicleService.findVehiclesAvailableForAgreement(agreement.getStartDate(),
                agreement.getEndDate(), vehicle.getBeds(), vehicle.getPrice());
        // if there are no available vehicles based on search criteria, show noresults page
        if (availableVehicles.isEmpty()) {
            return "noresults";
        } else {
            // else, show the available vehicles
            model.addAttribute("availableVehicles", availableVehicles);
            return "addAgreementShowAvailableVehicles";
        }
    }


    // mapping for showing page where the user chooses between new or existing renter
    @GetMapping("/create/{startDate}/{endDate}/selectVeh*cle/{vehicleId}")
    public String showPageNewOrExistingRenter(@PathVariable("startDate") String startDate,
                                              @PathVariable("endDate") String endDate,
                                              @PathVariable("vehicleId") int vehicleId,
                                              Model theModel) {
        theModel.addAttribute("startDate", startDate);
        theModel.addAttribute("endDate", endDate);
        theModel.addAttribute("carId", vehicleId);
        return "addAgreementNewOrExistingRenter";
    }

    // mapping for showing form for adding info about both the new renter and the agreement itself
    @GetMapping("/create/{startDate}/{endDate}/selectVeh*cle/{vehicleId}/new-renter")
    public String showNewRenterForm(@PathVariable("startDate") String startDate,
                                 @PathVariable("endDate") String endDate,
                                 @PathVariable("vehicleId") int vehicleId,
                                 Model theModel) {
        Agreement agreement = new Agreement();
        Renter theRenter = new Renter();
        theModel.addAttribute("renter", theRenter);
        theModel.addAttribute("startDate", startDate);
        theModel.addAttribute("endDate", endDate);
        theModel.addAttribute("vehicleId", vehicleId);
        theModel.addAttribute("agreement", agreement);
        ItemCreation items = new ItemCreation(agreementService.findAllItems());
        theModel.addAttribute("itemList", items);
        List<String> countries = countryService.showCountriesList();
        theModel.addAttribute("countries", countries);
        // in case we want to add a list of countries for the dropdown?
        //theModel.addAttribute("countryList", countryList.getCountries());
        return "addAgreementNewRenter";
    }

    // mapping for posting information about the new renter and the new agreement
    @PostMapping("/create/{startDate}/{endDate}/selectVeh*cle/{vehicleId}/new-renter")
    public String saveNewRenter(@PathVariable("startDate") String startDate,
                                @PathVariable("endDate") String endDate,
                                @PathVariable("vehicleId") int vehicleId,
                                Model theModel,
                                @ModelAttribute ("itemList") ItemCreation itemList,
                                @ModelAttribute Renter renter, @ModelAttribute Agreement agreement) {
        // adds renter to the database
        renterService.addRenter(renter);
        // fetches the new renter id
        int renterId = renterService.findMaxRenterId();
        // sets it to the new renter
        renter.setId(renterId);
        // and sets the new renter to the new agreement
        agreement.setRenter(renter);
        // finds vehicle based on vehicle id
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
        // and sets it to the new agreement
        agreement.setVehicle(vehicle);
        // converts dates from string to LocalDate type and sets new agreement values
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateConverted = LocalDate.parse(startDate, formatter);
        LocalDate endDateConverted = LocalDate.parse(endDate, formatter);
        agreement.setStartDate(startDateConverted);
        agreement.setEndDate(endDateConverted);
        // stores new agreement to the database
        agreementService.addAgreement(agreement);
        // stores items associated with new agreement to the database
        agreementService.addItems(agreement,itemList.getItems());
        return "redirect:/agreement/viewAgreements";
    }

    // mapping for showing renters to select, in case the user selects the existing renter option
    @GetMapping("create/{startDate}/{endDate}/selectVeh*cle/{vehicleId}/existing-renter")
    public String listRenters(@PathVariable("startDate") String startDate,
                              @PathVariable("endDate") String endDate,
                              @PathVariable("vehicleId") int vehicleId,
                              Model theModel, @RequestParam(defaultValue = "") String driverLicenseNumber) {
        theModel.addAttribute("startDate", startDate);
        theModel.addAttribute("endDate", endDate);
        theModel.addAttribute("vehicleId", vehicleId);
        List<Renter> renters = renterService.findByDriverLicenseNumber(driverLicenseNumber);
        theModel.addAttribute("renters", renters);
        return "addAgreementExistingRenter";
    }

    // mapping for showing form for adding agreement info if the user selects existing renter
    @GetMapping("/create/{startDate}/{endDate}/selectVeh*cle/{vehicleId}/existing-renter/{renterId}")
    public String showFormExistingRenter(@PathVariable("startDate") String startDate,
                                       @PathVariable("endDate") String endDate,
                                       @PathVariable("vehicleId") int vehicleId,
                                       @PathVariable("renterId") int renterId,
                                       Model model) {
        Agreement agreement = new Agreement();
        model.addAttribute("agreement", agreement);
        // finding all items from the database
        ItemCreation items = new ItemCreation(agreementService.findAllItems());
        model.addAttribute("itemList", items);
        return "addAgreementAddDetails";
    }

    // mapping for posting info about the new agreement if the user selects existing renter
    @PostMapping("/create/{startDate}/{endDate}/selectVeh*cle/{vehicleId}/existing-renter/{renterId}")
    public String saveExistingRenter(@PathVariable("startDate") String startDate,
                                   @PathVariable("endDate") String endDate,
                                   @PathVariable("vehicleId") int vehicleId,
                                   @PathVariable("renterId") int renterId,
                                   @ModelAttribute ("itemList") ItemCreation itemList,
                                   @ModelAttribute Agreement agreement,
                                   Model model) {
        // converts the dates from string to LocalDate and sets the new agreement dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateConverted = LocalDate.parse(startDate, formatter);
        LocalDate endDateConverted = LocalDate.parse(endDate, formatter);
        agreement.setStartDate(startDateConverted);
        agreement.setEndDate(endDateConverted);
        // finds the renter from the database based on renter id and associates it with new agreement
        agreement.setRenter(renterService.findRenterById(renterId));
        agreement.setVehicle(vehicleService.findVehicleById(vehicleId));
        agreementService.addAgreement(agreement);
        //gets list of items from wrapper class and inserts them into db
        agreementService.addItems(agreement,itemList.getItems());
        return "redirect:/agreement/viewAgreements";
    }

    // mapping for showing a selected agreement
    @GetMapping("/viewAgreement")
    public String showSingle(@RequestParam("agreementId") int agreementId, Model model) {
        Agreement agreement = agreementService.findById(agreementId);
        // set the item list associated with this agreement
        List<Item> itemList = agreementService.findItemsForAgreement(agreement.getId());
        agreement.setItems(itemList);
        model.addAttribute("agreement", agreement);
        return "showAgreement";
    }

    // mapping for showing agreements to select which one to end
    @GetMapping("/showAgreementListForEnd")
    public String showListForGenerateInvoice(Model model, @RequestParam(defaultValue = "")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Agreement> agreements = agreementService.findByEndDate(endDate);
        model.addAttribute("agreements", agreements);
        model.addAttribute("now", LocalDate.now());
        return "endAgreement";
    }

    // mapping for showing agreements to select which one to end
    @GetMapping("/showFormForUpdate")
    public String showFormForInvoice(@RequestParam ("agreementId") int agreementId, Model model) {
        // get the agreement from the db
        Agreement agreement = agreementService.findById(agreementId);
        // set agreement as a model attribute to pre-populate the form
        model.addAttribute("agreement", agreement);
        // send over to the form
        return "endAgreementAddDetails";
    }

    @PostMapping("/save")
    public String saveInvoice(@ModelAttribute("agreement") Agreement agreement, Model model) {
        // set the item list associated with this agreement
        List<Item> itemList = agreementService.findItemsForAgreement(agreement.getId());
        agreement.setItems(itemList);
        // and save the agreement
        agreementService.endAgreement(agreement);
        model.addAttribute(agreement);
        model.addAttribute("now", LocalDate.now());
        LocalDate dueDate = LocalDate.now( )
                .plusMonths( 1 );
        model.addAttribute("dueDate", dueDate);
        return "showInvoice";
    }

    // mapping for showing agreement form for update
    @GetMapping("/updateAgreement/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        // get the agreement from the db
        // and pass it to the model
        model.addAttribute("agreement", agreementService.findById(id));
        //get all the items associated with the agreement
        ItemCreation items = new ItemCreation(agreementService.findItemsForAgreement(id));
        //add all the other available items in case new ones should be added to an agreement
        items.addDifference(agreementService.findAllItems());
        model.addAttribute("itemList", items);
        return "/updateAgreement";
    }

    @PostMapping("/saveUpdate/{id}")
    public String saveUpdate( @ModelAttribute ("itemList") ItemCreation itemList,@ModelAttribute("agreement") Agreement agreement, @PathVariable("id") int id) {
        // and save the agreement
        agreement.setId(id);
        agreementService.updateAgreement(agreement);
        //update item list
        agreementService.updateItems(agreement,itemList.getItems());
        return "redirect:/agreement/viewAgreements";
    }

    @GetMapping("/cancelAgreement")
    public String showCancellationFee(@RequestParam ("agreementId") int agreementId, Model model) {
        // get the agreement from the db
        Agreement agreement = agreementService.findById(agreementId);
        //get all items for the agreement
        agreement.setItems(agreementService.findItemsForAgreement(agreement.getId()));
        // and pass it to the model
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("agreement", agreement);
        return "cancelAgreement";
    }

    @PostMapping("/saveCancel")
    public String saveCancellation(@ModelAttribute("agreement") Agreement agreement) {
        agreementService.cancelAgreement(agreement.getId());
        return "redirect:/agreement/viewAgreements";
    }

    /*@GetMapping("/showInvoice")
    public String showInvoice(@RequestParam("agreementId") int agreementId, Model model) {
        Agreement agreement = agreementService.findById(agreementId);
        // set the item list associated with this agreement
        List<Item> itemList = agreementService.findItemsForAgreement(agreement.getId());
        agreement.setItems(itemList);
        model.addAttribute("agreement", agreement);
        model.addAttribute("now", LocalDate.now());
        LocalDate dueDate = LocalDate.now( )
                .plusMonths( 1 );
        model.addAttribute("dueDate", dueDate);
        return "showInvoice";
    }*/
}
