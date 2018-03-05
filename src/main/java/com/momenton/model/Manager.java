package com.momenton.model;

import java.util.List;

public class Manager extends Employee {

    private List<Employee> subordinates;

    public Manager(Employee employee, List<Employee> subordinates) {
        super(employee.getName(), employee.getEmployeeId(), employee.getManagerId());
        this.subordinates = subordinates;
    }

    public Manager(String name, Integer employeeId) {
        super(name, employeeId);
    }

    public void addSubordinate(Employee employee) {
        subordinates.add(employee);
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(List<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "\nname='" + getName() + '\'' +
                ",\nemployeeId=" + getEmployeeId() +
                ",\nmanagerId=" + getManagerId() +
                ",\nsubordinates=" + subordinates +
                '}';
    }
}
