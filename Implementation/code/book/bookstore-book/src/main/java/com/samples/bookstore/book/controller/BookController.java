package com.samples.bookstore.book.controller;


import java.util.List;
import java.util.Map;

import com.samples.bookstore.book.bean.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.samples.bookstore.book.bean.Book;
import com.samples.bookstore.book.service.BookService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(path="/books")
public class BookController {
	
    @Autowired
    BookService bookService;
    
    @Autowired
    Environment env;
    
    @GetMapping(path="/test")
    public String test() {
    	return "Hello ! Greetings from book service";
    }
  

    @PostMapping(path="/add")
    ResponseEntity<HttpStatus> addBook(@RequestBody Map<String,Object> requestMap){  
    	
    	String email = (String) requestMap.get("email");     	
 
    	String uri = env.getProperty("USER_SVC_ROLE_URL","http://localhost:8080/user/role")+"/"+email;  //  http://localhost:8080/user/role/{abc@gmail.com}
    	
    	String result = new RestTemplate().getForObject(uri, String.class);
    	
    	if(result.equals("ADMIN")) {
		        String bookname=(String) requestMap.get("bookname");
		        String author =(String) requestMap.get("author");
		        String category = (String) requestMap.get("category");
		        double price =  Double.parseDouble(requestMap.get("price").toString());
		        int availability =(int) requestMap.get("availability");    
		    	Book book = new Book(bookname,author,category,price,availability);     	
		        if( bookService.addBook(book) > 0)
		            return ResponseEntity.status(HttpStatus.OK).build();  
		        else
		        	return ResponseEntity.status(HttpStatus.CONFLICT).build();
    	}
    	else {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    	}
        
        
    }

    
    @GetMapping(path="/list")    
    ResponseEntity<List<Book>> getbooks(){
    	return ResponseEntity.ok(bookService.getAllBooks());    			
    }
       

    @PostMapping(path="/updatequantity")   
    ResponseEntity<HttpStatus> updateQuantity(@RequestBody Map<String,Object> requestMap){   
      int count = bookService.updateQuantity((String)requestMap.get("bookId"),(int)requestMap.get("quantity"));
      if(count > 0)
    	  return ResponseEntity.status(HttpStatus.OK).build();
      else
    	  return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
    
    
    
    
    //filter need to be added

}