package com.example.demo.Controller;

import com.example.demo.Model.Renter;
import com.example.demo.Service.CountryService;
import com.example.demo.Service.RenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class RenterController { //Marianna

    @Autowired
    RenterService renterService;
    @Autowired
    CountryService countryService;

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

}
