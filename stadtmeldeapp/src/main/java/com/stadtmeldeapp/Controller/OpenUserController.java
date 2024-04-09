package com.stadtmeldeapp.Controller;

import java.net.http.HttpClient.Redirect;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.validation.FieldError;

import com.stadtmeldeapp.DTO.LoginDTO;
import com.stadtmeldeapp.DTO.LoginDataDTO;
import com.stadtmeldeapp.DTO.LoginResponseDTO;
import com.stadtmeldeapp.DTO.RegisterDTO;
import com.stadtmeldeapp.Entity.UserEntity;
import com.stadtmeldeapp.service.JwtService;
import com.stadtmeldeapp.service.UserDetailsServiceImpl;
import com.stadtmeldeapp.service.UserService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/auth")
public class OpenUserController {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;

  @Autowired
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterDTO registerDto) {
    logger.info("REGISTER");
    UserEntity newUser = userService.register(registerDto);
    if (newUser == null) {
      logger.info("Creating user " + registerDto.username() + " failed. Username already exists.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(String.format("Nutzer mit Nutzernamen '%s' existiert bereits.", registerDto.username()));
    }
    logger.info("New user " + registerDto.username() + " created.");
    return ResponseEntity.ok().body(newUser);
  }

  @PostMapping(value = "/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO request) {
    logger.info("LOGIN");
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
    logger.info("User " + request.username() + " authenticated.");
    String token = jwtService.generateToken(userDetailsServiceImpl.loadUserByUsername(request.username()));
    return ResponseEntity.ok(new LoginResponseDTO(request.username(), token));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

}