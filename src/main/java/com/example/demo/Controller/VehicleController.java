package com.example.demo.Controller;

import com.example.demo.Model.Vehicle;
import com.example.demo.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class VehicleController { //Karolina

    @Autowired
    VehicleService vehicleService;

    @GetMapping("/vehicles")
    public String showVehicleList(Model model){
        List<Vehicle> vehicleList = vehicleService.showVehicleList();
        model.addAttribute("vehicles", vehicleList);
        return "viewVehicles";
    }

}
