# nep-backend

东软环保公众监督系统（NEP）后端工程。

## 技术栈

- JDK 17+（编译目标 17）
- Spring Boot 3.4.5
- MyBatis-Plus 3.5.9
- Druid 1.2.24
- MySQL 5.5+ / 8.x（开发机可用）
- Lombok、Jackson

## 启动步骤

1. 启动 MySQL，确保可连接 `localhost:3306`
2. 导入 SQL（按顺序）：

```bash
mysql -uroot -p < src/main/resources/sql/init.sql
mysql -uroot -p < src/main/resources/sql/seed.sql
```

3. 检查 `src/main/resources/application.yml` 中的数据源账号密码
4. 启动：

```bash
mvn spring-boot:run
```

5. 验证：

```bash
curl http://localhost:8080/api/test/hello
curl http://localhost:8080/api/test/db
```

## 演示账号

密码统一为 `123456`（存储为 MD5 + 盐）。

| 角色 | 账号 | 密码 |
|---|---|---|
| 公众监督员 | 13800000001 | 123456 |
| 网格员 | 001 / 002 / 003 / 004 / 005 | 123456 |
| 系统管理员 | admin | 123456 |

## 密码加密说明

- 算法：MD5（任务书限制，不上 BCrypt）
- 盐值：`nep_2026_`（写死在 `MD5Util`）
- 规则：`MD5(nep_2026_ + 明文密码)`
- 示例：`123456` → `08fd5e46db299792277fd2c0315537b4`

## 目录结构

```
nep-backend/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/neusoft/nep/
    │   ├── NepApplication.java
    │   ├── common/          # R、PageResult、Cors、异常处理
    │   ├── controller/      # TestController
    │   ├── entity/          # 8 张表实体
    │   ├── mapper/          # AqiMapper
    │   └── utils/           # MD5Util
    └── resources/
        ├── application.yml
        └── sql/
            ├── init.sql     # 建表
            └── seed.sql     # 种子数据
```

## 接口约定

- 端口：`8080`
- 前缀：`/api`
- 统一返回：`R{code, msg, data}`，成功码 `200`
