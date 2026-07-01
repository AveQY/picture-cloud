# Picture Cloud - Collaborative Image Management Platform

> A full-featured image hosting and cloud storage management system supporting personal spaces and team collaboration, AI outpainting, image search, and space data analytics.

## Project Overview

**Picture Cloud** is a frontend-backend separated image management platform. Users can upload images to the cloud (Tencent Cloud COS), organize them with categories and tags, and manage them efficiently. The platform supports **Personal Space** and **Team Space** modes, each with different tier limits (capacity/quantity), and provides rich data analytics capabilities.

## Tech Stack

### Backend

| Technology | Description |
| --- | --- |
| **Spring Boot 2.7.6** | Core framework, Java 8 |
| **MyBatis Plus 3.5.9** | ORM framework with pagination |
| **MySQL** | Relational database |
| **Apache ShardingSphere** | Sharding `picture` table by `spaceId` |
| **Redis** | Caching and Session storage |
| **Caffeine** | Local cache |
| **Sa-Token** | Authentication framework |
| **Spring WebSocket + Disruptor** | Real-time push (high-performance lock-free queue) |
| **Tencent Cloud COS** | Object storage for images |
| **Alibaba Cloud AI (OutPainting)** | AI image outpainting |
| **Baidu Image Search** | Reverse image search |
| **Knife4j** | API documentation |
| **Spring AOP** | Aspect-based permission interception |

### Frontend

| Technology | Description |
| --- | --- |
| **Vue 3 + TypeScript** | Frontend framework |
| **Vite** | Build tool |
| **Ant Design Vue 4.x** | UI component library |
| **Pinia** | State management |
| **Vue Router** | Routing |
| **ECharts + Word Cloud** | Data visualization |
| **Axios** | HTTP client |

## Features

### User System
- User registration / login
- User management (admin)
- VIP redemption

### Image Management
- Single / batch upload (to Tencent Cloud COS)
- Image cropping, editing, deletion
- Image categories and tags
- Image review (admin: pending / approved / rejected)
- Dominant color extraction and color analysis
- Thumbnail generation
- Reverse image search (Baidu)
- AI outpainting (Alibaba Cloud)

### Space Management
- Personal space & team space
- Space tiers: Basic / Pro / Enterprise (different capacity and quantity limits)
- Space member management (team roles: admin / editor / viewer)
- Space usage statistics

### Space Analytics
- Space usage analysis (total capacity, total count, usage ratio)
- Image category distribution
- Tag word cloud
- Image size distribution
- User upload activity analysis

### WebSocket Real-time Push
- High-performance real-time push based on Spring WebSocket + Disruptor

## Quick Start

### Prerequisites

- **JDK 1.8+**
- **Maven 3.6+**
- **Node.js 18+**
- **MySQL 8.0+**
- **Redis**

### Database Initialization

Execute `sql/create_table.sql` to create the database and tables:

```bash
mysql -u root -p < sql/create_table.sql
```

### Backend

1. Update database connection, Redis config, and Tencent Cloud COS credentials in `application.yml`
2. Build and run:

```bash
mvn clean package -DskipTests
java -jar target/picture-cloud-0.0.1-SNAPSHOT.jar
```

Or run directly with Maven:

```bash
mvn spring-boot:run
```

The service runs at `http://localhost:8123/api` by default.

API docs: `http://localhost:8123/api/doc.html`

### Frontend

```bash
cd picture-frontend
npm install
npm run dev
```

Frontend runs at `http://localhost:5173` by default.

### Configuration

Core config file: `src/main/resources/application.yml`

| Config | Description |
| --- | --- |
| `server.port` | Backend port (default: 8123) |
| `spring.datasource` | MySQL connection |
| `spring.redis` | Redis connection |
| `spring.session.store-type` | Session storage (Redis) |
| `cos.client` | Tencent Cloud COS credentials and bucket |
| `mybatis-plus` | MyBatis Plus settings |

## Project Structure

```
picture-cloud
├── src/main/java/com/aweqy/picturecloud
│   ├── annotation/        # Custom annotations
│   ├── aop/               # AOP aspects (permission interceptor)
│   ├── api/               # External API integration
│   │   ├── aliyunai/      # Alibaba Cloud AI outpainting
│   │   └── imagesearch/   # Reverse image search
│   ├── common/            # Common utilities (response wrapper, pagination)
│   ├── config/            # Configuration classes (CORS, COS, JSON, MyBatis Plus)
│   ├── constant/          # Constants
│   ├── controller/        # Controllers
│   ├── exception/         # Global exception handling
│   ├── manager/           # Managers (COS upload, file, sharding, WebSocket, auth)
│   ├── mapper/            # MyBatis Plus Mapper
│   ├── model/             # Data models
│   │   ├── dto/           # Request DTOs
│   │   ├── entity/        # Database entities (User, Picture, Space, SpaceUser)
│   │   ├── enums/         # Enumerations
│   │   └── vo/            # View objects
│   ├── service/           # Service interfaces
│   └── utils/             # Utility classes
├── src/main/resources
│   ├── application.yml    # Main configuration
│   ├── biz/               # Business JSON configs (auth, VIP codes)
│   └── mapper/            # MyBatis XML mappers
├── sql/create_table.sql   # Database schema
├── picture-frontend/      # Vue 3 frontend
└── target/                # Build output
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
