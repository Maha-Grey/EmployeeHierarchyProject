package com.momenton.service;

import com.momenton.model.Employee;
import com.momenton.model.EmployeeHierarchy;

import java.util.List;

public interface EmployeeHierarchyService {

    /**
     * Checks if the provided list is valid and then rearrange the data into
     *  a hierarchy representing the organization of the given employees
     *
     * @param allEmployees list of all employees in the organization
     *
     * @return Employee Hierarchy if the list is valid and throws a RuntimeException otherwise
     */
    EmployeeHierarchy getEmployeeHierarchy(List<Employee> allEmployees);

    boolean isValidList(List<Employee> employees);

}
