How it works (step-by-step flow)
1. Configuration Storage

All configurations are version-controlled and stored in your Git repository (Config-Cloud).

Configs are structured per environment:

dev/product-service-dev.properties
qa/order-service-qa.properties
prod/application-prod.properties

2. Config Server setup

The Config Server connects to your Git repository.

It exposes REST endpoints like:

http://localhost:8888/{application}/{profile}


Example:

http://localhost:8888/product-service/dev

http://localhost:8888/application/prod

3. Microservice configuration

Each microservice (e.g., ProductService) includes:

spring.application.name=product-service
spring.profiles.active=dev
spring.config.import=optional:configserver:http://localhost:8888


At startup, the microservice contacts the Config Server, asking for:

GET /product-service/dev


and loads its configuration dynamically.

4. RabbitMQ + Spring Cloud Bus

When you change a property in the Git repo and commit/push it,
you can trigger an update event:

POST http://localhost:8888/actuator/busrefresh


The Config Server sends a message via RabbitMQ.

All microservices subscribed to the bus receive this message and automatically reload updated configuration values (without restart).

Advantages of this architecture
| Benefit                                   | Description                                                                             |
| ----------------------------------------- | --------------------------------------------------------------------------------------- |
| 🧩 **Centralized Configuration**          | All configs in one Git repo — easy to manage, audit, and version.                       |
| 🧪 **Environment Separation**             | `dev`, `qa`, `prod` folders clearly separate configs for different deployments.         |
| 🔁 **Dynamic Refresh (Spring Cloud Bus)** | Change configs and refresh services without restarting.                                 |
| 🛡️ **Secure & Scalable**                 | Config Server can be protected by authentication and integrated with CI/CD.             |
| 🧠 **Environment-Aware Loading**          | Services automatically pick correct configs using `spring.profiles.active`.             |
| 📜 **Git-based Version Control**          | Rollback or track changes easily; Git acts as source of truth.                          |
| ⚙️ **Service Discovery Integration**      | Config Server and clients register with Eureka. Works seamlessly in distributed setups. |
| 🚀 **Production-ready**                   | Follows the same pattern used in cloud-native systems on AWS, Azure, or Kubernetes.     |

Example: Config Load Flow
| Step | Component                              | Description                                                                                                            |
| ---- | -------------------------------------- | ---------------------------------------------------------------------------------------------------------------------- |
| 1️⃣  | `ProductService` starts                | Reads `spring.config.import` and contacts Config Server                                                                |
| 2️⃣  | Config Server                          | Fetches from Git → merges `application.properties`, `application-dev.properties`, and `product-service-dev.properties` |
| 3️⃣  | ProductService                         | Receives all properties and starts using them                                                                          |
| 4️⃣  | You update Git config                  | e.g., change `custom.message=Updated message`                                                                          |
| 5️⃣  | Trigger `/busrefresh` on Config Server | Config Server publishes refresh event via RabbitMQ                                                                     |
| 6️⃣  | ProductService, OrderService, Gateway  | All receive event → reload their config automatically                                                                  |

Example API Verification

| URL                                              | Description                          | Expected Result                   |
| ------------------------------------------------ | ------------------------------------ | --------------------------------- |
| `http://localhost:8888/product-service/dev`      | Fetch Product Service DEV config     | JSON of merged properties         |
| `http://localhost:8081/message`                  | Endpoint in Product Service          | Returns value from config repo    |
| `POST http://localhost:8888/actuator/busrefresh` | Triggers refresh across all services | Services log “Refreshing context” |
