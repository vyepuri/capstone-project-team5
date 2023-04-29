


import os
import mysql.connector
import datetime
import time
import requests as req
from flask import jsonify
import json, ast

class OrderModelClass:

##########################################################        Initialize mysql database connection        #########################################################

    def __init__(self):
        sqlUsername = os.getenv('BS_ORDER_DB_USERNAME')
        sqlPassword = os.getenv('BS_ORDER_DB_PASSWORD')
        orderDatabase = os.getenv('BS_ORDER_DBNAME')
        hostName = os.getenv('BS_ORDER_DBHOST')
        print("Connecting to mysql database....")
        self.myDbConnection = mysql.connector.connect(
                host=hostName,
                user=sqlUsername,
                passwd=sqlPassword,
                database=orderDatabase,
                auth_plugin="mysql_native_password"
        )
        print("Connection established...")





########################################################         Disconnects database connection              ########################################################

    def __del__(self):
        print("Disconnects mysql Database")
        self.myDbConnection.close()




#########################################################       Add cart details to the order table            #######################################################

    def addCart(self,params):
        try:
            print(params)
            epochTime = int(time.time())
            orderId = "o"+str(epochTime)
            emailId = params.get("emailid")
            bookId = params.get("bookid")
            quantity = params.get("quantity")
            price = float(params.get("quantity")) * float(params.get("price"))
            orderStatus = "Added to cart"
            sucessMsg = "success"
            failMsg = "fail"

            #print("orderId "+orderId)
            sqlQuery = "INSERT INTO orders (orderid,emailid,bookid,quantity,price,orderstatus) VALUES (%s,%s,%s,%s,%s,%s)"
            val = (orderId,emailId,bookId,quantity,price,orderStatus)

            myCursor = self.myDbConnection.cursor()
            myCursor.execute(sqlQuery,val)
            self.myDbConnection.commit()
            result = myCursor.rowcount
            myCursor.close()

            if result:
                print("Cart added sucessfully!!!")
                return sucessMsg
            else:
                raise ValueError(failMsg)

        except ValueError as e:
            return e

        except mysql.connector.Error as Error:
            return failMsg




###########################################################      List cart details to the user        #######################################################

    def listCart(self,params):
        try:
            print(params)
            emailId = params.get("emailid")
            orderStatus = "Added to cart"
            cartList = []
            content = {}
            sucessMsg = "No carts found"
            failMsg = "Failed to fetch cart list"

            #print("emailId "+emailId)
            sqlQuery = "select orders.orderid,orders.bookid,books.bookname,orders.quantity,orders.price from orders INNER JOIN books  ON orders.bookid = books.bookid where  orders.emailid='"+emailId+"' and orders.orderstatus='"+orderStatus+"'"

            myCursor = self.myDbConnection.cursor()
            myCursor.execute(sqlQuery)

            records = myCursor.fetchall()
            rows = myCursor.rowcount
            myCursor.close()
            print(records)
            print(rows)

            for result in records:
                content = {'orderid': result[0], 'bookid': result[1], 'bookname': result[2], 'quantity': result[3], 'price': result[4]}
                cartList.append(content)
                content = {}

            if rows:
                print("Fetched cart list successfully!!!")
                return cartList
            else:
                return sucessMsg

        except mysql.connector.Error as Error:
            return failMsg




################################################                      Delete cart in the orders table            ###################################################

    def deleteCart(self,params):
        try:
            print(params)
            emailId = params.get("emailid")
            orderId = params.get("orderid")
            sucessMsg = "success"
            failMsg = "fail"

            #print("emailId "+emailId)
            sqlQuery = "delete from orders where orderid='"+orderId+"'"

            myCursor = self.myDbConnection.cursor()
            myCursor.execute(sqlQuery)
            self.myDbConnection.commit()
            records = myCursor.rowcount
            myCursor.close()
            print(records)

            if records:
                print("Cart deleted sucessfully!!!")
                return sucessMsg
            else:
                raise ValueError(failMsg)

        except ValueError as e:
            return e

        except mysql.connector.Error as Error:
            return failMsg




