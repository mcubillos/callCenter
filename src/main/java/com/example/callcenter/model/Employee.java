package com.example.callcenter.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int type;
    private boolean isAvailable;

    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return  Objects.equals(getId(), employee.getId()) &&
                Objects.equals(getName(), employee.getName()) &&
                getType() == employee.getType() &&
                isAvailable() == employee.isAvailable();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getType(), isAvailable());
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
