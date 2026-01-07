package com.project.journalApp.service;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.project.journalApp.entity.User;
import com.project.journalApp.repository.UserRepository;

@Component
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	public void saveNewUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList("USER"));
		userRepository.save(user);
	}

	public void saveExistingUser(User user) {
		userRepository.save(user);
	}
	
	public List<User> getAll() {
		return userRepository.findAll();
	}

	public User getByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}
	
	public void deleteById(ObjectId id) {
		userRepository.deleteById(id);;
	}

    public void deleteByUserName(String userName) {
		userRepository.deleteByUserName(userName);
    }

	public User findByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}
}