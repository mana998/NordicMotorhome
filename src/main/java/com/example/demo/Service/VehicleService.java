package com.example.demo.Service;

import com.example.demo.Model.Vehicle;
import com.example.demo.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService { //Karolina

    @Autowired
    VehicleRepository vehicleRepository;

    public List<Vehicle> showVehicleList(){
        return vehicleRepository.showVehicleList();
    }
    public Vehicle findVehicleById(int vehicleID){
        return vehicleRepository.findVehicleById(vehicleID);
    }
    public Vehicle addVehicle(Vehicle vehicle){
        return vehicleRepository.addVehicle(vehicle);
    }

}
