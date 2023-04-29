package com.samples.bookstore.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.samples.bookstore.user.bean.User;
import com.samples.bookstore.user.dao.UserDao;

@Service
public class UserService {

	@Autowired
	UserDao userDao;

	
	public int addUser(User user) {
		user.setRole("USER");
		return userDao.addUser(user);
	}
	
	public MappingJacksonValue authenticateUser(User user) {
		
		User loggedInUser = userDao.authenticateUser(user);	
		
		if(loggedInUser == null || loggedInUser.getEmail() == null) 
			return null;
		else {			
			  SimpleFilterProvider filterProvider = new SimpleFilterProvider();
			  filterProvider.addFilter("loginFilter",
			  SimpleBeanPropertyFilter.filterOutAllExcept("email","name","role"));
			  MappingJacksonValue mapping = new MappingJacksonValue(loggedInUser);
			  mapping.setFilters(filterProvider);			  
			  return mapping;	
		}
	
	}

	public String getUserRole(String email) {	
		
		return userDao.getUserRole(email);	
		 
	}	
	
	
	
}
