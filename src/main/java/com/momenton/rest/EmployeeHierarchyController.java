package com.momenton.rest;

import com.momenton.model.Employee;
import com.momenton.model.EmployeeHierarchy;
import com.momenton.service.EmployeeHierarchyService;
import com.momenton.util.EmployeeHierarchyFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class EmployeeHierarchyController {

    @Autowired
    private EmployeeHierarchyService employeeHierarchyService;

    /**
     * REST web service
     *
     * Accepts a list of all employees in a specific organization and returns a hierarchical object
     *  representation of the organization starting with the CEO
     *
     * @param allEmployees list of all employees
     *
     * @return EmployeeHierarchy that represents the passed employees
     */
    @RequestMapping(value = "/getEmployeeHierarchy", method = POST)
    public @ResponseBody
    EmployeeHierarchy getHierarchyAsObject(@RequestBody List<Employee> allEmployees) {
        System.out.println("Web Service \"/getEmployeeHierarchy\" called.");
        EmployeeHierarchy hierarchy = employeeHierarchyService.getEmployeeHierarchy(allEmployees);
        //log to console
        System.out.println(EmployeeHierarchyFormatter.getTextRepresentation(hierarchy));
        return hierarchy;
    }

    /**
     * REST web service
     *
     * Accepts a list of all employees in a specific organization and returns HTML table that represents
     *  the hierarchy of the organization starting with the CEO
     *
     * @param allEmployees list of all employees
     *
     * @return html snippet of a table that represents the employees organizational hierarchy
     */
    @RequestMapping(value = "/getEmployeeHierarchyAsHTML", method = POST)
    public String getHierarchyAsHTML(@RequestBody List<Employee> allEmployees) {
        System.out.println("Web Service \"/getEmployeeHierarchyAsHTML\" called.");
        EmployeeHierarchy hierarchy = employeeHierarchyService.getEmployeeHierarchy(allEmployees);
        //log to console
        System.out.println(EmployeeHierarchyFormatter.getTextRepresentation(hierarchy));
        return EmployeeHierarchyFormatter.getHTMLRepresentation(hierarchy);
    }

}
