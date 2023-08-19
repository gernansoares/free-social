# free-social

Example project of how microservices can be used to create a social network

## Projects

* free-social-lib: Common classes and configurations for all projects
* free-social-gateway: API Gateway which controls authentication and authorization, downstream requests to services
* free-social-users: Users microservice, controls CRUD operations and testing

## Used technologies:

* Spring Boot 3
* Spring Security 6 
* Spring Cloud Gateway 4
* Lombok
* JWT
* PostgreSQL 
* H2 database
