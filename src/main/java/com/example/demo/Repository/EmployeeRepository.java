package com.example.demo.Repository; //Ilias

import com.example.demo.Model.Employee;
import com.example.demo.Security.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    @Autowired
    JdbcTemplate template;

    public List<Employee> fetchAll(){
        String sql = "SELECT employeeID AS id, first_name AS firstName, last_name AS lastName, " +
                            "CPR AS cpr, email, phone, salary, IFNULL(username, 'N/A') AS username, " +
                            "IF(password IS NOT NULL, '*******', 'N/A') AS password, role, enabled AS isEnabled, j.name AS type, " +
                            "street, door, floor, building, zip, c.name AS city, co.name AS country  " +
                     "FROM employee " +
                     "JOIN users USING (userID) " +
                     "JOIN job j USING (jobID) "+
                     "JOIN address USING (addressID) " +
                     "JOIN zip USING (zipID) " +
                     "JOIN city c USING (cityID) " +
                     "JOIN country co USING (countryID)";

        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        return template.query(sql, rowMapper);
    }

    public Employee addEmployee(Employee emp){
        String sql = "INSERT INTO city (name) VALUES (?)";
        template.update(sql, emp.getCity());
        sql = "INSERT INTO zip (zip, cityID, countryID) VALUES (?, (SELECT LAST_INSERT_ID()), (SELECT countryID FROM country WHERE name = ?))";
        template.update(sql, emp.getZip(), emp.getCountry());
        sql = "INSERT INTO address (street, building, floor, door, zipID) VALUES (?, ?, ?, ?, (SELECT LAST_INSERT_ID()))";
        template.update(sql, emp.getStreet(), emp.getBuilding(), emp.getFloor(), emp.getDoor());
        if(emp.getPassword().length() > 0 && emp.getUsername().length() > 0) {
            sql = "INSERT INTO users (username, password, role, enabled) VALUES (?, ?, ?, '1')";
            String hashPass = PasswordGenerator.passGenerator(emp.getPassword());
            template.update(sql, emp.getUsername(), hashPass, emp.getRole());
        }else{
            sql = "INSERT INTO users (role) VALUES (?)";
            template.update(sql, emp.getRole());
        }
        sql = "INSERT INTO job (name) VALUES (?)";
        template.update(sql, emp.getType());
        sql = "INSERT INTO employee (first_name, last_name, CPR, email, phone, salary, jobID, addressID, userID) " +
                "VALUES (?, ?, ?, ?, ?, ?, (SELECT LAST_INSERT_ID()), " +
                "(SELECT addressID FROM address ORDER BY addressID DESC LIMIT 1), (SELECT userID FROM users ORDER BY userID DESC LIMIT 1))";
        template.update(sql, emp.getFirstName(), emp.getLastName(), emp.getCpr(), emp.getEmail(),
                emp.getPhone(), emp.getSalary());
        return null;
    }

    public Employee findEmployeeById(int id){
        String sql = "SELECT employeeID AS id, first_name AS firstName, last_name AS lastName, " +
                "CPR AS cpr, email, phone, salary, IFNULL(username, 'N/A') AS username, " +
                "IF(password IS NOT NULL, '*******', 'N/A') AS password, role, enabled AS isEnabled, j.name AS type, " +
                "street, door, floor, building, zip, c.name AS city, co.name AS country  " +
                "FROM employee " +
                "JOIN users USING (userID) " +
                "JOIN job j USING (jobID) "+
                "JOIN address a USING (addressID) " +
                "JOIN zip USING (zipID) " +
                "JOIN city c USING (cityID) " +
                "JOIN country co USING (countryID) " +
                "WHERE employeeID = ?";

        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        return template.queryForObject(sql, rowMapper, id);
    }

    public Employee updateEmployee(int id, Employee emp){

        String sql = "UPDATE employee " +
                "JOIN users USING (userID) " +
                "JOIN job j USING (jobID) "+
                "JOIN address USING (addressID) " +
                "JOIN zip USING (zipID) " +
                "JOIN city c USING (cityID) " +
                "SET first_name = ?, last_name = ?, cpr = ?, phone = ?, email = ?," +
                " salary = ?, j.name = ?, street = ?, door = ?, floor = ?, building = ?, zip = ?, c.name = ? " +
                "WHERE employeeID = ?";

        template.update(sql, emp.getFirstName(), emp.getLastName(), emp.getCpr(), emp.getPhone(),
                emp.getEmail(), emp.getSalary(), emp.getType(), emp.getStreet(), emp.getDoor(),
                emp.getFloor(), emp.getBuilding(), emp.getZip(), emp.getCity(), emp.getId());

        if(emp.getRole() != null) {
            if (emp.getPassword().length() > 0 && emp.getUsername().length() > 0) {
                sql = "UPDATE users " +
                        "JOIN employee USING(userID) " +
                        "SET username = ?, password = ?, role = ?, enabled = '1'" +
                        "WHERE employeeID = ?";
                String hashPass = PasswordGenerator.passGenerator(emp.getPassword());
                template.update(sql, emp.getUsername(), hashPass, emp.getRole(), id);
            } else {
                sql = "UPDATE users " +
                        "JOIN employee USING(userID) " +
                        "SET role = ? " +
                        "WHERE employeeID = ?";
                template.update(sql, emp.getRole(), id);
            }
        }
        return null;
    }
}
