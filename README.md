# free-social

Example project of how microservices can be used to create a social network

## Projects

* free-social-lib: Common classes and configurations for all projects
* free-social-gateway: API Gateway which controls authentication and authorization, downstream requests to services
* free-social-users: Users microservice, controls CRUD operations and testing

## Used technologies:

* Spring Boot 3
* Spring Security
* Spring Cloud Gateway
* Spring WebFlux
* Maven
* JWT
* PostgreSQL (for production)
* H2 database (for testing)
