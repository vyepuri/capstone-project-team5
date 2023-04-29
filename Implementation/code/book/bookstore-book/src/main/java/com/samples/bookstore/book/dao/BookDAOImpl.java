package com.samples.bookstore.book.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.samples.bookstore.book.bean.Book;

@Component
public class BookDAOImpl implements BookDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public int addBook(Book book) {       
        return jdbcTemplate.update("insert into books(bookid,bookname,author,category,availability,price) values(?,?,?,?,?,?)",
            new Object[]{book.getBookid(),book.getBookname(),book.getAuthor(),book.getCategory(),book.getAvailability(),book.getPrice()});
    }

    @Override
    public List<Book> getAllBooks() {
        return jdbcTemplate.query("select bookid,bookname,author,category,availability,price from books",new BeanPropertyRowMapper<>(Book.class));       
    }
    

    @Override
    public int updateQuantity(String bookId,int quantity) {
    	String query ="update books set availability = availability - "+quantity+" where bookid=?";
        return jdbcTemplate.update(query,new Object[]{bookId});
    }
    
}
