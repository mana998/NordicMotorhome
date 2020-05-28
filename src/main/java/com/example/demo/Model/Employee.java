package com.example.demo.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Employee extends Person{ //Ilias

    @Id
    private int id;
    private String type;
    private int salary;
    private String username;
    private String password;
    private String role;
    public Integer isEnabled;

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public void setType(String type) {
        this.type = type;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Employee(String firstName, String lastName, String cpr, String email, String phone,
                    String street, int building, int floor, String door, int zip, String city, String country,
                    int id, String type, int salary, String username, String password, String role, Integer isEnabled) {
        super(firstName, lastName, cpr, email, phone, street, building, floor, door, zip, city, country);
        this.id = id;
        this.type = type;
        this.salary = salary;
        this.username = username;
        this.password = password;
        this.role = role;
        this.isEnabled = isEnabled;
    }

    public Employee() {}


}
