package com.example.demo.Repository;

import com.example.demo.Model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
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
        String sql="INSERT INTO brand (brand_name) VALUES (?)";
        template.update(sql, vehicle.getBrand());
        sql="INSERT INTO model (model_name, brandID, beds, price) VALUES (?, (SELECT brandID FROM brand WHERE brand_name = ?), ?, ?)";
        template.update(sql, vehicle.getModel(), vehicle.getBrand(), vehicle.getBeds(), vehicle.getPrice());
        sql="INSERT INTO vehicle (plates, is_available, modelID, brandID) VALUES (?,?,(SELECT modelID FROM model WHERE model_name = ?),(SELECT brandID FROM brand WHERE brand_name = ?))";
        template.update(sql, vehicle.getPlates(), vehicle.isIsAvailable(), vehicle.getModel(), vehicle.getBrand());
        return null;
    }






}
