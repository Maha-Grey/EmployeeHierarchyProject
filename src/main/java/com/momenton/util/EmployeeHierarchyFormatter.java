package com.momenton.util;

import com.momenton.model.Employee;
import com.momenton.model.EmployeeHierarchy;
import com.momenton.model.Manager;

import java.util.List;

public class EmployeeHierarchyFormatter {

    private static String emptyItem;
    private static String startLine;
    private static String endLine;
    private static String openItem;
    private static String closeItem;

    // Add border and padding to the table. Just for Testing purposes.
    // Should't exist in production environment to allow the caller to use their own styling
    private static final String HTML_TABLE_STYLE = "<style>\n" +
            "table, td {\n" +
            "   border: 1px solid;\n" +
            "   border-collapse: collapse;\n" +
            "   padding: 4px;\n" +
            "}\n" +
            "</style>\n";

    // To Prevent Creating instances as utility classes should only be called in a static manner
    private EmployeeHierarchyFormatter() {
    }

    /**
     * return an html table snippet that represents the hierarchy of the employees in the organization
     *
     * @param hierarchy the organizational hierarchy object
     * @return styled html table representing the hierarchy
     */
    public static String getHTMLRepresentation(EmployeeHierarchy hierarchy) {
        assignStringValues(DisplayType.HTML);
        StringBuilder htmlString = new StringBuilder(HTML_TABLE_STYLE);
        htmlString.append("EmployeeHierarchy: <br/>");
        // First Row containing the CEO
        htmlString.append("<table>").append(startLine).append(openItem)
                .append(hierarchy.getCEO().getName()).append(closeItem);
        int depth = hierarchy.getDepth();
        // add empty cells at the end of the row
        for (int column = 1; column < depth; column++) {
            htmlString.append(emptyItem);
        }
        htmlString.append(endLine);
        // Get the rest of the table rows
        htmlString.append(getSubordinateString(1, depth, hierarchy.getCEO().getSubordinates()));
        // close table
        htmlString.append("</table>");
        return htmlString.toString();
    }


    /**
     * return a table-like text that represents the hierarchy of the employees in the organization
     *
     * @param hierarchy the organizational hierarchy object
     * @return styled table-like text representing the hierarchy
     */
    public static String getTextRepresentation(EmployeeHierarchy hierarchy) {
        assignStringValues(DisplayType.TEXT);
        StringBuilder hierarchicalTextDisplay = new StringBuilder("\nEmployeeHierarchy:\n");
        // first row
        hierarchicalTextDisplay.append(openItem).append(hierarchy.getCEO().getName())
                .append(closeItem).append(endLine); // No need to add empty items at the end
        // get the rest of the table
        hierarchicalTextDisplay.append(getSubordinateString(1, hierarchy.getDepth(), hierarchy.getCEO().getSubordinates()));
        return hierarchicalTextDisplay.toString();
    }

    /**
     * Recursive method to go through the employees and create the appropriate representation according to the DisplayType
     *
     * @param level The level of the current iteration (CEO is level 0 Their subordinates are level 1 and so on)
     * @param depth The depth of the hierarchy
     * @param subordinates List of subordinate employees
     *
     * @return Subordinates formatted according to the display type.
     */
    private static String getSubordinateString (int level, int depth, List<Employee> subordinates) {
        StringBuilder subordinateString = new StringBuilder();
        for (Employee employee: subordinates) {
            subordinateString.append(startLine);
            // add preceding empty cells
            for (int colBefore = 0; colBefore < level; colBefore++) {
                subordinateString.append(emptyItem);
            }
            // add the current employee
            subordinateString.append(openItem).append(employee.getName()).append(closeItem);
            // add following empty cells
            for (int colAfter = level + 1; colAfter < depth; colAfter++) {
                subordinateString.append(emptyItem);
            }
            subordinateString.append(endLine);
            if (employee.getClass().equals(Manager.class)) {
                Manager manager = (Manager) employee;
                // add lines/ rows for each of the subordinates
                subordinateString.append(getSubordinateString(level + 1, depth,  manager.getSubordinates()));
            }
        }
        return subordinateString.toString();
    }

    /**
     * Assign the strings used in creating the formatted hierarchy according to the given display type
     *
     * @param displayType Display type of the requested representation (Currently Text or HTML)
     */
    private static void assignStringValues(DisplayType displayType) {
        switch (displayType) {
            case HTML:
                startLine = "<tr>";
                emptyItem = "<td></td>";
                openItem = "<td>";
                closeItem = "</td>";
                endLine = "</tr>";
                break;
            case TEXT:
                startLine = "";
                emptyItem = "\t\t|";
                openItem = "";
                closeItem = "\t|";
                endLine = "\n";
        }
    }

    private enum DisplayType {
        HTML,
        TEXT;
    }

}
