package com.example.demo.Repository;

import com.example.demo.Model.Agreement;
import com.example.demo.Model.Item;
import com.example.demo.Model.Renter;
import com.example.demo.Model.Vehicle;
import net.bytebuddy.matcher.InstanceTypeMatcher;
import org.springframework.beans.factory.annotation.Autowired;
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
        String sql = "SELECT agreementID, renterID, vehicleID, plates, start_date, end_date, pick_up_point, drop_off_point, " +
                "driven_km, level_of_gasoline, is_cancelled, total_cost " +
                "FROM agreement INNER JOIN vehicle USING (vehicleID) ORDER BY agreementID ";
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
        String sql = "SELECT agreementID, renterID, vehicleID, start_date, end_date, pick_up_point, drop_off_point, " +
                     "driven_km, level_of_gasoline, plates, is_cancelled " +
                     "FROM agreement INNER JOIN vehicle USING (vehicleID) WHERE agreementID = ? ";
        return template.queryForObject(sql, new AgreementRowMapper(), agreementId);
    }

    public void updateAgreement(Agreement theAgreement) {
        System.out.println("agreement before update = " + theAgreement);
        String updateStatement = "UPDATE agreement " +
                "SET drop_off_point = ?, pick_up_point = ? WHERE agreementID = ? ";
        template.update(updateStatement, theAgreement.getPickUpPoint(), theAgreement.getDropOffPoint(), theAgreement.getId());
    }
}

class AgreementRowMapper implements RowMapper<Agreement> {
    @Override
    public Agreement mapRow(ResultSet rs, int rowNum) throws SQLException {
        Agreement agreement = new Agreement();
        agreement.setId(rs.getInt("agreementID"));
        Renter renter = new Renter();
        renter.setId(rs.getInt("renterID"));
        agreement.setRenter(renter);

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleID(rs.getInt("vehicleID"));
        vehicle.setPlates(rs.getString("plates"));
        agreement.setVehicle(vehicle);

        Date sqlDate = rs.getDate("start_date");
        LocalDate localDate = Date.valueOf(String.valueOf(sqlDate)).toLocalDate();
        agreement.setStartDate(localDate);

        Date sqlDate2 = rs.getDate("end_date");
        LocalDate localDate2 = Date.valueOf(String.valueOf(sqlDate2)).toLocalDate();
        agreement.setEndDate(localDate2);

        agreement.setPickUpPoint(rs.getInt("pick_up_point"));
        agreement.setDropOffPoint(rs.getInt("drop_off_point"));
        agreement.setDrivenKm(rs.getInt("driven_km"));
        agreement.setLevelGasoline(rs.getBoolean("level_of_gasoline"));
        return agreement;
    }
}

