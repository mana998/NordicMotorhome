package com.example.demo.Service;

import com.example.demo.Model.Employee;
import com.example.demo.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository empRepo;

    public List<Employee> fetchAll(){
        return empRepo.fetchAll();
    }

    public Employee addEmployee(Employee emp){
        return empRepo.addEmployee(emp);
    }

}
