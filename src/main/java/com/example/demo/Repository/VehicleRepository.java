package com.example.demo.Repository;

import com.example.demo.Model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Repository
public class VehicleRepository { //Karolina

    @Autowired
    JdbcTemplate template;

    private static RowMapper<Vehicle> rowMapper = new BeanPropertyRowMapper<>(Vehicle.class);

    //get all the information about vehicles from the database
    public List<Vehicle> showVehicleList(){
        String sql = "SELECT vehicleID, plates, brand_name AS brand, model_name AS model, model.beds, model.price, " +
                "is_available AS isAvailable \n" +
                "FROM vehicle\n" +
                "JOIN brand USING (brandID)\n" +
                "JOIN model USING (modelID);";
        return template.query(sql, rowMapper);
    }

    //get the information about specific vehicle from the database
    public Vehicle findVehicleById(int vehicleID){
        String sql = "SELECT vehicleID, plates, brand_name AS brand, model_name AS model, model.beds, model.price, " +
                "is_available AS isAvailable \n" +
                "FROM vehicle\n" +
                "JOIN brand USING (brandID)\n" +
                "JOIN model USING (modelID)\n" +
                "WHERE vehicleID = ?";
        Vehicle vehicle = template.queryForObject(sql, rowMapper, vehicleID);
        return vehicle;
    }

    //add a new vehicle to the database
    public Vehicle addVehicle(Vehicle vehicle){
        int modelId;
        int brandId;
        //check if the model already exist
        String sql = "SELECT modelID FROM model WHERE model_name = ?";
        try {
            //if model exists, modelID gets assigned to modelId variable
            modelId = template.queryForObject(sql, Integer.class, vehicle.getModel());
            //assign brandID of selected model to brandId variable
            brandId = template.queryForObject("SELECT brandID FROM model WHERE model_name = ?", Integer.class, vehicle.getModel());
        } catch (IncorrectResultSizeDataAccessException e1){
            //if model doesn't exist
            //check if the brand exist already
            sql = "SELECT brandID FROM brand WHERE brand_name = ?";
            try {
                //if brand exists, brandID gets assigned to brandId variable
                brandId = template.queryForObject(sql, Integer.class, vehicle.getBrand());
            } catch (IncorrectResultSizeDataAccessException e2){
                //if brand doesn't exist, insert new brand to the database
                sql = "INSERT INTO brand (brand_name) VALUES (?)";
                template.update(sql, vehicle.getBrand());
                //get id of the new brand
                brandId = template.queryForObject("SELECT MAX(brandID) FROM brand", Integer.class);
            }
            //insert new model to the database
            sql="INSERT INTO model (model_name, brandID, beds, price) VALUES (?, ?, ?, ?)";
            template.update(sql, vehicle.getModel(), brandId, vehicle.getBeds(), vehicle.getPrice());
            //get id of the new model
            modelId = template.queryForObject("SELECT MAX(modelID) FROM model", Integer.class);
        }
        //insert new vehicle to the database
        sql="INSERT INTO vehicle (plates, is_available, modelID, brandID) VALUES (?,?,?,?)";
        template.update(sql, vehicle.getPlates(), vehicle.isIsAvailable(), modelId, brandId);
        return null;
    }

    //display all available vehicles
    public List<Vehicle> showAvailableVehicles(){
        String sql = "SELECT vehicleID, plates, brand_name AS brand, model_name AS model, model.beds, model.price\n" +
                "FROM vehicle\n" +
                "JOIN brand USING (brandID)\n" +
                "JOIN model USING (modelID)" +
                "WHERE is_available = '1';";
        return template.query(sql, rowMapper);
    }

    //find available vehicles with X number of beds
    public List<Vehicle> findAvailableVehiclesWBeds(int beds){
        String sql = "SELECT vehicleID, plates, brand_name AS brand, model_name AS model, model.beds, model.price\n" +
                "FROM vehicle\n" +
                "JOIN brand USING (brandID)\n" +
                "JOIN model USING (modelID)" +
                "WHERE is_available = '1'" +
                "AND model.beds = ?;";
        return template.query(sql, rowMapper, beds);
    }

