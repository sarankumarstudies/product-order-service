| Component                   | Description                                                         | Example Port                |
| --------------------------- | ------------------------------------------------------------------- | --------------------------- |
| **Config Cloud (Git Repo)** | Stores all configuration files for every environment and service    | GitHub Repo: `Config-Cloud` |
| **Config Server**           | Spring Boot app that connects to Git repo and serves configuration  | 8888                        |
| **Eureka Server**           | Service discovery registry                                          | 8761                        |
| **API Gateway**             | Routes requests to downstream services                              | 8000                        |
| **Product Service**         | Example business microservice (fetches products)                    | 8081                        |
| **Order Service**           | Example business microservice (handles orders)                      | 8082                        |
| **RabbitMQ**                | Message broker used by Spring Cloud Bus to broadcast refresh events | 5672 / 15672                |
