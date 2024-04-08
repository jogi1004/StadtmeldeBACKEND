package com.stadtmeldeapp.Controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stadtmeldeapp.DTO.UserInfoDTO;
import com.stadtmeldeapp.Entity.UserEntity;
import com.stadtmeldeapp.service.UserDetailsServiceImpl;
import com.stadtmeldeapp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/user")
public class PrivateUserController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfo(HttpServletRequest request) {

        logger.info("INFO");
        String headerUsername = request.getHeader("Username");

        UserInfoDTO userInfoDTO = userService.getUserInfoByUsername(headerUsername);

        if (userInfoDTO == null) {
            logger.info("User " + headerUsername + " not found.");
            // Wenn Nutzer im Header nicht exisitiert
            return ResponseEntity.notFound().build();
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(headerUsername);

        if (!userService.validate(request.getHeader("Authorization"), userDetails)) {
            logger.info(headerUsername + " does not match JWT");
            // Wenn JWT in Kombination mit Nutzer nicht gültig ist
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        logger.info("Return user info for " + headerUsername);
        return ResponseEntity.ok(userInfoDTO);
    }

    @PutMapping("/addProfilePicture")
    public ResponseEntity<UserEntity> addProfilePicture(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        logger.info("PROFILEPICTURE");

        String headerUsername = request.getHeader("Username");

        UserEntity userEntity = userService.getUserEntityByUsername(headerUsername);

        if (userEntity == null) {
            logger.info("User " + headerUsername + " not found.");
            // Wenn Nutzer im Header nicht exisitiert
            return ResponseEntity.notFound().build();
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(headerUsername);
        if (!userService.validate(request.getHeader("Authorization"), userDetails)) {
            logger.info(headerUsername + " does not match JWT");
            // Wenn JWT in Kombination mit Nutzer nicht gültig ist
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            userEntity.setProfilePicture(file.getBytes());
            userService.updateUser(userEntity);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        return ResponseEntity.internalServerError().build();
        }
    }
}