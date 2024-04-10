package com.stadtmeldeapp.Controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stadtmeldeapp.DTO.LoginDataDTO;
import com.stadtmeldeapp.service.CategoryService;
import com.stadtmeldeapp.service.JwtService;
import com.stadtmeldeapp.service.ReportingLocationService;
import com.stadtmeldeapp.service.UserDetailsServiceImpl;
import com.stadtmeldeapp.Entity.MaincategoryEntity;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebsiteController {
    
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ReportingLocationService reportingLocationService;

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        model.addAttribute("token", session.getAttribute("token"));
        return "index"; // This maps to index.html inside src/main/resources/templates
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("LoginDataDTO", new LoginDataDTO());

        return "login";
    }
    
    @PostMapping("/loginWebsite")
    public String loginWebsite(@ModelAttribute("LoginDataDTO") LoginDataDTO request, RedirectAttributes redirectAttributes, HttpSession session) {
      logger.info("LOGIN");
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      logger.info("User " + request.getUsername() + " authenticated.");
      String token = jwtService.generateToken(userDetailsServiceImpl.loadUserByUsername(request.getUsername()));
      redirectAttributes.addFlashAttribute("User", request.getUsername());
      session.setAttribute("token", token);
      session.setAttribute("test", "test");
      return "redirect:/";
    }

    @GetMapping("/mainWebsite/{id}")
    public String getMainCategoryById(@PathVariable("id") int id, RedirectAttributes redirectAttributes,HttpSession session) {
      logger.info("GET MAIN CATEGORY BY ID"); 
      List<MaincategoryEntity> mainCategory = categoryService.getAllMainCategories();
      // mainCategory = categoryService.getMainCategoryById(id);
        if (mainCategory != null) {
          logger.info("Main Category found."+ mainCategory.toString());
          redirectAttributes.addFlashAttribute("MainCategories", mainCategory);
          return "redirect:/"; 
        }
        logger.info("Main Category not found.");
        return "redirect:/";
    }
    
}
