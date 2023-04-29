create database if not exists bookstore;

create table bookstore.users( email varchar(50) not null, name varchar(50) not null,
password varchar(50) not null, mobile varchar(50) not null, address
varchar(100) not null, city varchar(50) not null, state varchar(50) not null,
pincode varchar(10) not null, role varchar(10) not null, CONSTRAINT email_pk
PRIMARY KEY (email) );

create table bookstore.books(bookid varchar(50) PRIMARY KEY,bookname varchar(50) NOT NULL,author varchar(50) NOT NULL,category varchar(50) NOT NULL,availability int NOT NULL,price double NOT NULL);

CREATE TABLE bookstore.orders(orderid VARCHAR(50) PRIMARY KEY,emailid VARCHAR(20) NOT NULL,bookid VARCHAR(50) NOT NULL,quantity INT NOT NULL,price DOUBLE NOT NULL,orderstatus VARCHAR(30),paymentdate VARCHAR(25),paymenttype VARCHAR(20),deliverystatus VARCHAR(20));


insert into bookstore.users values('admin@kent.edu','Admin','admin@123','3301234567','KSU','KENT','OH','44240','ADMIN');         
insert into bookstore.users values('vamsi@kent.edu','Vamsi','vamsi@123','3301236547','KSU','KENT','OH','44240','USER');


insert into bookstore.books values('b1234567','Vamsi book','Vamsi','Science Fiction',10,20.5);


INSERT INTO bookstore.orders VALUES('o1587906733','vamsi@kent.edu','b1234567',2,41.0,'Purchased','26/2/2023 13:12:13','NEFT','On the way');
INSERT INTO bookstore.orders (orderid,emailid,bookid,quantity,price,orderstatus) VALUES('o1588074866','vamsi@kent.edu','b1234567',2,41.0,'Added to cart');