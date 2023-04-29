package com.samples.bookstore.book.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.samples.bookstore.book.bean.Book;

@Repository
public interface BookDAO {

    int addBook(Book book);
    List<Book> getAllBooks();
    int updateQuantity(String bookId,int quantity);
}
