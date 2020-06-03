package com.example.demo.Service;

import com.example.demo.Model.Vehicle;
import com.example.demo.Repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
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
    public List<Vehicle> findVehiclesAvailableForAgreement(LocalDate startDate, LocalDate endDate, int beds, double price) { return vehicleRepository.findVehiclesAvailableForAgreement(startDate, endDate, beds, price); }
    public void updateVehicle(Vehicle vehicle){ vehicleRepository.updateVehicle(vehicle); }
    public Boolean deleteVehicle(int vehicleID){ return vehicleRepository.deleteVehicle(vehicleID); }

}
