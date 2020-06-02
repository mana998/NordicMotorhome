package com.example.demo.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Vehicle { //Karolina

    @Id
    private int vehicleID;
    private String plates;
    private String brand;
    private String model;
    private int beds;
    private double price;
    private boolean isAvailable;

    public Vehicle() {
    }

    public Vehicle(String plates, String brand, String model, int beds, double price, boolean isAvailable) {
        this.vehicleID = vehicleID;
        this.plates = plates;
        this.brand = brand;
        this.model = model;
        this.beds = beds;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getPlates() {
        return plates;
    }

    public void setPlates(String plates) {
        this.plates = plates;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