    //find available vehicles with X price
    public List<Vehicle> findAvailableVehiclesWPrice(double price){
        String sql = "SELECT vehicleID, plates, brand_name AS brand, model_name AS model, model.beds, model.price\n" +
                "FROM vehicle\n" +
                "JOIN brand USING (brandID)\n" +
                "JOIN model USING (modelID)" +
                "WHERE is_available = '1'" +
                "AND model.price = ?;";
        return template.query(sql, rowMapper, price);
    }
  
    //find vehicles available in a specific time period, with X number of beds and below X price // Dimitrios
    public List<Vehicle> findVehiclesAvailableForAgreement(LocalDate startDate, LocalDate endDate, int beds, double price) {
        Date sqlFromDate = Date.valueOf(startDate);
        Date sqlToDate = Date.valueOf(endDate);
        String sql= "SELECT DISTINCT vehicleID, plates, brand_name AS brand, model_name AS model, " +
                        "model.beds, model.price " +
                        "FROM vehicle LEFT JOIN agreement USING (vehicleID)" +
                        "INNER JOIN brand USING (brandID)" +
                        "INNER JOIN model USING (modelID)" +
                        "WHERE is_available = '1' AND vehicleID NOT IN" +
                        "(" +
                        "SELECT vehicle.vehicleID " +
                        "FROM vehicle LEFT JOIN agreement USING (vehicleID)" +
                        "WHERE (start_date <= ? AND end_date >= ?) " +
                        "OR (start_date >= ? AND end_date <= ?)" +
                        "OR (start_date >= ? AND end_date >= ? AND start_date <= ?)" +
                        "OR (start_date <= ? AND end_date <= ? AND end_date >= ?)" +
                        ")" +
                        "AND beds >= ? AND price <= ? ";
        List<Vehicle> result = template.query(sql, new Object[] {sqlFromDate, sqlToDate, sqlFromDate, sqlToDate, sqlFromDate,
                sqlToDate, sqlFromDate, sqlFromDate, sqlToDate, sqlToDate, beds, price}, rowMapper);
        return result;
    }

    // update vehicle information - allows to update plates, price and availability
    public void updateVehicle(Vehicle vehicle){
        String sql = "UPDATE vehicle " +
                "JOIN model USING (modelID) " +
                "SET plates = ?, is_available = ?, price = ? " +
                "WHERE vehicleID = ?";
        template.update(sql, vehicle.getPlates(), vehicle.isIsAvailable(), vehicle.getPrice(), vehicle.getVehicleID());
    }

    //delete vehicle
    public Boolean deleteVehicle(int vehicleID){ //Marianna
        //checks whether vehicle has active/future contracts
        String sql = "SELECT agreementID FROM agreement WHERE vehicleID = ? && end_date>=curdate() && (is_cancelled IS NULL OR is_cancelled = 0)";
        int activeContracts;
        //confirmation of deletion set to true as default
        boolean confirmation = true;
        try {
            activeContracts = template.queryForObject(sql, Integer.class, vehicleID);
            //if 1 active/future contract was found
            //can't delete vehicle
            confirmation = false;
        } catch (EmptyResultDataAccessException e) {
            //no contracts for vehicle so it can go further and execute finally to delete the vehicle
        } catch (IncorrectResultSizeDataAccessException e){
            //more than 1 active/future contract were found
            //can't delete vehicle
            confirmation = false;
        } finally {
            if (confirmation){
                //if vehicle doesn't have agreements it gets deleted
                sql = "DELETE FROM vehicle WHERE vehicleID = ?";
                template.update(sql, vehicleID);
            }
            // returns true if deletion was completed, false if not
            return confirmation;
        }
    }
}
