package com.momenton.model;

import java.util.Objects;

public class Employee implements Comparable<Employee> {

    private String name;
    private Integer employeeId;
    private Integer managerId;

    // default constructor required in most frameworks and APIs
    public Employee(){
        // can't leave them null, but add invalid values to raise flag if no correct values are assigned
        name = "";
        employeeId = 0;
    }

    public Employee(String name, Integer employeeId) {
        this(name,employeeId,null);
    }

    public Employee(String name, Integer employeeId, Integer managerId) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(employeeId);
        this.name = name;
        this.employeeId = employeeId;
        this.managerId = managerId;
    }

    public boolean hasManager() {
        return managerId != null && managerId > 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        Objects.requireNonNull(employeeId);
        this.employeeId = employeeId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        return employeeId.equals(employee.employeeId);
    }

    @Override
    public int hashCode() {
        return employeeId.hashCode();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", employeeId=" + employeeId +
                ", managerId=" + managerId +
                '}';
    }

    @Override
    public int compareTo(Employee o) {
        return this.employeeId.compareTo(o.employeeId);
    }

}
