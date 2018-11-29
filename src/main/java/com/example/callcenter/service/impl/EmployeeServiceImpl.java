package com.example.callcenter.service.impl;

import com.example.callcenter.model.Employee;
import com.example.callcenter.repository.EmployeeRepository;
import com.example.callcenter.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> findByType(int employeeType) {
        return employeeRepository.findByType(employeeType);
    }

    @Override
    public List<Employee> findByTypeAndIsAvailable(int employeeType){
        return employeeRepository.findByTypeAndIsAvailable(employeeType, true);
    }

    @Override
    public Employee updateEmployee(Employee employee){
        return employeeRepository.save(employee);
    }
}
