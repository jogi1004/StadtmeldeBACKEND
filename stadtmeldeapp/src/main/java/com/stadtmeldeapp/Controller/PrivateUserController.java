package com.stadtmeldeapp.Controller;

import java.io.IOException;

/* import org.slf4j.Logger;
import org.slf4j.LoggerFactory; */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.ProfilePictureDTO;
import com.stadtmeldeapp.DTO.UserInfoDTO;
import com.stadtmeldeapp.Entity.UserEntity;
import com.stadtmeldeapp.service.UserDetailsServiceImpl;
import com.stadtmeldeapp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class PrivateUserController {

    //Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping("/info")
    public ResponseEntity<UserInfoDTO> getUserInfo(HttpServletRequest request) throws NotFoundException {

        //logger.info("INFO");
        String headerUsername = request.getHeader("Username");

        UserInfoDTO userInfoDTO = userService.getUserInfoByUsername(headerUsername);

        if (userInfoDTO == null) {
            //logger.info("User " + headerUsername + " not found.");
            // Wenn Nutzer im Header nicht exisitiert
            return ResponseEntity.notFound().build();
        }

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(headerUsername);

        if (!userService.validate(request.getHeader("Authorization"), userDetails)) {
            //logger.info(headerUsername + " does not match JWT");
            // Wenn JWT in Kombination mit Nutzer nicht gültig ist
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //logger.info("Return user info for " + headerUsername);
        return ResponseEntity.ok(userInfoDTO);
    }

    @PutMapping("/addProfilePicture")
    public ResponseEntity<UserEntity> addProfilePicture(@RequestBody ProfilePictureDTO file, HttpServletRequest request) throws NotFoundException, IOException {

        //logger.info("PROFILEPICTURE");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userEntity = userService.getUserEntityByUsername(username);
            //logger.info("User " + headerUsername + " not found.");
            // Wenn Nutzer im Header nicht exisitiert

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        if (!userService.validate(request.getHeader("Authorization"), userDetails) || file == null) {
            //logger.info(headerUsername + " does not match JWT");
            // Wenn JWT in Kombination mit Nutzer nicht gültig ist
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
            userEntity.setProfilePicture(file.getProfilePicture());
            userService.updateUser(userEntity);
            return ResponseEntity.ok().build();
    }
}