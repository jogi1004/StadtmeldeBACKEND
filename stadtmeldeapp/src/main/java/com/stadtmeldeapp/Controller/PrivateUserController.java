package com.stadtmeldeapp.Controller;

/* import org.slf4j.Logger;
import org.slf4j.LoggerFactory; */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stadtmeldeapp.CustomExceptions.NotFoundException;
import com.stadtmeldeapp.DTO.ProfilePictureDTO;
import com.stadtmeldeapp.DTO.UserInfoDTO;
import com.stadtmeldeapp.DTO.UserInfoNoProfilePictureDTO;
import com.stadtmeldeapp.Entity.ProfilePictureEntity;
import com.stadtmeldeapp.Entity.UserEntity;
import com.stadtmeldeapp.service.ProfilePictureService;
import com.stadtmeldeapp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class PrivateUserController {

    // Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private ProfilePictureService profilePictureService;

    @GetMapping("/info")
    public ResponseEntity<UserInfoNoProfilePictureDTO> getUserInfo(HttpServletRequest request)
            throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserInfoNoProfilePictureDTO userInfoDTO = userService.getUserInfoByUsername(username);
        return ResponseEntity.ok(userInfoDTO);
    }

    @GetMapping("/infoprofilepic")
    public ResponseEntity<UserInfoDTO> getUserInfoWithProfilePicture(HttpServletRequest request)
            throws NotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserInfoDTO userInfoDTO = userService.getUserInfoByUsernameWithProfilePicture(username);
        return ResponseEntity.ok(userInfoDTO);
    }

    @PutMapping("/addProfilePicture")
    public ResponseEntity<UserEntity> addProfilePicture(@RequestBody ProfilePictureDTO ppDto,
            HttpServletRequest request)
            throws NotFoundException {
        userService.updateProfilePicture(ppDto.profilePicture(), request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/profilePicture/{profilePictureId}")
    public ResponseEntity<ProfilePictureEntity> getProfilePicture(@PathVariable int profilePictureId)
            throws NotFoundException {
        ProfilePictureEntity profilePictureEntity = profilePictureService.getProfilePictureById(profilePictureId);
        return new ResponseEntity<>(profilePictureEntity, HttpStatus.OK);
    }

    @PutMapping("/notifications")
    public ResponseEntity<Void> changeNotifications(@RequestBody boolean notificationsEnabled,
            HttpServletRequest request) throws NotFoundException {
        userService.updateNotificationsEnabled(notificationsEnabled, request);
        return ResponseEntity.ok().build();
    }
}