package com.example.demo.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Renter extends Person { //Marianna

    @Id
    private int id;
    private String licenseNumber;

    public Renter() {
    }

    public Renter(int id, String firstName, String lastName, String cpr, String email,
                  String phone, String street, int building, int floor, String door,
                  int zip, String city, String country, String licenseNumber) {

        super(firstName, lastName, cpr, email, phone, street, building, floor, door, zip, city, country);
        this.id = id;
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
