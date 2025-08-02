package com.codegym.project_module_5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.codegym.project_module_5")
public class ProjectModule5Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectModule5Application.class, args);
    }

}
