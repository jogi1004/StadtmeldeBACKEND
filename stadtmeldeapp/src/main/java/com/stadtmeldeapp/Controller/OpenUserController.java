package com.stadtmeldeapp.Controller;

import java.util.HashMap;
import java.util.Map;

/* import org.slf4j.Logger;
import org.slf4j.LoggerFactory; */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.FieldError;

import com.stadtmeldeapp.CustomExceptions.NotAllowedException;
import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.LoginDTO;
import com.stadtmeldeapp.DTO.LoginResponseDTO;
import com.stadtmeldeapp.DTO.RegisterDTO;
import com.stadtmeldeapp.Entity.UserEntity;
import com.stadtmeldeapp.service.EmailSenderService;

import com.stadtmeldeapp.service.JwtService;
import com.stadtmeldeapp.service.UserDetailsServiceImpl;
import com.stadtmeldeapp.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class OpenUserController {

  // Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserService userService;

  @Autowired
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private EmailSenderService emailSenderService;

  @PostMapping("/register")
  public ResponseEntity<Void> registerUser(@RequestBody @Valid RegisterDTO registerDto)
      throws NotFoundException, NotAllowedException, MessagingException {
    UserEntity newUser = userService.register(registerDto);
    emailSenderService.sendWelcomeEmail(newUser.getEmail(), newUser.getUsername());
    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
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