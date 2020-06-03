package com.example.demo.Controller;

import com.example.demo.Model.*;
import com.example.demo.Service.AgreementService;
import com.example.demo.Service.CountryService;
import com.example.demo.Service.RenterService;
import com.example.demo.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
        // passes the current date to the model in order to handle the minimum accepted date in the form
        model.addAttribute("now", LocalDate.now());
        return "addAgreementSelectDates";
    }

    // mapping for posting the two dates and finding available vehicles
    @PostMapping("/create/selectDates")
    public String saveDates(@ModelAttribute Agreement agreement, Model model) {
        // if the end date is before the start date, then switch the dates
        if (agreement.getEndDate().isBefore(agreement.getStartDate())) {
            LocalDate tempDate = agreement.getEndDate();
            agreement.setEndDate(agreement.getStartDate());
            agreement.setStartDate(tempDate);
        }
        model.addAttribute("agreement", agreement);
        List<Vehicle> availableVehicles = vehicleService.findVehiclesAvailableForAgreement(agreement.getStartDate(),
                agreement.getEndDate(), agreement.getVehicle().getBeds(), agreement.getVehicle().getPrice());
        // if there are no available vehicles based on search criteria, show no-results page
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
                                              @PathVariable("vehicleId") int vehicleId) {
        return "addAgreementNewOrExistingRenter";
    }

    // mapping for showing form for adding info about both the new renter and the agreement itself
    @GetMapping("/create/{startDate}/{endDate}/selectVeh*cle/{vehicleId}/new-renter")
    public String showNewRenterForm(@PathVariable("startDate") String startDate,
                                 @PathVariable("endDate") String endDate,
                                 @PathVariable("vehicleId") int vehicleId,
                                 Model model) {
        model.addAttribute("agreement", new Agreement());
        ItemCreation items = new ItemCreation(agreementService.findAllItems());
        model.addAttribute("itemList", items);
        List<String> countries = countryService.showCountriesList();
        model.addAttribute("countries", countries);
        return "addAgreementNewRenter";
    }

    // mapping for posting information about the new renter and the new agreement
    @PostMapping("/create/{startDate}/{endDate}/selectVeh*cle/{vehicleId}/new-renter")
    public String saveNewRenter(@PathVariable("startDate") String startDate,
                                @PathVariable("endDate") String endDate,
                                @PathVariable("vehicleId") int vehicleId,
                                @ModelAttribute ("itemList") ItemCreation itemList,
                                @ModelAttribute Agreement agreement) {
        // saves new renter to the database
        renterService.addRenter(agreement.getRenter());
        // fetches new renter id
        int renterId = renterService.findMaxRenterId();
        // sets new renter id to the new agreement
        agreement.getRenter().setId(renterId);
        // finds vehicle based on vehicle id
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
        // sets vehicle to the new agreement
        agreement.setVehicle(vehicle);
        // converts dates from string to LocalDate type and sets to new agreement
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateConverted = LocalDate.parse(startDate, formatter);
        LocalDate endDateConverted = LocalDate.parse(endDate, formatter);
        agreement.setStartDate(startDateConverted);
        agreement.setEndDate(endDateConverted);
        // saves new agreement
        agreementService.addAgreement(agreement);
        // saves extra items
        agreementService.addItems(agreement,itemList.getItems());
        return "redirect:/agreement/viewAgreements";
    }

    // mapping for showing renters to select, in case the user selects the existing renter option
    @GetMapping("create/{startDate}/{endDate}/selectVeh*cle/{vehicleId}/existing-renter")
    public String listRenters(@PathVariable("startDate") String startDate,
                              @PathVariable("endDate") String endDate,
                              @PathVariable("vehicleId") int vehicleId,
                              Model model, @RequestParam(defaultValue = "") String driverLicenseNumber) {
        List<Renter> renters = renterService.findByDriverLicenseNumber(driverLicenseNumber);
        model.addAttribute("renters", renters);
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
                                   @ModelAttribute Agreement agreement) {
        // converts the dates from string to LocalDate and sets the new agreement dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDateConverted = LocalDate.parse(startDate, formatter);
        LocalDate endDateConverted = LocalDate.parse(endDate, formatter);
        agreement.setStartDate(startDateConverted);
        agreement.setEndDate(endDateConverted);
        // finds the renter from the database based on renter id and associates it with new agreement
        agreement.setRenter(renterService.findRenterById(renterId));
        agreement.setVehicle(vehicleService.findVehicleById(vehicleId));
        // saves new agreement
        agreementService.addAgreement(agreement);
        // gets list of items from wrapper class and inserts them into db
        agreementService.addItems(agreement,itemList.getItems());
        return "redirect:/agreement/viewAgreements";
    }

    // mapping for showing a selected agreement
    @GetMapping("/viewAgreement")
    public String showSingle(@RequestParam("agreementId") int agreementId, Model model) {
        Agreement agreement = agreementService.findById(agreementId);
        setVehicleRenter(agreement);
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
        setVehicleRenter(agreement);
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
        Agreement agreement = agreementService.findById(id);
        setVehicleRenter(agreement);
        // and pass it to the model
        model.addAttribute("agreement", agreement);
        //get all the items associated with the agreement
        ItemCreation items = new ItemCreation(agreementService.findItemsForAgreement(id));
        //add all the other available items in case new ones should be added to an agreement
        items.addDifference(agreementService.findAllItems());
        model.addAttribute("itemList", items);
        return "/updateAgreement";
    }

    @PostMapping("/saveUpdate/{id}")
    public String saveUpdate( @ModelAttribute ("itemList") ItemCreation itemList,
                              @ModelAttribute("agreement") Agreement agreement, @PathVariable("id") int id) {
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
        setVehicleRenter(agreement);
        agreement.setItems(agreementService.findItemsForAgreement(agreement.getId()));
        // and pass it to the model
        model.addAttribute("agreement", agreement);
        // and pass it to the model
        model.addAttribute("now", LocalDate.now());
        model.addAttribute("agreement", agreement);
        return "cancelAgreement";
    }

    @PostMapping("/saveCancel")
    public String saveCancellation(@ModelAttribute("agreement") Agreement agreement, Model model) {
        // updates agreement (sets is_cancelled = '1')
        agreementService.cancelAgreement(agreement.getId());
        // we need to update the attribute here before we pass it into the model
        agreement.setCancelled(true);
        agreement.setItems(agreementService.findItemsForAgreement(agreement.getId()));
        model.addAttribute("agreement", agreement);
        model.addAttribute("now", LocalDate.now());
        LocalDate dueDate = LocalDate.now( )
                .plusMonths( 1 );
        model.addAttribute("dueDate", dueDate);
        return "showInvoice";
    }                                                  

    //method for assigning vehicle and renter to the agreement
    public void setVehicleRenter (Agreement agreement) {
        // find and set the vehicle for this agreement
        int vehicleId = agreement.getVehicle().getVehicleID();
        if (vehicleId != 0) {
            Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
            agreement.setVehicle(vehicle);
        }
        // find and set the renter for this agreement
        int renterId = agreement.getRenter().getId();
        if (renterId != 0) {
            Renter renter = renterService.findRenterById(renterId);
            agreement.setRenter(renter);
        }
    }
}
