package com.stackroute.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.springboot.exception.UserAlreadyExistsException;
import com.stackroute.springboot.model.User;
import com.stackroute.springboot.service.UserService;

@RestController
@RequestMapping("api/v1")
public class UserController {

	
	private UserService userService;
	private ResponseEntity<?> responseEntity;

	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping("user")
	public ResponseEntity<?> getUserById(@RequestParam String id){
		try {
			User user = this.userService.getUserById(id);
			this.responseEntity = new ResponseEntity<>(user,HttpStatus.OK);
		} catch (Exception e) {
			this.responseEntity = new ResponseEntity<>("Some internal error occured..", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return this.responseEntity;
	}

	@GetMapping("user")
	public ResponseEntity<?> getUserByname(@RequestParam String name){
		try {
			User user = this.userService.getUserByName(name);
			this.responseEntity = new ResponseEntity<>(user, HttpStatus.OK);
		} catch (Exception e) {
			this.responseEntity = new ResponseEntity<>("Some internal error occured..", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return this.responseEntity;
	}

	@DeleteMapping("user")
	public ResponseEntity<?> deleteUserById(@RequestParam String id){
		try {
			boolean deleted = this.userService.deleteUser(id);
			this.responseEntity = new ResponseEntity<>(deleted,HttpStatus.OK);
		} catch (Exception e) {
			this.responseEntity = new ResponseEntity<>("Some internal error occured..", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return this.responseEntity;
	}
	
	@GetMapping("users")
	public ResponseEntity<?> getAllUsers(){
		try {
			List<User> usersList = this.userService.getAllUsers();
			this.responseEntity = new ResponseEntity<>(usersList,HttpStatus.OK);
		} catch (Exception e) {
			this.responseEntity = new ResponseEntity<>("Some internal error occured..", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return this.responseEntity;
	}
	
	
}
