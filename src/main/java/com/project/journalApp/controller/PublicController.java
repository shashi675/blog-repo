package com.project.journalApp.controller;

import com.project.journalApp.entity.User;
import com.project.journalApp.service.UserService;
import com.project.journalApp.utils.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class PublicController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ok";
    }


    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            userService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
//            first authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUserName(), user.getPassword()));

//            get the user to generate the token
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String generatedToken = jwtUtil.generateToken(userDetails.getUsername());

            return new ResponseEntity<>(generatedToken, HttpStatus.OK);
        } catch (Exception e) {
            log.error("exception occurred while login", e);
            return new ResponseEntity<>("incorrect username or password", HttpStatus.BAD_REQUEST);
        }
    }
}
