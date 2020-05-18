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

    public List<Vehicle> showVehicleList(){
        String sql = "SELECT vehicleID, plates, brand_name AS brand, model_name AS model, model.beds, model.price, is_available AS isAvailable \n" +
                "FROM vehicle\n" +
                "JOIN brand USING (brandID)\n" +
                "JOIN model USING (modelID);";
        RowMapper<Vehicle> rowMapper = new BeanPropertyRowMapper<>(Vehicle.class);
        return template.query(sql, rowMapper);
    }

}
