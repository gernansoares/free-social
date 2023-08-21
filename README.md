# free-social

Example project of a social network implemented with microservices architecture

## Projects

* _**free-social-lib**_: Common classes and configurations for all projects
* _**free-social-gateway**_: API Gateway which controls authentication and authorization, also downstream requests to services
* _**free-social-token-jobs**_: Register and invalidate tokens issued by gateway
* _**free-social-users**_: Users service, controls CRUD operations

## Main technologies

* Spring Boot 3
* Spring Security
* Spring Cloud Gateway
* Spring WebFlux
* Maven
* JWT
* Apache Kafka
* PostgreSQL (for production)
* H2 database (in memory, for testing)

## How to run

You will **need to create a database called freesocial** and **[have Apache Kafka running at localhost:9092](https://www.geeksforgeeks.org/how-to-install-and-run-apache-kafka-on-windows/)**

Execute **mvn install to install all dependencies** in your local repository before running