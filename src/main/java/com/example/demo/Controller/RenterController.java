package com.example.demo.Controller;

import com.example.demo.Model.Renter;
import com.example.demo.Service.AgreementService;
import com.example.demo.Service.CountryService;
import com.example.demo.Service.RenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RenterController {

    @Autowired
    RenterService renterService;
    @Autowired
    CountryService countryService;
    @Autowired
    AgreementService agreementService;

    //show table of all employees
    @GetMapping("/viewRenters")
    public String renters(Model model){
        List<Renter> renterList = renterService.showRentersList(); //saves renters into the list
        model.addAttribute("renters",renterList); //adds renters list to the model
        return "viewRenters";
    }

    //show add renter form
    @GetMapping("/addRenter")
    public String addMember(Model model){
        model.addAttribute("countries",countryService.showCountriesList());
        return "addRenter";
    }

    //add new renter
    @PostMapping("/addRenter")
    public String addMember(@ModelAttribute Renter renter){
        renterService.addRenter(renter);
        return "redirect:/viewRenters";
    }

    @GetMapping("/showRenter/{id}")
    public String showRenter(@PathVariable("id") int id, Model model){
        model.addAttribute("renter",renterService.findRenterById(id));
        //pass the WHERE clause of query
        //for active agreements
        String sql=" WHERE a.renterID = " + id + " AND a.is_cancelled = 0 AND a.start_date <=  CURRENT_DATE() AND a.end_date >=  CURRENT_DATE() ";
        model.addAttribute("activeAgreements",agreementService.getSpecificAgreements(sql));
        //for future agreements
        sql=" WHERE a.renterID = " + id + " AND (a.is_cancelled IS NULL OR a.is_cancelled = 0) AND a.start_date >  CURRENT_DATE() ";
        model.addAttribute("futureAgreements",agreementService.getSpecificAgreements(sql));
        //for inactive agreements
        sql=" WHERE a.renterID = " + id + "  AND (a.is_cancelled = 1 OR a.end_date < CURRENT_DATE()) ";
        model.addAttribute("inactiveAgreements",agreementService.getSpecificAgreements(sql));
        return "showRenter";
    }

    //show update renter form
    @GetMapping("/updateRenter/{id}")
    public String updateRenter(@PathVariable("id") int id, Model model){
        model.addAttribute("renter",renterService.findRenterById(id));
        model.addAttribute("countries",countryService.showCountriesList());
        return "updateRenter";
    }

    //update renter information
    @PostMapping("/updateRenter/{id}")
    public String updateRenter(@ModelAttribute Renter renter,@PathVariable("id") int id){
        renterService.updateRenter(renter);
        return "redirect:/viewRenters";
    }

    @GetMapping("/deleteRenter/{id}")
    public String deleteRenter(@PathVariable("id") int id, Model model) {
        //if renter was deleted
        if (renterService.deleteRenter(id)) {
            return "redirect:/viewRenters";
        } else { //if renter wasn't deleted because of active/future contracts
            model.addAttribute("failed", true);
            //used forward instead of redirect to trigger javascript onload event
            return "forward:/viewRenters";
        }
    }

}
