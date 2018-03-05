package com.momenton.service;

import com.momenton.model.Employee;
import com.momenton.model.EmployeeHierarchy;
import com.momenton.model.Manager;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MomentonEmployeeHierarchyService implements EmployeeHierarchyService {

    /**
     * Checks if the provided list is valid and then rearrange the data into
     *  a hierarchy representing the organization of the given employees
     *
     * @param allEmployees list of all employees in the organization
     *
     * @return Employee Hierarchy if the list is valid and throws a RuntimeException otherwise
     */
    public EmployeeHierarchy getEmployeeHierarchy(List<Employee> allEmployees) {
        if (!isValidList(allEmployees))
            throw new IllegalArgumentException("This Employee List is invalid.");

        //Get the CEO of the company
        //Didn't check optional.isPresent() because we already checked in
        // isValidList(allEmployees) method which is called above
        Employee cEO = allEmployees.stream().filter(e -> e.getManagerId() == null).findFirst().get();

        Set<Employee> seenEmployees = new HashSet<>();

        // a recursive method to go through all the employees and create a hierarchical representation
        cEO = fillSubordinates(cEO, allEmployees, seenEmployees);

        // some employees are not covered in the hierarchy
        if(seenEmployees.size() != allEmployees.size())
            throw new RuntimeException("Some Employees are out of the hierarchy");

        return getHierarchyObject(cEO);
    }

    /**
     *  Checks for list validity:
     *      -   Employee names are all valid values (no nulls and no empty strings)
     *      -   Employee Ids are all unique positive integers
     *      -   Exactly one employee with no manager (The CEO)
     *      -   Manager Ids are all valid (all refer to valid employee ids and not the current employee)
     *
     * @param employees list of employees to validate
     *
     * @return true if all conditions are met, false otherwise
     */
    public boolean isValidList(List<Employee> employees) {
        return isValidEmployeeNames(employees)
                && isValidEmployeeIds(employees)
                && isExactlyOneCEO(employees)
                && isValidManagerIds(employees);
    }

    /**
     * Checks that employee names are all valid values (no nulls and no empty strings)
     *
     * @param employees list of employees to check
     *
     * @return true if all names have valid values, false otherwise
     */
    private boolean isValidEmployeeNames(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.getName() == null || e.getName().isEmpty())
                .count()==0;
    }

    /**
     * Checks that employee Ids are all unique positive integers
     *
     * @param employees list of employees to check
     *
     * @return true if all ids have valid values, false otherwise
     */
    private boolean isValidEmployeeIds(List<Employee> employees) {
        //all employee Ids must be positive integer value
        if (employees.stream().mapToInt(Employee::getEmployeeId)
                .anyMatch(employeeId -> employeeId<=0))
            // if any id is <= 0 then the list is not valid
            return false;

        //EmployeeId must be unique
        long idCount = employees.stream()
                .mapToInt(Employee::getEmployeeId)
                .count();
        long idDistinctCount = employees.stream()
                    .mapToInt(Employee::getEmployeeId)
                    .distinct().count();
        // if both are equal then the ids are already unique
        return idCount == idDistinctCount;
    }

    /**
     *  Checks if there is exactly one employee with no manager (The CEO)
     *
     * @param employees list of employees to check
     *
     * @return true if exactly one CEO is found, false otherwise
     */
    private boolean isExactlyOneCEO(List<Employee> employees) {
        //check if exactly one employee doesn't have a manager (The CEO)
        return employees.stream().filter(e -> e.getManagerId() == null).count() == 1;
    }

    /**
     *  Checks if manager Ids are all valid (all refer to valid employee ids and not the current employee)
     *
     * @param employees list of employees to validate
     *
     * @return true if all conditions are met, false otherwise
     */
    private boolean isValidManagerIds(List<Employee> employees) {
        // if an employee's manager id refers to his own employee id, then this is invalid
        if (employees.stream().filter(e -> e.getEmployeeId().equals(e.getManagerId())).count()!=0)
            return false;

        List<Integer> employeeIds = employees.stream()
                .mapToInt(Employee::getEmployeeId)
                .boxed().collect(Collectors.toList());
        List<Integer> managerIds = employees.stream()
                .filter(Employee::hasManager)
                .mapToInt(Employee::getManagerId).distinct()
                .boxed().collect(Collectors.toList());
        //check if there's a manager id that is not one of the employees
        return employeeIds.containsAll(managerIds);
    }

    /**
     * A recursive method to go through all the employees and create a hierarchical representation
     *  by filling the passed manager's subordinates if any or return and employee object if it has no subordinates
     *
     * @param manager the current employee
     * @param allEmployees list of all employees to go through
     * @param seenEmployees list of employees covered so far
     *
     * @return the sent employee object cast to manager and filled with the subordinates list if any,
     *      otherwise return the same object with no change
     */
    private Employee fillSubordinates(Employee manager, List<Employee> allEmployees, Set<Employee> seenEmployees) {

        // this employee is already covered
        seenEmployees.add(manager);

        Employee processedEmployee;

        //get all employees who have this manager
        List<Employee> subordinateList = allEmployees.stream()
                .filter(employee -> manager.getEmployeeId().equals(employee.getManagerId()))
                .collect(Collectors.toList());

        if (subordinateList.size() > 0 ) { // has subordinates
            // Must use final or effectively final in lambda expression
            Set<Employee> seenEmployeesToSend = seenEmployees;
            // recursive call to fill the subordinates of the current employee if any
            List<Employee> processedSubordinates = subordinateList.stream()
                    .map(employee -> fillSubordinates(employee , allEmployees, seenEmployeesToSend))
                    // Sort employees (under this manager) by Id
                    .sorted()
                    .collect(Collectors.toList());
            processedEmployee = new Manager(manager, processedSubordinates);
        } else { // if no subordinates exist, return the same object unchanged
            processedEmployee = manager;
        }

        return processedEmployee;
    }

    /**
     * Creates a Hierarchy object using the CEO given he/she has subordinates
     *
     * @param processedCEO the employee object after filling the subordinates list
     *
     * @return EmployeeHierarchy object if applicable, throws RuntimeException otherwise
     */
    private EmployeeHierarchy getHierarchyObject(Employee processedCEO) {
        if (processedCEO.getClass()!= Manager.class) // does not have any subordinates
            throw new RuntimeException("CEO Does not have any subordinates");
        return new EmployeeHierarchy((Manager) processedCEO);
    }


}
