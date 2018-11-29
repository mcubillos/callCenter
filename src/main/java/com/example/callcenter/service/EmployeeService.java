package com.example.callcenter.service;

import com.example.callcenter.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    List<Employee> findByType(int employeeType);
    List<Employee> findByTypeAndIsAvailable(int employeeType);
    Employee updateEmployee(Employee employee);
}
