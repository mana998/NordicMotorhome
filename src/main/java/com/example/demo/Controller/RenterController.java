package com.example.demo.Controller;

import com.example.demo.Model.Renter;
import com.example.demo.Service.RenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RenterController { //Marianna

    @Autowired
    RenterService renterService;

    //show table of all employees
    @GetMapping("/renters")
    public String renters(Model model){
        List<Renter> renterList = renterService.showRentersList(); //saves renters into the list
        model.addAttribute("renters",renterList); //adds renters list to the model
        return "viewRenters";
    }

}
