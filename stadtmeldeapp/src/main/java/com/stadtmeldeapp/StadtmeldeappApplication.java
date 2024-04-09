package com.stadtmeldeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.stadtmeldeapp.DTO.LoginDTO;
import com.stadtmeldeapp.DTO.LoginDataDTO;
import com.stadtmeldeapp.DTO.LoginResponseDTO;


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

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("LoginDataDTO", new LoginDataDTO());
        return "login";
    }

    
    
}


