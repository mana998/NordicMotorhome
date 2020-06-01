package com.example.demo.Controller;

import com.example.demo.Model.Employee;
import com.example.demo.Service.CountryService;
import com.example.demo.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController { //Ilias

    @Autowired
    EmployeeService employeeService;
    @Autowired
    CountryService countryService;

    //show table of all employees
    @GetMapping("/viewEmployees")
    public String employees(Model model){
                //Gets all the employees from the database
                List<Employee> employeeList = employeeService.showEmployeesList();
                //and passes them to the model
                model.addAttribute("employees", employeeList);
        return "viewEmployees";
    }

    //show add employee form
    @GetMapping("/addEmployee")
    public String addEmployee(Model model){
        //passes all countries to the model (to fill up the country drop down list)
        model.addAttribute("countries", countryService.showCountriesList());
        return "addEmployee";
    }

    //add a new employee
    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute Employee employee){
        //passes the employee object to the Repository through Service, and saves in the DB
        employeeService.addEmployee(employee);
        return "redirect:/viewEmployees";
    }

    //view specific employee's information
    @GetMapping("/viewEmployee/{id}")
    public String viewEmployee(@PathVariable("id") int id, Model model){
        model.addAttribute("employee", employeeService.findEmployeeById(id));
        return "showEmployee";
    }

    //fill the update form with employee data and shows the update form
    @GetMapping("/updateEmployee/{id}")
    public String updateEmployee(@PathVariable("id") int id, Model model){
        //passes all countries and the employee to the model (to fill up the data fields)
        model.addAttribute("employee", employeeService.findEmployeeById(id));
        model.addAttribute("countries", countryService.showCountriesList());
        return "updateEmployee";
    }

    //save employee information changes
    @PostMapping("/updateEmployee")
    public String updateEmployee(@ModelAttribute Employee employee){
        //passes the employee object to the Repository through Service, and saves in the DB
        employeeService.updateEmployee(employee.getId(), employee);
        return "redirect:/viewEmployees";
    }

    //deletes an employee
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable("id") int id){
        boolean deleted = employeeService.deleteEmployee(id);
        if(deleted){
            return "redirect:/viewEmployees";
        }else{
            return "redirect:/viewEmployees";
        }
    }

}