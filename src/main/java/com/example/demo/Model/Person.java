package com.example.demo.Model;

public class Person {

    private String firstName;
    private String lastName;
    private String cpr;
    private String email;
    private String phone;
    private String street;
    private int building;
    private int floor;
    private String door;
    private int zip;
    private String city;
    private String country;

    public int getBuilding() {
        return building;
    }

    public void setBuilding(int building) {
        this.building = building;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Person(String firstName, String lastName, String cpr, String email, String phone, String street, int building, int floor, String door, int zip, String city, String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cpr = cpr;
        this.email = email;
        this.phone = phone;
        this.street = street;
        this.building = building;
        this.floor = floor;
        this.door = door;
        this.zip = zip;
        this.city = city;
        this.country = country;
    }

    public Person(){}

    @Override
    public String toString() {
        return "Person{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cpr='" + cpr + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", street='" + street + '\'' +
                ", building=" + building +
                ", floor=" + floor +
                ", door='" + door + '\'' +
                ", zip=" + zip +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
