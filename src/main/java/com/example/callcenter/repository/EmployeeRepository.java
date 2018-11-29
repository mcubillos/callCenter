package com.example.callcenter.repository;

import com.example.callcenter.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByType(int type);
    List<Employee> findByTypeAndIsAvailable(int type, boolean available);
}