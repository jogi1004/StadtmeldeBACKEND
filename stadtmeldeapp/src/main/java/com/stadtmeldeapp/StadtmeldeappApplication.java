package com.stadtmeldeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SpringBootApplication
public class StadtmeldeappApplication {

    public static void main(String[] args) {
        SpringApplication.run(StadtmeldeappApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "index"; // This maps to index.html inside src/main/resources/templates
    }
}


