package com.stackroute.springboot.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.springboot.exception.UserAlreadyExistsException;
import com.stackroute.springboot.exception.UserNotFoundException;
import com.stackroute.springboot.model.User;
import com.stackroute.springboot.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("auth/v1")
public class UserAuthenticationController {
	
	private Map<String,String> map = new HashMap<>();
	private UserService userService;
	private ResponseEntity<?> responseEntity;
	

	@Autowired
	public UserAuthenticationController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping("/")
	public String serverStarted() {
		return "Authentication Server Started";
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> saveuserToDB(@RequestBody User user) throws UserAlreadyExistsException{
		try {
			User createdUser = userService.saveUser(user);
			this.responseEntity = new ResponseEntity<>(createdUser,HttpStatus.CREATED);
		} catch (UserAlreadyExistsException exception) {
			throw exception;
		}
		catch(Exception e) {
			System.out.println(e);
			this.responseEntity = new ResponseEntity<>("Some internal error occured..", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return this.responseEntity;
	}
	
	@PostMapping("login")
	public ResponseEntity<?> doLogin(@RequestBody User user){
/*
 *   json response with web token 
 *   {
 *   	"message" : "User Successfully LoggedIn"
 *   	"token" : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwicGFzc3dvcmQiOiJwYXNzd29yZCJ9.
 *                qWCsUuHsu8_Xn66bG4GJ1qhFuwRvJ43LTT3uzcC4zgY"
 *   }
 */
		try {
			String jwtToken = generateToken(user.getUsername(), user.getPassword());
			map.put("message", "User Successfully LoggedIn");
			map.put("token", jwtToken);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("message", e.getMessage());
			map.put("token", null);
			return new ResponseEntity<>(map,HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<>(map,HttpStatus.OK);
	}

	private String generateToken(String username, String password) throws ServletException, UserNotFoundException {
		String jwtToken = "";
		if(username == null || password == null) {
			throw new ServletException("Please send valid username and password");
		}
		
		//validate user aginst db
		
		boolean flag= 	userService.validateUser(username, password);
		if(!flag)
			throw new ServletException("Invalid Credentials");
		else {
			jwtToken = Jwts.builder()
					        .setSubject(username)
					        .setIssuedAt(new Date())
					        .setExpiration(new Date(System.currentTimeMillis() + 3000000))
					        .signWith(SignatureAlgorithm.HS256, "i@ieosow")
					        .compact();
		}
		
		return jwtToken;
			
		
	}
	
}
