# 东软环保公众监督系统（NEP）

基于公众监督与网格员实测的空气质量信息收集与管理平台。本仓库为课程实践工程，包含后端服务、公众监督员端前端及相关文档。

## 系统组成

| 模块 | 目录 | 端口 | 说明 |
|---|---|---|---|
| 后端 API | `nep-backend/` | 8080 | Spring Boot + MyBatis-Plus |
| 公众监督员端 | `nep-frontend-neps/` | 8081 | Vue 3 + Vant（移动端风格） |
| 网格员端 | — | 8082 | 待开发（NEPG） |
| 系统管理员端 | — | 8083 | 待开发（NEPM） |
| 决策者大屏 | — | 8084 | 待开发（NEPV） |

## 业务概览

1. **公众监督员**注册/登录，选择省-市网格，预估 AQI 等级并提交反馈  
2. **管理员**将反馈指派给网格员  
3. **网格员**现场实测 SO₂ / CO / PM2.5，系统计算综合 AQI  
4. **决策者**查看统计与分布大屏  

## 技术栈

- **后端**：JDK 17+、Spring Boot 3.4.x、MyBatis-Plus、Druid、MySQL  
- **前端（NEPS）**：Vue 3、Vite、Vue Router 4、Pinia、Axios、Vant 4  
- **协作文档**：`rules.md`（开发规则）、`docs/api-list.md`（接口清单）

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.8+
- Node.js 18+（建议）
- MySQL 8.x（账号默认 `root` / `root`）

### 2. 初始化数据库

使用官方 dump（字段定义权威）：

```bash
mysql -uroot -proot -e "DROP DATABASE IF EXISTS nep; CREATE DATABASE nep DEFAULT CHARACTER SET utf8mb3;"
mysql -uroot -proot nep < nep-backend/src/main/resources/sql/nep.sql
```

> 早期 `init.sql` / `seed.sql` 已过时，请勿再作为权威。

### 3. 启动后端

```bash
cd nep-backend
mvn spring-boot:run
```

验证：

```bash
curl http://localhost:8080/api/test/hello
curl http://localhost:8080/api/test/db
```

### 4. 启动公众监督员端

```bash
cd nep-frontend-neps
npm install
npm run dev
```

浏览器访问：http://localhost:8081  
（开发环境通过 Vite 将 `/api` 代理到 `http://localhost:8080`）

## 演示账号（来自官方 nep.sql）

| 角色 | 账号 | 密码 |
|---|---|---|
| 公众监督员 | `13147859658` | `123` |
| 公众监督员 | `13776567898` | `123456` |
| 系统管理员 | `admin` | `123` |
| 网格员示例 | `caocao` | `123` |

说明：官方库密码为**明文**存储；Token 为内存 UUID，重启后端后需重新登录。

## 目录说明

```
DNUI-project/
├── nep-backend/                 # 后端工程
│   └── src/main/resources/sql/
│       ├── nep.sql              # 官方库（权威）
│       ├── init.sql             # 旧脚手架（已过时）
│       └── seed.sql             # 旧种子数据（已过时）
├── nep-frontend-neps/           # 公众监督员端
├── docs/
│   └── api-list.md              # 接口清单（已实现 + 字段映射）
├── rules.md                     # AI / 团队开发硬约束
├── doc/                         # 需求书、原型等资料
└── README.md                    # 本文件
```

## 重要约定

1. **数据库字段**以 `nep-backend/src/main/resources/sql/nep.sql` 为准  
2. **接口路径与字段映射**以 `docs/api-list.md`「已实现」为准  
3. **开发规则**见根目录 `rules.md`（含 AQI 限值、密码策略、权限约定等）  
4. 监督员主键为手机号 `tel_id`；前端字段名可用 `supervisorId`，值为手机号字符串  

## 文档与协作

| 文件 | 用途 |
|---|---|
| [rules.md](./rules.md) | 字段命名、接口契约、AQI 规则、安全约定 |
| [docs/api-list.md](./docs/api-list.md) | 已实现 / 规划中接口清单 |
| [nep-backend/README.md](./nep-backend/README.md) | 后端启动与结构说明 |
| [nep-frontend-neps/README.md](./nep-frontend-neps/README.md) | NEPS 前端启动与流程说明 |

## 仓库

https://github.com/asamu18/DNUI-project
