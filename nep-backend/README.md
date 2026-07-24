# nep-backend

东软环保公众监督系统（NEP）后端工程。

## 技术栈

- JDK 17+（编译目标 17）
- Spring Boot 3.4.5
- MyBatis-Plus 3.5.9
- Druid 1.2.24
- MySQL 8.x / 5.5+（开发机可用）
- Lombok、Jackson

## 启动步骤

1. 启动 MySQL，确保可连接 `localhost:3306`
2. 导入**官方库**：

```bash
mysql -uroot -p -e "DROP DATABASE IF EXISTS nep; CREATE DATABASE nep DEFAULT CHARACTER SET utf8mb3;"
mysql -uroot -p nep < src/main/resources/sql/nep.sql
```

3. 检查 `src/main/resources/application.yml` 中的数据源账号密码（默认 `root/root`）
4. 启动：

```bash
mvn spring-boot:run
```

5. 验证：

```bash
curl http://localhost:8080/api/test/hello
curl http://localhost:8080/api/test/db
```

## 演示账号（官方 nep.sql）

| 角色 | 账号 | 密码 |
|---|---|---|
| 公众监督员 | 13147859658 | 123 |
| 公众监督员 | 13776567898 | 123456 |
| 网格员 | caocao | 123 |
| 系统管理员 | admin | 123 |

## 密码说明

- 官方 dump 为**明文**存储（`varchar(20)`）
- 登录/注册与 dump 一致使用明文比对
- 接口字段映射见 `docs/api-list.md`

## 目录结构

```
nep-backend/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/neusoft/nep/
    │   ├── NepApplication.java
    │   ├── common/
    │   ├── config/
    │   ├── controller/
    │   ├── dto/
    │   ├── entity/
    │   ├── interceptor/
    │   ├── mapper/
    │   ├── service/
    │   ├── utils/
    │   └── vo/
    └── resources/
        ├── application.yml
        └── sql/
            └── nep.sql          # 官方库权威
```

## 接口约定

- 端口：`8080`
- 前缀：`/api`
- 统一返回：`R{code, msg, data}`，成功码 `200`
- 清单：仓库根目录 `docs/api-list.md`
