package com.momenton.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.momenton.model.Employee;
import com.momenton.model.EmployeeHierarchy;
import com.momenton.model.Manager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeHierarchyServiceTest {

    @Autowired
    private EmployeeHierarchyService employeeHierarchyService;

    private List<Employee> allEmployees = new ArrayList<>();

    @Test
    public void testInvalidEmployeeId() {
        // Employee Id is zero
        readFile("/invalid/ZeroEmployeeId.json");
        assertFalse(employeeHierarchyService.isValidList(allEmployees));

        // Employee Id is a negative number
        readFile("/invalid/NegativeEmployeeId.json");
        assertFalse(employeeHierarchyService.isValidList(allEmployees));
    }

    @Test
    public void testInvalidEmployeeName() {
        // Empty String as an employee name
        readFile("/invalid/InvalidEmployeeName.json");
        assertFalse(employeeHierarchyService.isValidList(allEmployees));
    }

    @Test
    public void testInvalidManagerId() {
        // Manager Id that is not in the Employee Ids
        readFile("/invalid/InvalidManagerId.json");
        assertFalse(employeeHierarchyService.isValidList(allEmployees));
    }

    @Test
    public void testEmployeeIdSameAsManagerId() {
        // Manager Id is the same as the Employee Id for the same employee
        readFile("/invalid/EmployeeHisOwnManager.json");
        assertFalse(employeeHierarchyService.isValidList(allEmployees));
    }

    @Test
    public void testTwoEmployeesWithNoManager() {
        // Two or more employees who are not assigned a manager
        readFile("/invalid/TwoEmployeesWithNoManager.json");
        assertFalse(employeeHierarchyService.isValidList(allEmployees));
    }

    @Test
    public void testAllEmployeesHaveManagers() {
        // All employees are assigned a manager (No CEO)
        readFile("/invalid/AllEmployeesHaveManagers.json");
        assertFalse(employeeHierarchyService.isValidList(allEmployees));
    }

    @Test
    public void testEmployeeIdUnique() {
        // Two or more employees with the same employee id
        readFile("/invalid/TwoEmployeesWithSameId.json");
        assertFalse(employeeHierarchyService.isValidList(allEmployees));
    }

    @Test
    public void testCEOWithNoSubordinates() {
        // CEO who is the only one in the hierarchy
        readFile("/invalid/OnlyTheCEOInHierarchy.json");
        assertThrows(Exception.class, () ->
                employeeHierarchyService.getEmployeeHierarchy(allEmployees));
        // Other Employees reference each other and not the CEO
        readFile("/invalid/CEOWithNoSubordinates.json");
        assertThrows(Exception.class, () ->
                employeeHierarchyService.getEmployeeHierarchy(allEmployees));
    }

    @Test
    public void testEmployeesNotCoveredInHierarchy() {
        // Some employees are not covered within the hierarchy
        readFile("/invalid/EmployeesNotCoveredInHierarchy.json");
        assertThrows(Exception.class, () ->
                employeeHierarchyService.getEmployeeHierarchy(allEmployees));
    }

    @Test
    public void testHierarchyPresence() {
        // Test Correct data provided in the requirements
        readFile("/CorrectEmployeeData.json");
        EmployeeHierarchy hierarchy = employeeHierarchyService.getEmployeeHierarchy(allEmployees);

        assertNotNull(hierarchy);
    }

    @Test
    public void testHierarchyDepth() {
        // Test Correct data provided in the requirements
        readFile("/CorrectEmployeeData.json");
        EmployeeHierarchy hierarchy = employeeHierarchyService.getEmployeeHierarchy(allEmployees);

        // Confirm hierarchy depth
        assertEquals(3 , hierarchy.getDepth());
    }

    @Test
    public void testCEO() {
        // Test Correct data provided in the requirements
        readFile("/CorrectEmployeeData.json");
        EmployeeHierarchy hierarchy = employeeHierarchyService.getEmployeeHierarchy(allEmployees);

        // confirm CEO
        Manager actualCEO = hierarchy.getCEO();
        assertNotNull(actualCEO);
        Manager expectedCEO = new Manager("Jamie", 150);
        assertEquals(expectedCEO, actualCEO);

    }

    @Test
    public void testFirstLayer() {
        // Test Correct data provided in the requirements
        readFile("/CorrectEmployeeData.json");
        EmployeeHierarchy hierarchy = employeeHierarchyService.getEmployeeHierarchy(allEmployees);

        // confirm first layer exists
        List<Employee> actualFirstLayerSubordinates = hierarchy.getCEO().getSubordinates();
        assertNotNull(actualFirstLayerSubordinates);
        // the size will get confirmed in the following assertArrayEquals, but check here to fail fast
        assertEquals(actualFirstLayerSubordinates.size(), 2);

        // confirm first layer has the correct data
        Manager alan = new Manager("Alan", 100);
        Manager steve = new Manager("Steve", 400);
        Manager[] expectedFirstLayerManagers = {alan, steve};
        Manager[] actualFirstLayerManagers = new Manager[2];
        actualFirstLayerSubordinates.toArray(actualFirstLayerManagers);
        assertArrayEquals(expectedFirstLayerManagers, actualFirstLayerManagers);
    }

    @Test
    public void testSecondLayer() {
        // Test Correct data provided in the requirements
        readFile("/CorrectEmployeeData.json");
        EmployeeHierarchy hierarchy = employeeHierarchyService.getEmployeeHierarchy(allEmployees);

        //given the previous tests has already passed
        Manager actualAlan = (Manager) hierarchy.getCEO().getSubordinates().get(0);

        // confirm second layer
        // a. confirm Alan's subordinates
        assertNotNull(actualAlan.getSubordinates());
        // the size will get confirmed in the following assertArrayEquals, but check here to fail fast
        assertEquals(actualAlan.getSubordinates().size(),2);
        // confirm values
        Employee martin = new Employee("Martin", 220);
        Employee alex = new Employee("Alex", 275);
        Employee[] expectedAlanSubordinates = {martin, alex};
        Employee[] actualAlanSubordinates = new Employee[2];
        actualAlan.getSubordinates().toArray(actualAlanSubordinates);
        assertArrayEquals(expectedAlanSubordinates, actualAlanSubordinates);
        // confirm no more subordinates
        assertEquals(Employee.class , actualAlanSubordinates[0].getClass());
        assertEquals(Employee.class , actualAlanSubordinates[1].getClass());

        // b. confirm Steve's subordinates
        Manager actualSteve = (Manager) hierarchy.getCEO().getSubordinates().get(1);

        assertNotNull(actualSteve.getSubordinates());
        // the size will get confirmed in the following assertArrayEquals, but check here to fail fast
        assertEquals(actualSteve.getSubordinates().size(),1);

        // confirm values
        Employee david = new Employee("David", 190);
        Employee[] expectedSteveSubordinates = {david};
        Employee[] actualSteveSubordinates = new Employee[1];
        actualSteve.getSubordinates().toArray(actualSteveSubordinates);
        assertArrayEquals(expectedSteveSubordinates, actualSteveSubordinates);
        // confirm no more subordinates
        assertEquals(Employee.class , actualSteveSubordinates[0].getClass());

    }

    // helper method that reads a given json file and fill the data into the allEmployees list to be used in tests
    private void readFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            allEmployees = mapper.readValue(System.class.getResource(fileName),
                    mapper.getTypeFactory().constructCollectionType(List.class, Employee.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}