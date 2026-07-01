# 协同云图台 (Picture Cloud)

> 一个功能完善的图床与图片云存储管理系统，支持个人空间与团队协作、AI 扩图、以图搜图、空间数据分析。

## 项目简介

**协同云图台** 是一款前后端分离的图片云管理平台。用户可以将图片上传至云端（腾讯云 COS），进行分类、标签、检索和管理。平台支持 **个人空间** 与 **团队空间** 两种模式，每个空间可设置不同等级（容量/数量限制），并提供了丰富的数据分析能力。

## 技术栈

### 后端

| 技术 | 说明 |
| --- | --- |
| **Spring Boot 2.7.6** | 核心框架，Java 8 |
| **MyBatis Plus 3.5.9** | ORM 框架，分页插件 |
| **MySQL** | 关系数据库 |
| **Apache ShardingSphere** | 图片表按 `spaceId` 分表 |
| **Redis** | 缓存与 Session 存储 |
| **Caffeine** | 本地缓存 |
| **Sa-Token** | 权限认证框架 |
| **Spring WebSocket + Disruptor** | 实时推送（高性能无锁队列） |
| **腾讯云 COS** | 对象存储（图片文件） |
| **阿里云 AI (OutPainting)** | 图片扩图（外绘） |
| **百度以图搜图** | 以图搜图集成 |
| **Knife4j** | 接口文档 |
| **Spring AOP** | 切面权限拦截 |

### 前端

| 技术 | 说明 |
| --- | --- |
| **Vue 3 + TypeScript** | 前端框架 |
| **Vite** | 构建工具 |
| **Ant Design Vue 4.x** | UI 组件库 |
| **Pinia** | 状态管理 |
| **Vue Router** | 路由管理 |
| **ECharts + 词云** | 数据可视化 |
| **Axios** | HTTP 请求 |

## 功能模块

### 用户系统
- 用户注册 / 登录
- 用户管理（管理员）
- 会员（VIP）兑换功能

### 图片管理
- 单张 / 批量上传图片（至腾讯云 COS）
- 图片裁剪（vue-cropper）、编辑、删除
- 图片分类与标签
- 图片审核（管理员：待审核 / 通过 / 拒绝）
- 图片主色调提取与颜色分析
- 缩略图生成
- 以图搜图（百度图片搜索）
- AI 扩图（阿里云 OutPainting）

### 空间管理
- 个人空间 & 团队空间
- 空间等级：普通版 / 专业版 / 旗舰版（不同容量与数量限制）
- 空间成员管理（团队空间：管理员 / 编辑者 / 查看者）
- 空间使用情况统计

### 空间数据分析
- 空间使用分析（总容量、总数量、使用占比）
- 图片分类分布
- 标签词云
- 图片大小分布
- 用户上传活跃度分析

### WebSocket 实时推送
- 基于 Spring WebSocket + Disruptor 实现高性能实时推送

## 快速开始

### 环境要求

- **JDK 1.8+**
- **Maven 3.6+**
- **Node.js 18+**
- **MySQL 8.0+**
- **Redis**

### 数据库初始化

执行 `sql/create_table.sql` 创建数据库和表：

```bash
mysql -u root -p < sql/create_table.sql
```

### 后端启动

1. 修改 `application.yml` 中的数据库连接、Redis 配置和腾讯云 COS 密钥
2. 编译启动：

```bash
mvn clean package -DskipTests
java -jar target/picture-cloud-0.0.1-SNAPSHOT.jar
```

或使用 Maven 插件直接运行：

```bash
mvn spring-boot:run
```

服务默认运行在 `http://localhost:8123/api`。

接口文档地址：`http://localhost:8123/api/doc.html`

### 前端启动

```bash
cd picture-frontend
npm install
npm run dev
```

前端默认运行在 `http://localhost:5173`。

### 配置文件说明

核心配置 `src/main/resources/application.yml`：

| 配置项 | 说明 |
| --- | --- |
| `server.port` | 后端端口（默认 8123） |
| `spring.datasource` | MySQL 数据库连接 |
| `spring.redis` | Redis 连接配置 |
| `spring.session.store-type` | Session 存储方式（Redis） |
| `cos.client` | 腾讯云 COS 密钥与桶配置 |
| `mybatis-plus` | MyBatis Plus 配置 |

## 项目结构

```
picture-cloud
├── src/main/java/com/aweqy/picturecloud
│   ├── annotation/        # 自定义注解
│   ├── aop/               # AOP 切面（权限拦截）
│   ├── api/               # 外部 API 集成
│   │   ├── aliyunai/      # 阿里云 AI 扩图
│   │   └── imagesearch/   # 以图搜图
│   ├── common/            # 通用工具（统一响应、分页）
│   ├── config/            # 配置类（CORS、COS、JSON、MyBatis Plus）
│   ├── constant/          # 常量定义
│   ├── controller/        # 控制器层
│   ├── exception/         # 全局异常处理
│   ├── manager/           # 管理器（COS上传、文件管理、分片、WebSocket、权限）
│   ├── mapper/            # MyBatis Plus Mapper
│   ├── model/             # 数据模型
│   │   ├── dto/           # 请求 DTO
│   │   ├── entity/        # 数据库实体（User、Picture、Space、SpaceUser）
│   │   ├── enums/         # 枚举
│   │   └── vo/            # 视图 VO
│   ├── service/           # 服务层接口与实现
│   └── utils/             # 工具类
├── src/main/resources
│   ├── application.yml    # 主配置文件
│   ├── mapper/            # XML Mapper
│   └── static/            # 静态资源
├── sql/create_table.sql   # 建表 SQL
├── picture-frontend/      # 前端项目
│   └── src/
│       ├── api/           # API 接口封装
│       ├── components/    # 组件
│       ├── pages/         # 页面
│       ├── router/        # 路由
│       ├── stores/        # Pinia 状态
│       ├── layouts/       # 布局
│       └── utils/         # 工具函数
├── pom.xml                # Maven 构建文件
└── httpTest/              # HTTP 接口测试文件
```

## 页面路由

| 路径 | 页面 | 权限 |
| --- | --- | --- |
| `/` | 首页 | 公开 |
| `/user/login` | 用户登录 | 公开 |
| `/user/register` | 用户注册 | 公开 |
| `/add_picture` | 创建图片 | 登录 |
| `/add_picture/batch` | 批量创建图片 | 登录 |
| `/picture/:id` | 图片详情 | 登录 |
| `/search_picture` | 图片搜索 | 登录 |
| `/add_space` | 创建空间 | 登录 |
| `/my_space` | 我的空间 | 登录 |
| `/space/:id` | 空间详情 | 登录 |
| `/space_analyze` | 空间分析 | 登录 |
| `/user_exchange_vip` | 兑换会员 | 登录 |
| `/admin/userManage` | 用户管理 | 管理员 |
| `/admin/pictureManage` | 图片管理 | 管理员 |
| `/admin/spaceManage` | 空间管理 | 管理员 |
| `/spaceUserManage/:id` | 空间成员管理 | 管理员 |

## 相关文档

- 后端接口文档（启动后）：`http://localhost:8123/api/doc.html`
- 前端使用 Vite 构建，支持热更新

## 许可证

本项目仅供学习和参考。
