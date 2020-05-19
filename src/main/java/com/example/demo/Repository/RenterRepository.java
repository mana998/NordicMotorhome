package com.example.demo.Repository;

import com.example.demo.Model.Renter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RenterRepository {

    @Autowired
    JdbcTemplate template;

    private static RowMapper<Renter> rowMapper = new BeanPropertyRowMapper<>(Renter.class);
    
    //get all the information about renters from the database
    public List<Renter> showRentersList(){
        String sql = "SELECT renterID AS id, first_name AS firstName, last_name AS lastName, CPR AS cpr, email, phone, " +
                "driver_license_number AS licenseNumber, a.street, a.building, a.floor, a.door, z.zip, city.name AS city," +
                " c.name AS country FROM renter r JOIN address a ON r.addressID=a.addressID JOIN zip z ON a.zipID=z.zipID" +
                " JOIN city ON z.cityID=city.cityID JOIN country c ON z.countryID=c.countryID";
        return template.query(sql, rowMapper);
    }

    //get specific renter according to ID
    public Renter getRenter(int id){
        String sql = "SELECT renterID AS id, first_name AS firstName, last_name AS lastName, CPR AS cpr, email, phone," +
                " driver_license_number AS licenseNumber, a.street, a.building, a.floor, a.door, z.zip, city.name AS city," +
                " c.name AS country FROM renter r JOIN address a ON r.addressID=a.addressID JOIN zip z ON a.zipID=z.zipID " +
                "JOIN city ON z.cityID=city.cityID JOIN country c ON z.countryID=c.countryID WHERE renterID = ?";
        return template.queryForObject(sql, rowMapper, id);
    }

    //add new renter
    public void addRenter(Renter renter){
        int zip ;
        int city;
        //checks database, whether the entered city already exists
        String sql = "SELECT cityID FROM city WHERE name = ?";
        try {
            //tries to execute the query
            //if city exists, id gets assigned to city variable
            city = template.queryForObject(sql, Integer.class, renter.getCity());
        } catch (IncorrectResultSizeDataAccessException e){
            //if returned result set isn't 1, insert new city to the database
            sql = "INSERT INTO city (name) VALUES (?)";
            template.update(sql, renter.getCity());
            //get id of the new city
            city = template.queryForObject("SELECT MAX(cityID) FROM city", Integer.class);
        }
        //as database has all the countries, selects the country id
        sql = "SELECT countryID FROM country WHERE name = ?";
        Integer country = template.queryForObject(sql, Integer.class, renter.getCountry());
        //looks for the zip ID where zip, cityID and countryID match
        sql = "SELECT zipID FROM zip WHERE zip = ? && cityID = ? && countryID = ?";
        try {
            //tries to execute the query
            //if correct zip exists, id gets assigned to zip variable
            zip = template.queryForObject(sql, Integer.class, renter.getZip(), city, country);
        } catch (IncorrectResultSizeDataAccessException e){
            //if returned result set isn't 1, insert new zip with correct associations to the database
            sql = "INSERT INTO zip (zip, cityID, countryID) VALUES (?,?,?)";
            template.update(sql, renter.getZip(), city, country);
            //get id of the new zip
            zip = template.queryForObject("SELECT MAX(zipID) FROM zip", Integer.class);
        }
        //insert new address to the database
        sql = "INSERT INTO address (street, building, floor, door, zipID) VALUES (?,?,?,?,?)";
        template.update(sql, renter.getStreet(), renter.getBuilding(), renter.getFloor(), renter.getDoor(), zip);
        //get id of the new address
        Integer address = template.queryForObject("SELECT MAX(addressID) FROM address", Integer.class);
        //insert new renter to the database
        sql= "INSERT INTO renter (first_name, last_name, CPR, email, phone, driver_license_number, addressID) " +
                "VALUES (?,?,?,?,?,?,?)";
        template.update(sql, renter.getFirstName(), renter.getLastName(), renter.getCpr(), renter.getEmail(),
                renter.getPhone(), renter.getLicenseNumber(), address);
    }

}
