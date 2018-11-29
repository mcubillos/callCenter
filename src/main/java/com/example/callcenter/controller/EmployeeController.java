package com.example.callcenter.controller;

import com.example.callcenter.model.Employee;
import com.example.callcenter.service.EmployeeService;
import com.example.callcenter.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO<Iterable<Employee>>> findAll() {
        return ResponseEntity.ok(new ResponseVO<>(employeeService.findAll()));
    }

    @GetMapping(value = "/{employee_type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseVO<Iterable<Employee>>> get(@PathVariable("employee_type") int employeeType) {
        return ResponseEntity.ok(new ResponseVO<>(employeeService.findByType(employeeType)));
    }
}
