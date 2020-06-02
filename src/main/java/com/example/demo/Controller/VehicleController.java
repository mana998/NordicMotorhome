package com.example.demo.Controller;

import com.example.demo.Model.Vehicle;
import com.example.demo.Service.AgreementService;
import com.example.demo.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class VehicleController { //Karolina

    @Autowired
    VehicleService vehicleService;
    @Autowired
    AgreementService agreementService;

    //display table with all vehicles
    @GetMapping("/viewVehicles")
    public String showVehicleList(Model model){
        List<Vehicle> vehicleList = vehicleService.showVehicleList();
        model.addAttribute("vehicles", vehicleList);
        return "viewVehicles";
    }

    //display add vehicle form
    @GetMapping("/addVehicle")
    public String addVehicle(){
        return "addVehicle"; }

    //add new vehicle
    @PostMapping("/addVehicle")
    public String addVehicle(@ModelAttribute Vehicle vehicle){
        vehicleService.addVehicle(vehicle);
        return "redirect:/viewVehicles";
    }

    //display information about single vehicle
    @GetMapping("/showVehicle/{vehicleID}")
    public String showVehicle(@PathVariable("vehicleID") int vehicleID, Model model){
        model.addAttribute("vehicle", vehicleService.findVehicleById(vehicleID));
        //WHERE addition to the query
        //for active agreements
        String sql=" WHERE a.vehicleID = " + vehicleID + " AND a.is_cancelled = 0 AND a.start_date <=  CURRENT_DATE() AND a.end_date >=  CURRENT_DATE() ";
        model.addAttribute("activeAgreements", agreementService.getSpecificAgreements(sql));
        //for future agreements
        sql=" WHERE a.vehicleID = " + vehicleID + " AND (a.is_cancelled IS NULL OR a.is_cancelled = 0) AND a.start_date >  CURRENT_DATE() ";
        model.addAttribute("futureAgreements", agreementService.getSpecificAgreements(sql));
        //for inactive agreements
        sql=" WHERE a.vehicleID = " + vehicleID + "  AND (a.is_cancelled = 1 OR a.end_date < CURRENT_DATE()) ";
        model.addAttribute("inactiveAgreements", agreementService.getSpecificAgreements(sql));
        return "showVehicle";
    }

    //display update form
    @GetMapping("/updateVehicle/{vehicleID}")
    public String updateVehicle(@PathVariable("vehicleID") int vehicleID, Model model) {
        model.addAttribute("vehicle", vehicleService.findVehicleById(vehicleID));
        return "updateVehicle";
    }

    //update vehicle information
    @PostMapping("/updateVehicle")
    public String updateVehicle(@ModelAttribute Vehicle vehicle) {
        vehicleService.updateVehicle(vehicle);
        return "redirect:/viewVehicles";
    }

    //delete vehicle
    @GetMapping("/deleteVehicle/{vehicleID}")
    public String deleteVehicle(@PathVariable("vehicleID") int vehicleID, Model model) {
        // if vehicle was deleted
        if (vehicleService.deleteVehicle(vehicleID)) {
            return "redirect:/viewVehicles";
        } else { // in case vehicle wasn't deleted due to active/future contracts
            model.addAttribute("failed", true);
            //used forward instead of redirect to trigger javascript onload event
            return "forward:/viewVehicles";
        }
    }

}
