package com.example.demo.Repository;

import com.example.demo.Model.Agreement;
import com.example.demo.Model.Item;
import com.example.demo.Model.Renter;
import com.example.demo.Model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class AgreementRepository {

    @Autowired
    JdbcTemplate template;

    private static RowMapper<Item> itemRowMapper = new BeanPropertyRowMapper<>(Item.class);

    public List<Agreement> findAll(){
        String sql = "SELECT agreementID, renterID, first_name, last_name, vehicleID, plates, price, start_date, end_date, pick_up_point, drop_off_point, " +
                "driven_km, level_of_gasoline, is_cancelled " +
                "FROM agreement INNER JOIN vehicle USING (vehicleID) INNER JOIN renter USING (renterID) INNER JOIN model USING (modelID) ORDER BY agreementID ";
        new AgreementRowMapper();
        return template.query(sql, new AgreementRowMapper());
    }

    public List<Item> getAllLineItems(int agreementId) {
        return template.query("SELECT extras_name AS name, extras_price AS price \n" +
                        "FROM agreement_has_extras INNER JOIN extras USING (extrasID) \n" +
                        "WHERE agreementId = ? ",
                        new Object[] { agreementId },itemRowMapper);
    }

    public List<Item> findAllItems() {
        String sql = "SELECT extrasID AS id, extras_name AS name, extras_price AS price FROM extras";
        return template.query(sql, itemRowMapper);
    }

    public void addAgreement(Agreement agreement) {
        String sql = "INSERT INTO agreement (renterID, vehicleID, start_date, end_date, pick_up_point, drop_off_point) VALUES (?,?,?,?,?,?)";
        template.update(sql, agreement.getRenter().getId(), agreement.getVehicle().getVehicleID(), agreement.getStartDate(),
        agreement.getEndDate(), agreement.getPickUpPoint(), agreement.getDropOffPoint());
    }

    public Item findItemById(int id) {
        String sql = "SELECT extrasID AS id, extras_name AS name, extras_price AS price FROM extras WHERE extrasID = ? ";
        return template.queryForObject(sql, itemRowMapper, id);
    }

    public void addItems(int agreementId, int itemId) {
        String sql = "INSERT INTO agreement_has_extras (agreementID, extrasID) VALUES (?,?)";
        template.update(sql, agreementId, itemId);
    }

    public int findMaxAgreementId() {
        return template.queryForObject("SELECT MAX(agreementID) FROM agreement", Integer.class);
    }

    public Agreement findById(int agreementId) {
        String sql = "SELECT agreementID, renterID, first_name, last_name, vehicleID, start_date, end_date, pick_up_point, drop_off_point, " +
                     "driven_km, level_of_gasoline, plates, price, is_cancelled " +
                     "FROM agreement INNER JOIN vehicle USING (vehicleID) INNER JOIN renter USING (renterID) INNER JOIN model USING (modelID) WHERE agreementID = ? ";
        return template.queryForObject(sql, new AgreementRowMapper(), agreementId);
    }

    // not used for now
    public void updateAgreement(Agreement agreement) {
        String updateStatement = "UPDATE agreement " +
                "SET renterID = ?, vehicleID = ?, start_date = ?, end_date = ?, pick_up_point = ?," +
                "drop_off_point = ?, driven_km = ?, level_of_gasoline = ?, is_cancelled = ? " +
                "WHERE agreementID = ? ";
        template.update(updateStatement, agreement.getRenter().getId(), agreement.getVehicle().getVehicleID(),
                agreement.getStartDate(), agreement.getEndDate(), agreement.getPickUpPoint(),
                agreement.getDropOffPoint(), agreement.getDrivenKm(), agreement.isLevelGasoline(),
                agreement.isCancelled(), agreement.getId());
    }

    // method for deciding if we can delete a renter
    public boolean renterActiveAgreements(int renterId) {
        // searches database for active agreements for the given renter
        String sql = "SELECT agreementID FROM agreement WHERE (start_date >= CURRENT_DATE() AND is_cancelled = 0) AND renterID = ?";
        List<Integer> agreementIds = template.queryForList(sql, Integer.class, renterId);
        // returns true if there are no active agreements for the given renter
        return agreementIds.isEmpty();
    }

    public boolean vehicleActiveAgreements(int vehicleId) {
        String sql = "SELECT agreementID FROM agreement WHERE (start_date >= CURRENT_DATE() AND is_cancelled = 0) AND vehicleID = ?";
        List<Integer> agreementIds = template.queryForList(sql, Integer.class, vehicleId);
        return agreementIds.isEmpty();
    }

    //Used mostly for show Vehicle/Renter
    //get basic information about agreement depending on condition passed as parameter
    public List<Agreement> getSpecificAgreements(String addition) { //Marianna
        String sql = "SELECT a.agreementID, a.vehicleID, brand_name, model_name, a.renterID, r.first_name, r.last_name, a.start_date, a.end_date, a.is_cancelled " +
                "FROM agreement a INNER JOIN vehicle v USING (vehicleID) JOIN brand USING (brandID) JOIN model USING (modelID) JOIN renter r ON a.renterID = r.renterID " + addition;
        return template.query(sql, new AgreementRowMapperShort());
    }

    public List<Agreement> findByEndDate(LocalDate endDate) {
        String str = "%" + endDate + "%";
        return template.query
                ("SELECT agreementID, renterID, first_name, last_name, vehicleID, plates, price, start_date, end_date, pick_up_point, drop_off_point," +
                                "driven_km, level_of_gasoline, is_cancelled " +
                                "FROM agreement INNER JOIN renter USING (renterID) INNER JOIN vehicle USING (vehicleID) INNER JOIN model USING (modelID)" +
                                "WHERE end_date LIKE ? ;",
                        new Object[] { str }, new AgreementRowMapper());
    }
}

