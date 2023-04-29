package com.samples.bookstore.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.samples.bookstore.user.bean.User;

@Repository
public class UserDao {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int addUser(User user) {		
		
		int count =  jdbcTemplate.queryForObject("select count(*) from users where email=?", new Object[] { user.getEmail() }, Integer.class);
		if(count > 0) 
			return 0;		
		else 
			return  jdbcTemplate.update("insert into users values(?,?,?,?,?,?,?,?,?)",new Object[]{user.getEmail(),user.getName(),user.getPassword(),user.getMobile(),user.getAddress(),user.getCity(),user.getState(),user.getPincode(),user.getRole()});
				
	}

	public User authenticateUser(User user) {
		try {
		  return (User )jdbcTemplate.queryForObject("select * from users where email=? and password=?", new Object[] { user.getEmail(),user.getPassword() }, new BeanPropertyRowMapper(User.class));
		}
		catch(Exception e) {
			return null;
		}
	}
	
     public String getUserRole(String email) {		
		
    	 try {
    	   return jdbcTemplate.queryForObject("select role from users where email=?", new Object[] { email }, String.class);	
    	 }catch(EmptyResultDataAccessException e) {
    		 return null;
    	 }
	}
	
	
	
}
