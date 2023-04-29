package com.samples.bookstore.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.samples.bookstore.user.bean.User;
import com.samples.bookstore.user.controller.UserController;
import com.samples.bookstore.user.service.UserService;


@SpringBootTest
class UserApplicationTests {
	
	@Mock
    UserService userService;
	
	@InjectMocks
	UserController userController = new UserController();
	
    
	
	@Test	
	void contextLoads() {
	}
	
	@Test
	public void test() {
		String response = userController.test();
		assertEquals("Hello! It's working", response);
	}
	
	@Test
	public void addUserTest() {
		User user = new User();
		user.setName("testuser");
		user.setPassword("testpassword");
		user.setEmail("testuser@test.com");
		user.setRole("ADMIN");
		user.setMobile("9123456789");
		user.setAddress("Test Address");
		user.setCity("Test city");
		user.setState("Test State");
		user.setPincode("123456");
		Mockito.when(userService.addUser(user)).thenReturn(1);
		ResponseEntity<HttpStatus> response = userController.addUser(user);		
		assertEquals(200,response.getStatusCodeValue());
	}
	@Test
	public void addUserTestForExistingUser() {
		User user = new User();
		user.setName("testuser");
		user.setPassword("testpassword");
		user.setEmail("testuser@test.com");
		user.setRole("ADMIN");
		user.setMobile("9123456789");
		user.setAddress("Test Address");
		user.setCity("Test city");
		user.setState("Test State");
		user.setPincode("123456");
		Mockito.when(userService.addUser(user)).thenReturn(0);
		ResponseEntity<HttpStatus> response = userController.addUser(user);		
		assertEquals(409,response.getStatusCodeValue());
	}
	@Test
	public void loginTestFailure() {
		User user = new User();		
		user.setPassword("testpassword");
		user.setEmail("testuser@test.com");		
		Mockito.when(userService.authenticateUser(user)).thenReturn(null);
		ResponseEntity<MappingJacksonValue> response = userController.authenticateUser(user);		
		assertEquals(404,response.getStatusCodeValue());
	}
	@Test
	public void loginTestPass() {
		User user = new User();		
		user.setPassword("testpassword");
		user.setEmail("testuser@test.com");		
		Mockito.when(userService.authenticateUser(user)).thenReturn(new MappingJacksonValue(user));
		ResponseEntity<MappingJacksonValue> response = userController.authenticateUser(user);		
		assertEquals(200,response.getStatusCodeValue());
	}
	
	@Test
	public void existingUserRoleTest() {
		Mockito.when(userService.getUserRole("testuser@test.com")).thenReturn("USER");
		ResponseEntity<String> response = userController.getRole("testuser@test.com");		
		assertEquals(200,response.getStatusCodeValue());
	}
	@Test
	public void nonExistingUserRoleTest() {
		Mockito.when(userService.getUserRole("nouser@test.com")).thenReturn(null);
		ResponseEntity<String> response = userController.getRole("nouser@test.com");		
		assertEquals(404,response.getStatusCodeValue());
	}

	
}
