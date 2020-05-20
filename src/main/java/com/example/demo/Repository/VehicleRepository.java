package com.example.demo.Repository;

import com.example.demo.Model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehicleRepository { //Karolina

    @Autowired
    JdbcTemplate template;

    private static RowMapper<Vehicle> rowMapper = new BeanPropertyRowMapper<>(Vehicle.class);

    //get all the information about vehicles from the database
    public List<Vehicle> showVehicleList(){
        String sql = "SELECT vehicleID, plates, brand_name AS brand, model_name AS model, model.beds, model.price, is_available AS isAvailable \n" +
                "FROM vehicle\n" +
                "JOIN brand USING (brandID)\n" +
                "JOIN model USING (modelID);";
        return template.query(sql, rowMapper);
    }

    //get the information about specific vehicle from the database
    public Vehicle findVehicleById(int vehicleID){
        String sql = "SELECT vehicleID, plates, brand_name AS brand, model_name AS model, model.beds, model.price, is_available AS isAvailable \n" +
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

}
