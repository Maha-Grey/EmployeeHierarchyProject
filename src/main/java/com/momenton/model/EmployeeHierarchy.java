package com.momenton.model;

import java.util.List;
import java.util.Objects;

public class EmployeeHierarchy {

    private Manager cEO;

    public EmployeeHierarchy(Manager cEO) {
        Objects.requireNonNull(cEO);
        this.cEO = cEO;
    }

    public Manager getCEO() {
        return cEO;
    }

    public void setCEO(Manager cEO) {
        Objects.requireNonNull(cEO);
        this.cEO = cEO;
    }

    /**
     * Get the depth of this hierarchy object
     *
     * @return number of layers in the current hierarchy
     */
    public int getDepth() {
        return getDepth(cEO.getSubordinates(),1);
    }

    /**
     * recursive method to get the depth of a given employee list
     *
     * @param employees list of employees to get depth for
     * @param depth current list's depth
     *
     * @return the maximum depth of the provided list
     */
    private int getDepth(List<Employee> employees, int depth) {
        depth++;
        int maxDepth = depth;
        for (Employee employee: employees) {
            if (employee.getClass().equals(Manager.class)) {
                Manager manager = (Manager) employee;
                maxDepth = getDepth(manager.getSubordinates(), depth);
                if (depth > maxDepth)
                    maxDepth = depth;
            }
        }
        return maxDepth;
    }

    @Override
    public String toString() {
        return "EmployeeHierarchy{" +
                "cEO=" + cEO +
                "}" ;
    }

}
