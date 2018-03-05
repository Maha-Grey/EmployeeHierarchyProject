package com.momenton.model;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeHierarchyModelTest {

    @Test
    public void testNullEmployeeId() {
        // check if the model allows null id
        assertThrows(Exception.class, () -> new Employee("name", null));
        assertThrows(Exception.class, () -> new Employee("name", null, 150));
        assertThrows(Exception.class, () -> new Employee().setEmployeeId(null));
        assertNotNull(new Employee().getEmployeeId());
    }

    @Test
    public void testNullEmployeeName() {
        // check if the model allows null name
        assertThrows(Exception.class, () -> new Employee(null, 100));
        assertThrows(Exception.class, () -> new Employee(null, 100, 150));
        assertThrows(Exception.class, () -> new Employee().setName(null));
        assertNotNull(new Employee().getName());
    }

    @Test
    public void testNullManagerId() {
        //Confirm that the model accepts null manager Id
        Employee employeeNoManager = new Employee("name", 100);
        assertNull(employeeNoManager.getManagerId());

        employeeNoManager = new Employee("name", 100, null);
        assertNull(employeeNoManager.getManagerId());

        employeeNoManager = new Employee();
        employeeNoManager.setManagerId(null);
        assertNull(employeeNoManager.getManagerId());

        assertNull(new Employee().getManagerId());
    }

}
