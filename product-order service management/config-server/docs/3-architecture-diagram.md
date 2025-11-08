                    ┌──────────────────────────┐
                    │   Config Cloud (GitHub)  │
                    │  ────────────────        │
                    │  application.properties   │
                    │  dev/product-service...   │
                    │  qa/order-service...      │
                    │  prod/...                 │
                    └──────────┬────────────────┘
                               │
                               ▼
                    ┌──────────────────────────┐
                    │   Config Server (8888)   │
                    │ Reads Git repo configs   │
                    │ Serves via REST API      │
                    │ e.g., /product-service/dev│
                    └───────┬───────────────────┘
                            │
┌────────────────────────┼────────────────────────┐
│                        │                        │
▼                        ▼                        ▼
┌──────────────┐      ┌──────────────┐        ┌────────────────┐
│ Product Svc  │      │ Order Svc    │        │ API Gateway    │
│ (8081)       │      │ (8082)       │        │ (8000)         │
│ spring.config│      │ spring.config│        │ spring.config  │
│ .import=...  │      │ .import=...  │        │ .import=...    │
└──────┬───────┘      └──────┬──────┘        └──────┬─────────┘
│                     │                       │
└──────────────┬──────┴──────────────┬────────┘
▼                     ▼
┌───────────────────────────────┐
│     RabbitMQ (Spring Bus)     │
│  Broadcasts refresh events     │
│  e.g., /busrefresh             │
└───────────────────────────────┘
