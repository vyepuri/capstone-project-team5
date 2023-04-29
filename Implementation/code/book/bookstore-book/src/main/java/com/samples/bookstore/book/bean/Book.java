package com.samples.bookstore.book.bean;


public class Book {

    String bookid; 
	String bookname;
    String author;
    String category;
    double price;
    int availability;
    
    
    public Book() {
		super();
		
	}


	public Book(String bookname, String author, String category, double price, int availability) {
		super();
		this.bookname = bookname;
		this.author = author;
		this.category = category;
		this.price = price;
		this.availability = availability;
	}
    

    public String getBookid() { return bookid;   }

    public void setBookid(String bookid) { this.bookid = bookid;  }

    public String getBookname() { return bookname;  }

    public void setBookname(String bookname) { this.bookname = bookname;  }

    public String getAuthor() { return author;   }

    public void setAuthor(String author) { this.author = author;  }

    public String getCategory() { return category;   }

    public void setCategory(String category) { this.category = category;  }

    public double getPrice() { return price;  }

    public void setPrice(double price) { this.price = price;  }

    public int getAvailability() { return availability;  }

    public void setAvailability(int availability) { this.availability = availability;  }




}
