# AI 开发规则文件（rules.md）

> 本文件是「东软环保公众监督系统」项目的 AI 编程协作规则
> 任何 AI 工具（Cursor / Claude Code / 通义灵码等）开工前必须先完整阅读本文件
> 每次任务完成后，AI 需要回写「本次新发现」到第 6 节
> 团队 4 人共享同一个 rules.md，commit 进 Git 仓库根目录

---

## 1. 硬约束（违反任何一条，任务视为失败）

### 1.1 字段命名（与前端接口强对齐）

- **禁止改字段名**：数据库表字段、Java 实体类、API 返回 JSON 全部按建表 SQL 的下划线命名，Java 侧自动转驼峰
- **必须保持的字段**：
  - 通用：`id`、`deleted`（逻辑删除）、`createTime`
  - 区域：`provinceId`、`cityId`、`provinceName`、`cityName`
  - 角色：`supervisorId`、`gridMemberId`、`loginCode`
  - AQI：`level`、`grade`、`color`、`so2Iaqi`、`coIaqi`、`pm25Iaqi`、`totalAqi`、`totalLevel`
  - 反馈：`feedbackDesc`、`feedbackTime`、`status`、`assignedGridMemberId`、`assignType`

### 1.2 接口契约

- 所有接口前缀 `/api`
- 后端端口 `8080`
- 前端端口：NEPS=8081，NEPG=8082，NEPM=8083，NEPV=8084
- 统一返回结构：`R{code, msg, data}`
- 成功码 `200`，业务错误码 `500`，未授权 `401`，权限不足 `403`
- 分页返回：`PageResult{total, current, size, records}`

### 1.3 技术栈底线

- JDK 17 及以上
- MySQL 5.5（任务书要求），用 `utf8_general_ci` 不用 `utf8mb4_0900_ai_ci`
- SpringBoot 3.x 生态

---

## 2. 行为规范

### 2.1 执行流程

1. **先读 rules.md**：每次新对话开始第一件事
2. **再读主提示词**：理解本次任务
3. **分步执行**：每步 1 个完整功能，停下来等用户确认
4. **完成后回写**：在第 6 节追加本次新发现的规则

### 2.2 沟通规范

- 简洁直接，不要客套
- 错误必须贴完整堆栈（前 30 行 + 关键行）
- 代码用代码块包裹，标注文件路径
- 中文回复，技术名词保留英文

### 2.3 决策原则

- 遇到模糊点 → 选最简单方案，并在回复中说明为什么
- 遇到多种合理方案 → 列出 2-3 个选项给用户选
- 遇到超出规则范围的需求 → 停下来问用户，不擅自决定

---

## 3. 代码规范

### 3.1 命名

- 类名：大驼峰（`UserService`）
- 方法名、变量名：小驼峰（`getUserById`）
- 常量：全大写下划线（`MAX_RETRY_COUNT`）
- 包名：全小写（`com.neusoft.nep.service`）
- 数据库表名：小写下划线（`aqi_feedback`）
- 数据库字段：小写下划线（`province_id`）

### 3.2 注释

- 类级注释：说明类的职责
- 方法级注释：只注释 public 方法，写明入参、返回值、用途
- 行内注释：解释「为什么」而不解释「是什么」
- 中文注释用 UTF-8

### 3.3 错误处理

- Controller 不直接 catch，统一抛到 GlobalExceptionHandler
- Service 抛业务异常（`BusinessException`），不带堆栈
- 关键操作加 try-catch + 友好提示
- 不要吞异常

### 3.4 数据库

- 所有表必须有 `id` 主键自增
- 所有表必须有 `deleted` 逻辑删除字段（默认 0）
- 时间字段用 `DATETIME`，默认 `CURRENT_TIMESTAMP`
- 金额/浓度用 `DECIMAL`，不用 `FLOAT`
- 字符串长度宁大勿小

### 3.5 接口

- 路径用复数名词（`/api/aqiFeedbacks` 而不是 `/api/getAqiFeedback`）
- 动词用 POST（写操作）、GET（读操作）、PUT（更新）、DELETE（删除）
- 入参用对象，不用 Map
- 返回统一 `R`，不要直接返回实体

---

## 4. AQI 计算规范（最高优先级）

### 4.1 公式

```
AQI = MAX(SO2_IAQI, CO_IAQI, PM2.5_IAQI)
```

### 4.2 限值表

