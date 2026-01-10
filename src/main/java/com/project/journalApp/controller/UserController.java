package com.project.journalApp.controller;

import com.project.journalApp.api.response.WeatherResponse;
import com.project.journalApp.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.project.journalApp.entity.User;
import com.project.journalApp.service.UserService;


@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
		
	private UserService userService;
	private WeatherService weatherService;


//	create user method is in public controller


	@GetMapping("/greet")
	public ResponseEntity<?> greeting() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();

		WeatherResponse weatherResponse = weatherService.getWeather("delhi");
		String greet = "";
		if(weatherResponse != null) {
			greet = ", it feels like " + weatherResponse.getMain() .getFeelsLike()+ " degrees temp.";
			return ResponseEntity.ok("hi " + userName + greet);

		}
		return ResponseEntity.internalServerError().build();
	}

	@PutMapping()
	public ResponseEntity<?> updateUser(@RequestBody User user) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();

		User userInDB = userService.getByUserName(userName);
		if(userInDB != null) {
			userInDB.setUserName(user.getUserName());
			userInDB.setPassword(user.getPassword());
			userService.saveNewUser(userInDB);
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping
	public ResponseEntity<?> deleteUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();

		userService.deleteByUserName(userName);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
