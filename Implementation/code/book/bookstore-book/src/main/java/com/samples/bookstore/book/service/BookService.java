package com.samples.bookstore.book.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samples.bookstore.book.bean.Book;
import com.samples.bookstore.book.dao.BookDAO;

@Service
public class BookService {

    @Autowired
    BookDAO bookDAO;

    public int addBook(Book book){
        Book newBook=new Book(); 
        String bookid="b"+UUID.randomUUID().toString();
        newBook.setBookid(bookid);
        newBook.setBookname(book.getBookname());
        newBook.setAuthor(book.getAuthor());
        newBook.setCategory(book.getCategory());
        newBook.setAvailability(book.getAvailability());
        newBook.setPrice(book.getPrice());

        int count =  bookDAO.addBook(newBook);
        return count;
    }
    
    public List<Book> getAllBooks(){
        return bookDAO.getAllBooks();
    }
    
    public int updateQuantity(String bookId,int quantity){       
        int update=bookDAO.updateQuantity(bookId,quantity);
        return update;
    }

}
