package com.stadtmeldeapp.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.stadtmeldeapp.DTO.LoginDataDTO;
import com.stadtmeldeapp.service.JwtService;
import com.stadtmeldeapp.service.UserDetailsServiceImpl;

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
    
}