// maps a row to a new Agreement object and handles OneToOne relationship with Renter and Vehicle
class AgreementRowMapper implements RowMapper<Agreement> {
    @Override
    public Agreement mapRow(ResultSet rs, int rowNum) throws SQLException {
        Agreement agreement = new Agreement();
        agreement.setId(rs.getInt("agreementID"));
        Renter renter = new Renter();
        renter.setId(rs.getInt("renterID"));
        // retrieves renter first and last name in order to display them in the agreements table
        renter.setFirstName(rs.getString("first_name"));
        renter.setLastName(rs.getString("last_name"));
        agreement.setRenter(renter);
        Vehicle vehicle = new Vehicle();
        // retrieves vehicle id and vehicle license plates in order to display them in the agreements table
        vehicle.setVehicleID(rs.getInt("vehicleID"));
        vehicle.setPlates(rs.getString("plates"));
        vehicle.setPrice(rs.getDouble("price"));
        agreement.setVehicle(vehicle);
        // retrieves agreement information (start date, end date etc.)
        Date sqlDate = rs.getDate("start_date");
        // converts sql date to LocalDate Java object
        LocalDate localDate = Date.valueOf(String.valueOf(sqlDate)).toLocalDate();
        agreement.setStartDate(localDate);
        Date sqlDate2 = rs.getDate("end_date");
        LocalDate localDate2 = Date.valueOf(String.valueOf(sqlDate2)).toLocalDate();
        agreement.setEndDate(localDate2);
        agreement.setPickUpPoint(rs.getInt("pick_up_point"));
        agreement.setDropOffPoint(rs.getInt("drop_off_point"));
        agreement.setDrivenKm(rs.getInt("driven_km"));
        agreement.setLevelGasoline(rs.getBoolean("level_of_gasoline"));
        agreement.setCancelled(rs.getBoolean("is_cancelled"));
        return agreement;
    }
}

class AgreementRowMapperShort implements RowMapper<Agreement> {
    @Override
    public Agreement mapRow(ResultSet rs, int rowNum) throws SQLException {
        Agreement agreement = new Agreement();
        agreement.setId(rs.getInt("agreementID"));

        //create new renter object with parameters from the query
        Renter renter = new Renter();
        renter.setId(rs.getInt("renterID"));
        renter.setFirstName(rs.getString("first_name"));
        renter.setLastName(rs.getString("last_name"));
        //add renter object to renter object
        agreement.setRenter(renter);

        //create new vehicle object with parameters from the query
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleID(rs.getInt("vehicleID"));
        vehicle.setBrand(rs.getString("brand_name"));
        vehicle.setModel(rs.getString("model_name"));
        //add vehicle object to renter object
        agreement.setVehicle(vehicle);

        Date sqlDate = rs.getDate("start_date");
        LocalDate localDate = Date.valueOf(String.valueOf(sqlDate)).toLocalDate();
        agreement.setStartDate(localDate);

        Date sqlDate2 = rs.getDate("end_date");
        LocalDate localDate2 = Date.valueOf(String.valueOf(sqlDate2)).toLocalDate();
        agreement.setEndDate(localDate2);

        agreement.setCancelled(rs.getBoolean("is_cancelled"));

        return agreement;
    }

}

