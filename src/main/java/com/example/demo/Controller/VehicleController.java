package com.example.demo.Controller;

import com.example.demo.Model.Vehicle;
import com.example.demo.Service.BrandService;
import com.example.demo.Service.ModelService;
import com.example.demo.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class VehicleController { //Karolina

    @Autowired
    VehicleService vehicleService;
    @Autowired
    BrandService brandService;
    @Autowired
    ModelService modelService;

    @GetMapping("/viewVehicles")
    public String showVehicleList(Model model){
        List<Vehicle> vehicleList = vehicleService.showVehicleList();
        model.addAttribute("vehicles", vehicleList);
        return "viewVehicles";
    }

    @GetMapping("/addVehicle")
    public String addVehicle(Model model){
        //model.addAttribute("brands", brandService.showBrandsList());
        //model.addAttribute("models", modelService.showModelsList());
        return "/addVehicle"; }

    @PostMapping("/addVehicle")
    public String addVehicle(@ModelAttribute Vehicle vehicle){
        vehicleService.addVehicle(vehicle);
        return "redirect:/viewVehicles";
    }

}
