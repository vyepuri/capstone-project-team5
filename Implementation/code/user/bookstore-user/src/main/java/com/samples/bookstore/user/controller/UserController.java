package com.samples.bookstore.user.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samples.bookstore.user.bean.User;
import com.samples.bookstore.user.service.UserService;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	
	@GetMapping(value = "/test")
	public String test() {
		return "Hello! It's working";
	}
	
	@GetMapping(value = "/test-lb")
	public String testLb() {
		String ip,hostname;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		return "Response from : "+ ip + " - "+hostname;
	}
	
	
	@PostMapping(value = "/add")
	public ResponseEntity<HttpStatus> addUser(@RequestBody User user){		
		if(userService.addUser(user) > 0)
		  return ResponseEntity.status(HttpStatus.OK).build();
		else
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<MappingJacksonValue> authenticateUser(@RequestBody User user){	
		
		MappingJacksonValue mapping =  userService.authenticateUser(user);
		
		if(mapping == null) 
		  return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		else
			return ResponseEntity.ok(mapping);
	}
	
	
	@GetMapping(value = "/role/{email}")
	public ResponseEntity<String> getRole(@PathVariable String email){
		
				
		String role =  userService.getUserRole(email);		
		if(role == null) 
		  return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		else
			return ResponseEntity.ok(role);
	}
	
	

}
