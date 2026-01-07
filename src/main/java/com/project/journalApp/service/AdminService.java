package com.project.journalApp.service;

import com.project.journalApp.entity.User;
import com.project.journalApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    public void saveNewAdmin(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("ADMIN"));
        userRepository.save(user);
    }
}
