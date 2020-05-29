package com.example.demo.Repository;

import com.example.demo.Model.Renter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public Renter findRenterById(int id){
        String sql = "SELECT renterID AS id, first_name AS firstName, last_name AS lastName, CPR AS cpr, email, phone," +
                " driver_license_number AS licenseNumber, a.street, a.building, a.floor, a.door, z.zip, city.name AS city," +
                " c.name AS country FROM renter r JOIN address a ON r.addressID=a.addressID JOIN zip z ON a.zipID=z.zipID " +
                "JOIN city ON z.cityID=city.cityID JOIN country c ON z.countryID=c.countryID WHERE renterID = ?";
        return template.queryForObject(sql, rowMapper, id);
    }

    //this part is necessary for both update and add renter functionality
    //as it looks whether given value is already in the table to retrieve only id
    //or whether it should be added
    public int compareInformation(Renter renter){
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
        return zip;
    }

    //add new renter
    public void addRenter(Renter renter){
        int zip = compareInformation(renter);
        //insert new address to the database
        String sql = "INSERT INTO address (street, building, floor, door, zipID) VALUES (?,?,?,?,?)";
        template.update(sql, renter.getStreet(), renter.getBuilding(), renter.getFloor(), renter.getDoor(), zip);
        //get id of the new address
        Integer address = template.queryForObject("SELECT MAX(addressID) FROM address", Integer.class);
        //insert new renter to the database
        sql= "INSERT INTO renter (first_name, last_name, CPR, email, phone, driver_license_number, addressID) " +
                "VALUES (?,?,?,?,?,?,?)";
        template.update(sql, renter.getFirstName(), renter.getLastName(), renter.getCpr(), renter.getEmail(),
                renter.getPhone(), renter.getLicenseNumber(), address);
    }

    public void updateRenter(Renter renter){
        int zip = compareInformation(renter);
        //update current address in the database
        String sql = "UPDATE address SET street = ?, building = ?, floor = ?, door = ?, zipID = ? WHERE addressID = " +
                "(SELECT addressID FROM renter WHERE renterID = ?)";
        template.update(sql, renter.getStreet(), renter.getBuilding(), renter.getFloor(), renter.getDoor(), zip, renter.getId());
        //update current renter in the database
        sql= "UPDATE renter SET first_name = ?, last_name = ?, CPR = ?, email = ?, phone = ?, driver_license_number = ? WHERE renterID = ?";
        template.update(sql, renter.getFirstName(), renter.getLastName(), renter.getCpr(), renter.getEmail(), renter.getPhone(), renter.getLicenseNumber(), renter.getId());
    }

    public boolean deleteRenter(int id){
        //checks whether exist with active or future contract exists
        String sql = "SELECT agreementID FROM agreement WHERE renterID = ? && end_date>=curdate() && is_cancelled = 0";
        int activeContracts;
        //confirmation of deletion set to true as default
        boolean confirmation = true;
        try {
            activeContracts = template.queryForObject(sql, Integer.class, id);
            //exactly 1 future/active contract were found
            //can't delete renter
            confirmation = false;
        } catch (EmptyResultDataAccessException e){
            //this exception is okay - no contracts for the renter and it can execute finally block to delete the renter
        } catch (IncorrectResultSizeDataAccessException e){
            //more than 1 future/active contracts were found
            //can't delete renter
            confirmation = false;
        } finally{
            if (confirmation) {
                //if renter doesn't have active/future contracts it deletes him
                sql = "DELETE FROM renter WHERE renterID = ?";
                template.update(sql, id);
            }
            //returns whether deletion has been completed or no
            return confirmation;
        }
    }



    // Dimitrios
    public int findMaxRenterId() {
        String sql = "SELECT MAX(renterId) FROM renter";
        return template.queryForObject("SELECT MAX(renterID) FROM renter", Integer.class);
    }

    // Dimitrios
    public List<Renter> findByDriverLicenseNumber(String driverLicenseNumber) {
        String str = "%" + driverLicenseNumber + "%";
        return template.query
                ("SELECT renterID AS id, first_name, last_name, email, driver_license_number AS licenseNumber, phone \n" +
                                "FROM renter " +
                                "WHERE driver_license_number LIKE ? ;",
                        new Object[] { str }, rowMapper);
    }
}
