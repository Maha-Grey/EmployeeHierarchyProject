package com.momenton;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableAutoConfiguration
@SpringBootApplication
public class EmployeeHierarchyApp {

    public static void main(String[] args) {
        // Start Spring Boot app and expose the web services
        SpringApplication.run(EmployeeHierarchyApp.class, args);
    }

}
