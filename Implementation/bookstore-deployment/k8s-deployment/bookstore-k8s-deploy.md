
### Run the following steps in k8s master node. 
<br/>

All the deployment yaml files are deployed in *bookstore* namespace.

<br/>
Following is the deployment order for deploying the services.

-  MySQL  (mandatory)
-  User Service (uses mysql) - Java
-  Book Service (uses mysql,userservice) - Java
-  Orders Service (uses mysql,userservice,orders service) - Python 
-  UI Service (uses all backend services)


### Step 1 : Create *bookstore* namespace

```sh
>> kubectl create namespace bookstore
```

### Step 2 : Deploy MySQL

```sh
# Deploy MySQL 
>> kubectl apply -f 1-bookstore-mysql.yaml
Note: This will automatically creates a pvc and pv with nfs storage class.

# List pods to get mysql pod name
>> kubectl get pods -n bookstore

# Load data.sql into mysql. Replace mysql podname here.
>> kubectl -n bookstore exec -i <my_sql_pod_name> -- mysql -h mysql.bookstore.svc.cluster.local -pRoot_12345 < data.sql

# Login to MySQL pod to test
>> kubectl run -it --rm --image=mysql:latest --restart=Never mysql-client -- mysql -h mysql.bookstore.svc.cluster.local -pRoot_12345

```

### Step 3 : Deploy User Service

```sh
# Deploy User Service
>> kubectl apply -f 2-bookstore-user.yaml

# Test whether the app is up and running and connecting to database or not
http://<node-ip>:30051/user/test
    Output: Hello! It's working
http://<nodeip>:30051/user/role/admin@kent.edu
    Output: ADMIN

```

### Step 4 : Deploy Book Service

```sh
# Deploy Book Service
>> kubectl apply -f 3-bookstore-book.yaml 

# Verify application is up and running and connecting to database or not. 
http://<node-ip>:30052/books/test 
     Output: Hello ! Greetings from book service
http://<node-ip>/books/list 
      Output: [{"bookid":"b1234567","bookname":"Vamsi book","author":"Vamsi","category":"Science Fiction","price":250.5,"availability":10}]

```

### Step 5 : Deploy Orders Service

```sh
# Deploy Orders Service
>> kubectl apply -f 4-bookstore-orders.yaml

# Verify application is up and running and connecting to database or not.  
http://<node-ip>:30053/orders/test
     Output:Order service is working!!! 
# Test in Postman / Curl     
POST : http://<ip>:30053/orders/cart/list - {"emailid": "vamsi@kent.edu"}
>> curl -H "Content-Type: application/json" -X POST -d '{"emailid": "vamsi@kent.edu"}' http://<node-ip>:30053/orders/cart/list
     Output: [{"bookid":"b1234567","bookname":"Vamsi book","orderid":"o1588074866","price":10.2,"quantity":2}]

```


### Step 6 : Modify Config map
Modify the config map by replacing <node-ip> with any of the k8s node ips.

#### 5-bookstore-ui-configmap.yaml
```
apiVersion: v1
kind: ConfigMap
metadata:
  name: bookstore-ui-configmap
  namespace: bookstore
data:
  app.json: |
    {
      "orders_host": "<node-ip>:30053",
      "user_host": "<node-ip>:30051",
      "books_host": "<node-ip>:30052"
    }  
```

### Step 7 : Create configmap for UI and deploy UI service.

```sh
# Create config map
>> kubectl apply -f 5-bookstore-ui-configmap.yaml
>> kubectl apply -f 6-bookstore-ui.yaml

# View the application in the browser
http://<node-ip>:30054
  Use the following details for login
       vamsi@kent.edu/vamsi@123
       admin@kent.edu/admin@123


```

    
### Step 8 : Test load balancing of user service   
    
```sh
    while true; do curl -w "\n" http://<ip>:30051/user/test-lb; sleep 1; done    
 ```
    
    

<br/>
