package com.project.journalApp.controller;

import com.project.journalApp.entity.User;
import com.project.journalApp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class PublicController {
    private UserService userService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }


    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
