package com.epam.wca.gym.controller;

import com.epam.wca.gym.dto.UserUpdateDTO;
import com.epam.wca.gym.entity.User;
import com.epam.wca.gym.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    @NonNull
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<String> getUserInfo(HttpServletRequest request) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        return ResponseEntity.ok(authenticatedUser.getUserName());
    }

    @PutMapping("/change/password")
    public ResponseEntity<String> changeUserPassword(
            @RequestBody @Valid UserUpdateDTO userUpdateDTO,
            HttpServletRequest request
    ) {
        User authenticatedUser = (User) request.getAttribute("authenticatedUser");

        if (!authenticatedUser.getUserName().equals(userUpdateDTO.username()) ||
            !authenticatedUser.getPassword().equals(userUpdateDTO.oldPassword())) {
            // authenticated as other user and trying to change password details of other user
            return new ResponseEntity<>("Not authorised", HttpStatus.UNAUTHORIZED);
        }

        authenticatedUser.setPassword(userUpdateDTO.newPassword());
        userService.update(authenticatedUser);

        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }
}