| 等级 | SO2 (μg/m³) | CO (mg/m³) | PM2.5 (μg/m³) | 颜色 |
|---|---|---|---|---|
| 1 优 | 0-150 | 0-5 | 0-35 | #00e400 |
| 2 良 | 150-500 | 5-10 | 35-75 | #ffff00 |
| 3 轻度污染 | 500-650 | 10-35 | 75-115 | #ff7e00 |
| 4 中度污染 | 650-800 | 35-60 | 115-150 | #ff0000 |
| 5 重度污染 | 800-1600 | 60-90 | 150-250 | #8f3f97 |
| 6 严重污染 | 1600+ | 90+ | 250+ | #7e0023 |

### 4.3 计算时机

- 前端实时算（输入即变）
- 后端再算一次（提交时校验）
- 后端算的为权威

### 4.4 边界处理

- 浓度 < 0 → 当 0 处理
- 浓度缺失 → 该项 IAQI 按 1（优）算
- 浓度超最大值 → 按最大等级算

---

## 5. 安全与权限

### 5.1 密码

- 存储 MD5（任务书限制，不上 BCrypt）
- 统一加盐 `nep_2026_`
- 不要在日志中打印密码字段

### 5.2 SQL 注入

- 全部用 MyBatis-Plus 的 LambdaQueryWrapper
- 禁止用 `${}` 拼接
- 复杂查询用 XML，不用字符串拼接

### 5.3 越权

- 公众监督员 token 不能访问管理员接口
- 网格员只能看自己被指派的任务
- 管理员能看所有
- 跨角色访问统一返回 403

---

## 6. 本次任务新发现（AI 每次任务后回写）

<!--
格式：
## [YYYY-MM-DD] 任务简述
- 发现 1：...
- 发现 2：...
- 待确认 1：...
-->

## [2026-07-23] 后端工程脚手架搭建
- 发现 1：start.spring.io 现已只接受 Spring Boot ≥4.0.0，与本项目「SpringBoot 3.x」硬约束冲突；脚手架改为手写 Boot 3.4.5 + MyBatis-Plus 3.5.9 + Druid 1.2.24
- 发现 2：提示词写「utf8mb4 + utf8_general_ci」会冲突，实际建表用 `utf8mb4_general_ci`（仍避开 MySQL 8 的 `utf8mb4_0900_ai_ci`）；`admins.id` 原文缺 `INT`，已补上
- 发现 3：JDK 26 下 Lombok 需在 `maven-compiler-plugin` 显式配置 `annotationProcessorPaths`，否则 `@RequiredArgsConstructor` / `@Data` 可能不生成代码
- 发现 4：已改用 `com.mysql.cj.jdbc.Driver`（旧 `com.mysql.jdbc.Driver` 有弃用警告）；密码 MD5(`nep_2026_123456`) = `08fd5e46db299792277fd2c0315537b4`
- 发现 5：`application.yml` 数据源已统一为 `root/root`（需本机 MySQL root 密码同步为 `root`）

## [2026-07-23] NEPS 公众监督员端开发
- 发现 1：NEPS 提示词 API 清单路径（`/api/supervisor/*`、`/api/region/*`、`/api/aqi/levels`、`/api/aqiFeedback/*`）与早期 `docs/api-list.md` 规划路径（`/api/provinces` 等）不一致；已以后端按 NEPS 提示词落地为准，前端严格对齐，`docs/api-list.md` 已同步为实际路径
- 发现 2：组件库选 Vant 4（移动端优先），状态管理选 Pinia；Axios 走 Vite proxy `/api`→`8080`，避免浏览器直连跨域
- 发现 3：Token 采用内存 UUID（`TokenUtil`），重启后端会失效，课程演示够用；鉴权拦截器排除注册/登录/查重与 `/api/test/**`
- 发现 4：种子城市名为「北京」而非「北京市」，选择网格页展示为「北京市-北京」属数据问题，不是接口字段错误
- 已确认 1：Token 方案固定为**内存 UUID**，不升级 JWT / 不落库（课设够用；重启后端需重新登录属预期行为）
- 已确认 2：全项目接口路径以**实际已实现**为准（见 `docs/api-list.md`「已实现」），其他端（NEPG/NEPM/NEPV）禁止再使用早期规划路径

---

## 7. 上下文文件清单

AI 必须了解这些文件的位置：

- `nep-backend/src/main/resources/sql/init.sql` - 8 张表建表 SQL（字段定义权威）
- `nep-backend/src/main/resources/sql/seed.sql` - 种子数据
- `nep-backend/src/main/java/com/neusoft/nep/entity/` - 8 个实体类
- `nep-frontend-neps/` - 公众监督员端前端
- 接口清单（开发中维护）：`docs/api-list.md`
