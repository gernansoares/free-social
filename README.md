# free-social

Sample project of a social network implemented with microservices architecture

## Modules

* _**free-social-lib**_: Common classes and configurations for all projects
* _**free-social-service-discovery**_: Services registration and discover
* _**free-social-gateway**_: API Gateway, downstream requests to services
* _**free-social-security**_: Authentication and token registration/invalidation
* _**free-social-users**_: Users service, CRUD operations
* _**free-social-posts**_: Posts service, CRUD operations

## Main technologies

* Spring Framework 6
* Spring Boot 3
* Spring Security
* Spring Data JPA
* Spring Data Redis
* Spring Cloud Gateway
* Spring WebFlux
* Netflix Eureka  
* Micrometer tracing
* Maven
* JWT
* Apache Kafka
* Redis (for session storage)
* PostgreSQL (for production)
* H2 database (in memory, for testing)

## How to run

You will **need to create a database called freesocial** and **have Apache Kafka running at localhost:9092** (see installation guide for **[Windows](https://www.geeksforgeeks.org/how-to-install-and-run-apache-kafka-on-windows/)** and **[Linux](https://www.geeksforgeeks.org/how-to-install-kafka-with-zookeeper-on-ubuntu/)** or try using **[Docker](https://www.conduktor.io/kafka/how-to-start-kafka-using-docker/)**)

Execute **mvn install to install all dependencies** in your local repository before running

Edit RedisConfig to set redis properties

Edit KafkaDefaultConfig to set kafka properties

## Endpoins

You can see all supported endpoints in AvaiableRoutes enum at gateway project, but here is a small list:

* **/auth/login** - For login
* **/logout/logout** - For logout
* **/token** - For token validation (not registered in gateway, for internal usage only)
* **/newuser** - Creates user
* **/users** - Update and delete user
* **/posts** - Posts CRUD

## Extra information

Gateway will always insert a UUID header for each authenticated request before downstream it to services, UUID can be used to identify the user in a simpler way