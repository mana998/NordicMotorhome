package com.example.demo.Repository;

import com.example.demo.Model.Employee;
import com.example.demo.Security.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository{ //Ilias

    @Autowired
    JdbcTemplate template;

    //Method to get all the employees and their data from the DB
    public List<Employee> showEmployeesList(){
        // Using aliases as DB column names and our Employee variables have different names
        String sql = "SELECT employeeID AS id, first_name AS firstName, last_name AS lastName, " +
                            "CPR AS cpr, email, phone, salary, IFNULL(username, 'N/A') AS username, " +
                            // if an employee has a password, save it as stars, otherwise save N/A
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

    //Method to add en employee to the DB
    public Employee addEmployee(Employee emp){
        String sql = "INSERT INTO city (name) VALUES (?)";
        template.update(sql, emp.getCity());
        sql = "INSERT INTO zip (zip, cityID, countryID) VALUES (?, (SELECT LAST_INSERT_ID()), (SELECT countryID FROM country WHERE name = ?))";
        template.update(sql, emp.getZip(), emp.getCountry());
        sql = "INSERT INTO address (street, building, floor, door, zipID) VALUES (?, ?, ?, ?, (SELECT LAST_INSERT_ID()))";
        template.update(sql, emp.getStreet(), emp.getBuilding(), emp.getFloor(), emp.getDoor());
        //Inserts username and password only if they were entered
        if(emp.getPassword().length() > 0 && emp.getUsername().length() > 0) {
            sql = "INSERT INTO users (username, password, role, enabled) VALUES (?, ?, ?, '1')";
            //Encodes the password to hash representation
            String hashPass = PasswordGenerator.passGenerator(emp.getPassword());
            //Updates the database with username, encoded password, role
            template.update(sql, emp.getUsername(), hashPass, emp.getRole());
        }else{
            //else only saves the role as "restricted"
            sql = "INSERT INTO users (role) VALUES (?)";
            template.update(sql, emp.getRole());
        }
        sql = "INSERT INTO job (name) VALUES (?)";
        template.update(sql, emp.getType());
        sql = "INSERT INTO employee (first_name, last_name, CPR, email, phone, salary, jobID, addressID, userID) " +
                "VALUES (?, ?, ?, ?, ?, ?, (SELECT LAST_INSERT_ID()), " +
                //We need the latest IDs from address and users tables, so we order by descending and take the first row
                "(SELECT addressID FROM address ORDER BY addressID DESC LIMIT 1), (SELECT userID FROM users ORDER BY userID DESC LIMIT 1))";
        template.update(sql, emp.getFirstName(), emp.getLastName(), emp.getCpr(), emp.getEmail(),
                emp.getPhone(), emp.getSalary());
        return null;
    }

    //Method to select an employee based on their ID
    public Employee findEmployeeById(int id){
        String sql = "SELECT employeeID AS id, first_name AS firstName, last_name AS lastName, " +
                // if the employee has no username, N/A is returned
                "CPR AS cpr, email, phone, salary, IFNULL(username, 'N/A') AS username, " +
                // if an employee has a password, returns it as stars, otherwise returns it as N/A
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

    // Method to update employee information
    public Employee updateEmployee(int id, Employee emp){

        String sql = "SELECT countryID FROM country WHERE name = ?";
        Integer countryID = template.queryForObject(sql, Integer.class, emp.getCountry());

                sql = "UPDATE employee " +
                "JOIN users USING (userID) " +
                "JOIN job j USING (jobID) "+
                "JOIN address USING (addressID) " +
                "JOIN zip USING (zipID) " +
                "JOIN city c USING (cityID) " +
                "SET first_name = ?, last_name = ?, cpr = ?, phone = ?, email = ?," +
                " salary = ?, j.name = ?, street = ?, door = ?, floor = ?, building = ?, zip = ?, countryID = ?, c.name = ? " +
                "WHERE employeeID = ?";

        template.update(sql, emp.getFirstName(), emp.getLastName(), emp.getCpr(), emp.getPhone(),
                emp.getEmail(), emp.getSalary(), emp.getType(), emp.getStreet(), emp.getDoor(),
                emp.getFloor(), emp.getBuilding(), emp.getZip(), countryID, emp.getCity(), emp.getId());

        //if the role was entered checks for username and password entry
        if(emp.getRole() != null) {
            if (emp.getPassword().length() > 0 && emp.getUsername().length() > 0) {
                sql = "UPDATE users " +
                        "JOIN employee USING(userID) " +
                        "SET username = ?, password = ?, role = ?, enabled = '1'" +
                        "WHERE employeeID = ?";
                //Encodes the password to hash representation
                String hashPass = PasswordGenerator.passGenerator(emp.getPassword());
                //Updates the database with username, encoded password, role
                template.update(sql, emp.getUsername(), hashPass, emp.getRole(), id);
            } else {
                // else just saves the role as restricted, with no access to the system
                sql = "UPDATE users " +
                        "JOIN employee USING(userID) " +
                        "SET role = ?, username = ?, password = ? " +
                        "WHERE employeeID = ?";
                template.update(sql, emp.getRole(), null, null, id);
            }
        }
        return null;
    }

    // Method to delete employee
    public Boolean deleteEmployee(int id){

        String sql = "SELECT userID " +
                "FROM users " +
                "JOIN employee USING (userID) " +
                "WHERE employeeID = ?";
        Integer userId = template.queryForObject(sql, Integer.class, id);

        sql = "DELETE address FROM address " +
                "JOIN employee USING (addressID) " +
                "WHERE employeeID = ?";
        template.update(sql, id);


        sql = "DELETE FROM users WHERE userID = ?";
        return template.update(sql, userId) > 0;
    }
}
