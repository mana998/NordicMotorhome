package com.example.demo.Service;

import com.example.demo.Model.Employee;
import com.example.demo.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService { //Ilias

    @Autowired
    EmployeeRepository empRepo;

    public List<Employee> showEmployeesList(){
        return empRepo.showEmployeesList();
    }

    public Employee addEmployee(Employee emp){
        return empRepo.addEmployee(emp);
    }

    public Employee findEmployeeById(int id){
        return empRepo.findEmployeeById(id);
    }

    public Employee updateEmployee(int id, Employee emp){ return empRepo.updateEmployee(id, emp); }

    public boolean deleteEmployee(int id){ return empRepo.deleteEmployee(id); }

}
