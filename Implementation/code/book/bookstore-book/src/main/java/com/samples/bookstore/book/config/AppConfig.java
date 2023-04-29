package com.samples.bookstore.book.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {
	
    @Autowired
    private Environment env;    
  
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource)    {
        return new JdbcTemplate(dataSource);
    }	
 
    @Bean
    public DataSource dataSource() {
		      
       System.out.println("Driver = "+env.getProperty("BS_BOOK_DB_DRIVER","null"));
       System.out.println("URL = "+env.getProperty("BS_BOOK_DB_URL","null"));
       System.out.println("User  = "+env.getProperty("BS_BOOK_DB_USERNAME","null"));
       System.out.println("Pass  = "+env.getProperty("BS_BOOK_DB_PASSWORD","null"));

       System.out.println("Role URL  = "+env.getProperty("USER_SVC_ROLE_URL","null"));
 
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(env.getProperty("BS_BOOK_DB_DRIVER","org.h2.Driver"));
        dataSourceBuilder.url(env.getProperty("BS_BOOK_DB_URL","jdbc:h2:mem:testdb"));
        dataSourceBuilder.username(env.getProperty("BS_BOOK_DB_USERNAME","sa"));
        dataSourceBuilder.password(env.getProperty("BS_BOOK_DB_PASSWORD",""));
        return dataSourceBuilder.build();
        
    } 
    
}



