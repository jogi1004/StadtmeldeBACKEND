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

import com.stadtmeldeapp.DTO.LoginDataDTO;
import com.stadtmeldeapp.DTO.LoginResponseDTO;
import com.stadtmeldeapp.service.JwtService;
import com.stadtmeldeapp.service.UserDetailsServiceImpl;
import com.stadtmeldeapp.service.UserService;

@Controller
public class WebsiteController {
    
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;
    
    @PostMapping(value = "/loginWebsite")
    public String loginWebsite(@ModelAttribute("LoginDataDTO") LoginDataDTO request, Model model) {
      logger.info("LOGIN");
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      logger.info("User " + request.getUsername() + " authenticated.");
      String token = jwtService.generateToken(userDetailsServiceImpl.loadUserByUsername(request.getUsername()));
      model.addAttribute("User", request.getUsername());
      return "redirect:/";
    }
}
