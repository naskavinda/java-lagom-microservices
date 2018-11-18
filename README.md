# Lagom-java-microservices
This application create using Lagom, Java and Cassandra Database. Here we are using embedded Cassandra to persist events and embedded kafka for publishing and subscribing between microservices.

There have two Microservices call  `Order microservices`  and `User Microservices` 

# user-microservice

There have two asynchronous rest call which is `Create User`  and  `Get User` 

#### 1. Create User : 
Route(Method - POST) : `localhost:9000/api/user`

Rawdata(json): 
````
    {
        "id": "1",
        "name": "Supun Kavinda",
        "email": "naskavinda@gmail.com",
        "userName": "naskavinda",
        "Password": "abc123"
    }
````

#### 2. Get User details :

Route(Method - POST) : `localhost:9000/api/user/:id`


# Order-microservice

There have two asynchronous rest call which is `Place Order`  and  `Get Order Status` 
In Hear These Two microservices communicate using cross microservice call.

#### 1. Place Order 

Route(Method - POST) : `localhost:9000/api/order`

Rawdata(json):
````$xslt
{
    "id": "1",
    "userId": "1",
    "products": ["10","2","5"]
}
````

#### 2. Get Order Status

Route(Method - POST) : `localhost:9000/api/order/:id`

# Prerequisites
1. Java 1.8
2. Maven 3.5.1

#### Command to start the project

`mvn lagom:runAll`
