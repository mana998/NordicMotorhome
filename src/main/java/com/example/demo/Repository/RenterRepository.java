package com.example.demo.Repository;

import com.example.demo.Model.Renter;
import org.springframework.beans.factory.annotation.Autowired;
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



}
