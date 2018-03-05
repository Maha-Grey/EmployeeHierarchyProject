# EmployeeHierarchyProject

### Used Technologies, Frameworks, and Tools:

- Java 8
- Spring Boot
- REST Web Services
- JUnit
- Jackson
- Postman

### Assumptions and Notes

- Employee model will not accept null values for Employee Name or Employee Id.
- Employee model will accept null values for Manager Id.
- A RuntimeException will be thrown if the provided employee list is not valid.
- If conversion succeeeded, the REST Web Services will log a textual hierarcical representation on the console.
- The Json used in testing the web service can be found under /src/test/resources/CorrectEmployeeData.json


### When an employee list is considered invalid

- If an Employee Id is not unique across the list
- If an Employee Id is null, 0, or negative number.
- If an Employee Name is null or an empty string.
- If a Manager Id is not included in the given Employee Ids.
- If there is more than one employee with no Manager Id (More than one CEO).
- If there are no employees without a Manager Id (No CEO).
- If one employee is assigned a Manager Id that is the same as his own Employee Id.
- If CEO has no subordinates.
- If There are some employees who are not under the hierarchy (Cyclic reference to each other instead oh hierarcical reference leading to the CEO).