####################################################              Update order status as Purchased                  #####################################

    def payCart(self,params):
        try:
            print(params)
            emailId = params.get("emailid")
            paymentType = params.get("paymenttype")
            paymentDate = params.get("paymentdate")
            books = params.get("books")
            orderStatus1 = "Purchased"
            orderStatus2 = "Added to cart"
            deliveryStatus = "On the way"
            sucessMsg = "success"
            failMsg = "fail"

            #print(books)
            sqlQuery = "update orders set  orderstatus=%s , paymentdate=%s , paymenttype=%s , deliverystatus=%s where emailId=%s and orderstatus=%s"
            val = (orderStatus1,paymentDate,paymentType,deliveryStatus,emailId,orderStatus2)

            #print(sqlQuery)
            #print(val)
            print(books)
            myCursor = self.myDbConnection.cursor()
            myCursor.execute(sqlQuery,val)
            self.myDbConnection.commit()
            rows = myCursor.rowcount
            myCursor.close()

            print(rows)
            if rows:
                print("Updates payment status successfully!!!")
                for book in books:
                    bookObj = ast.literal_eval(json.dumps(book))
                    response = req.post(os.getenv("BOOK_QUANTITY_UPDATE_URL"), json = bookObj)
                    print(response.status_code)
                    booksQuantity = response.status_code
                if booksQuantity == 200:
                    return sucessMsg
                else:
                    raise ValueError(failMsg)
            else:
                raise ValueError(failMsg)

        except ValueError as e:
            return e

        except mysql.connector.Error as Error:
            return failMsg




##################################################               List order details to the user              ##################################################

    def listMyOrders(self,params):
        try:
            print(params)
            emailId = params.get("emailid")
            orderStatus = "Purchased"
            orderList = []
            content = {}
            sucessMsg = "No orders found"
            failMsg = "Failed to fetch order list"

            #print("emailId "+emailId)
            sqlQuery = "select books.bookname,orders.quantity,orders.price,orders.paymentdate,orders.paymenttype,orders.deliverystatus from orders INNER JOIN books  ON orders.bookid = books.bookid where  orders.emailid='"+emailId+"' and orders.orderstatus='"+orderStatus+"'"

            myCursor = self.myDbConnection.cursor()
            myCursor.execute(sqlQuery)

            records = myCursor.fetchall()
            rows = myCursor.rowcount
            myCursor.close()
            print(records)
            print(rows)

            for result in records:
                content = {'bookname': result[0], 'quantity': result[1], 'price': result[2], 'paymentdate': result[3], 'paymenttype': result[4], 'deliverystatus': result[5]}
                orderList.append(content)
                content = {}

            if rows:
                print("Fetched order list successfully!!!")
                return orderList
            else:
                return sucessMsg

        except mysql.connector.Error as Error:
            return failMsg




############################################################       List all order details to the admin           ########################################################

    def listAllOrders(self,params):
        userRole = req.get(os.getenv("USER_SVC_ROLE_URL")+"/"+params.get("emailid"))
        print(userRole.text)

        if userRole.text == "ADMIN":
            try:
                print(params)
                emailId = params.get("emailid")
                orderStatus = "Purchased"
                orderList = []
                content = {}
                sucessMsg = "No orders found"
                failMsg = "Failed to fetch order list"

                #print("emailId "+emailId)
                sqlQuery = "select orders.orderid,books.bookname,orders.quantity,orders.paymentdate,orders.paymenttype,users.name,users.mobile,users.address,users.city,users.state,users.pincode, orders.deliverystatus from orders INNER JOIN books  ON orders.bookid = books.bookid INNER JOIN users  ON orders.emailid = users.email where orders.orderstatus='"+orderStatus+"'"

                myCursor = self.myDbConnection.cursor()
                myCursor.execute(sqlQuery)

                records = myCursor.fetchall()
                rows = myCursor.rowcount
                myCursor.close()
                print(records)
                print(rows)

                for result in records:
                    content = {'orderid': result[0],'bookname': result[1], 'quantity': result[2], 'paymentdate': result[3], 'paymenttype': result[4], 'name': result[5], 'mobile': result[6], 'address': result[7], 'city': result[8], 'state': result[9], 'pincode': result[10], 'deliverystatus': result[11]}
                    orderList.append(content)
                    content = {}


                if rows:
                    print("Fetched all orders successfully!!!")
                    return orderList
                else:
                    return sucessMsg

            except mysql.connector.Error as Error:
                return failMsg

        else:
            try:
                raise ValueError(failMsg)
            except ValueError as e:
                return e




###########################################################              Update delivery status as delivered               ##################################################

    def updateDeliveryStatus(self,params):
        userRole = req.get(os.getenv("USER_SVC_ROLE_URL")+"/"+params.get("emailid"))
        print(userRole.text)

        if userRole.text == "ADMIN":
            try:
                print(params)
                orderId = params.get("orderid")
                deliveryStatus = params.get("deliverystatus")
                sucessMsg = "success"
                failMsg = "fail"
                sqlQuery = "update orders set deliverystatus='"+deliveryStatus +"' where orderid ='"+orderId+"'"

                myCursor = self.myDbConnection.cursor()
                myCursor.execute(sqlQuery)
                self.myDbConnection.commit()
                rows = myCursor.rowcount
                myCursor.close()

                print(rows)

                if rows:
                    print("Updates delivery status successfully!!!")
                    return sucessMsg
                else:
                    return failMsg

            except mysql.connector.Error as Error:
                return failMsg

        else:
            try:
                raise ValueError(failMsg)
            except ValueError as e:
                return e



