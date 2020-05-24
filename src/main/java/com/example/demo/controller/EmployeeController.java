package com.example.demo.Controller; //Ilias

import com.example.demo.Model.Employee;
import com.example.demo.Service.CountryService;
import com.example.demo.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    CountryService countryService;

    @GetMapping("/viewEmployees")
    public String employees(Model model){
                List<Employee> employeeList = employeeService.fetchAll();
                model.addAttribute("employees", employeeList);
        return "/viewEmployees";
    }
    @GetMapping("/addEmployee")
    public String addEmployee(Model model){
        model.addAttribute("countries", countryService.showCountriesList());
        return "/addEmployee";
    }
    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute Employee employee){
        employeeService.addEmployee(employee);
        return "redirect:/viewEmployees";
    }
    @GetMapping("/viewEmployee/{id}")
    public String viewEmployee(@PathVariable("id") int id, Model model){
        model.addAttribute("employee", employeeService.findEmployeeById(id));
        return "/viewEmployee";
    }

}