# AI 开发规则文件（rules.md）

> 本文件是「东软环保公众监督系统」项目的 AI 编程协作规则
> 任何 AI 工具（Cursor / Claude Code / 通义灵码等）开工前必须先完整阅读本文件
> 每次任务完成后，AI 需要回写「本次新发现」到第 6 节
> 团队 4 人共享同一个 rules.md，commit 进 Git 仓库根目录
>
> **数据库权威**：`nep-backend/src/main/resources/sql/nep.sql`（官方 dump）  
> **接口权威**：`docs/api-list.md`「已实现」章节

---

## 1. 硬约束（违反任何一条，任务视为失败）

### 1.1 字段命名（以官方 nep.sql 为准）

- **禁止擅自改库表字段名**：Java 实体、MyBatis 映射按官方 dump 下划线字段；Java 侧驼峰
- **禁止再使用已废弃脚手架字段**：如通用 `id`/`deleted`、`loginCode`、`feedbackDesc`、`so2Iaqi` 等（早期 `init.sql` 已过时）
- **官方库关键字段（必须对齐）**：

| 域 | 库字段 / Java | 说明 |
|---|---|---|
| 监督员 | `tel_id` / `telId` | 主键=手机号；API 对外仍可用 `supervisorId`（值=手机号字符串） |
| 监督员 | `real_name`、`birthday`、`sex` | `sex`：1男 / 0女 |
| 区域 | `province_id`、`city_id`、`province_name`、`city_name` | 省/市主键分别为 `province_id` / `city_id` |
| AQI | `aqi_id`、`aqi_explain`、`color`、`health_impact` | 及 `so2_*` / `co_*` / `spm_*` 上下限 |
| 反馈 | `af_id`、`tel_id`、`address`、`information`、`estimated_grade` | `af_date`/`af_time`；`state`：0未指派 / 1已指派 / 2已确认 |
| 网格员 | `gm_id`、`gm_code`、`gm_name`、`tel`、`state` | |
| 管理员 | `admin_id`、`admin_code` | |
| 统计 | `so2_value/level`、`co_value/level`、`spm_value/level`、`aqi_id`、`gm_id`、`fd_id` | |

- **API 映射层**：NEPS 等前端可用友好字段名（如 `detailAddress`→写 `address`），但必须以 `docs/api-list.md` 对照表为准，禁止两边各写一套

### 1.2 接口契约

- 所有接口前缀 `/api`
- 后端端口 `8080`
- 前端端口：NEPS=8081，NEPG=8082，NEPM=8083，NEPV=8084
- 统一返回结构：`R{code, msg, data}`
- 成功码 `200`，业务错误码 `500`，未授权 `401`，权限不足 `403`
- 分页返回：`PageResult{total, current, size, records}`
- **路径以 `docs/api-list.md` 已实现为准**（例：`/api/supervisor/login`、`/api/region/provinces`），不要再用早期规划路径

### 1.3 技术栈底线

- JDK 17 及以上
- MySQL（官方 dump 来自 8.x，字符集多为 `utf8mb3`）；开发可用本地 MySQL 8
- SpringBoot 3.x 生态
- 连接：`root` / `root`，库名 `nep`

---

## 2. 行为规范

### 2.1 执行流程

1. **先读 rules.md**：每次新对话开始第一件事
2. **再读主提示词**：理解本次任务
3. **分步执行**：每步 1 个完整功能，停下来等用户确认
4. **完成后回写**：在第 6 节追加本次新发现的规则；接口变更必须同步 `docs/api-list.md`

### 2.2 沟通规范

- 简洁直接，不要客套
- 错误必须贴完整堆栈（前 30 行 + 关键行）
- 代码用代码块包裹，标注文件路径
- 中文回复，技术名词保留英文
- **Git 提交说明必须使用中文**（含标题与正文；专有名词如 `NEPS`、`API`、文件名可保留英文）

### 2.3 决策原则

- 遇到模糊点 → 选最简单方案，并在回复中说明为什么
- 遇到多种合理方案 → 列出 2-3 个选项给用户选
- 遇到超出规则范围的需求 → 停下来问用户，不擅自决定
- **库表冲突时**：以官方 `nep.sql` 为准，不以早期 AI 脚手架 SQL 为准

---

## 3. 代码规范

### 3.1 命名

- 类名：大驼峰（`UserService`）
- 方法名、变量名：小驼峰（`getUserById`）
- 常量：全大写下划线（`MAX_RETRY_COUNT`）
- 包名：全小写（`com.neusoft.nep.service`）
- 数据库表名：小写下划线（`aqi_feedback`）
- 数据库字段：小写下划线（`province_id`）——**与官方 dump 一致**

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

- **主键按官方表定义**（不一定叫 `id`，如 `tel_id` / `af_id` / `gm_id`）
- **官方库无统一 `deleted` 逻辑删除**；不要假设所有表都有 `deleted`
- 日期/时间在官方库多为 `varchar`（如 `af_date`/`af_time`/`birthday`），按字符串读写
- 浓度/等级在官方库多为 `INT`（`so2_value` 等）
- 改表结构前先问用户；默认只读官方 dump，不擅自迁移

### 3.5 接口

- 已落地路径以 `docs/api-list.md` 为准（可含业务动词路径，如 `/aqiFeedback/submit`）
- 写操作用 POST，读操作用 GET；更新用 PUT，删除用 DELETE
- 入参用对象，不用 Map
- 返回统一 `R`，不要直接返回实体（可返回 VO / 映射后的 Map）

---

## 4. AQI 计算规范（最高优先级）

### 4.1 公式

综合等级取三项中最差（最大等级）：

```
AQI_LEVEL = MAX(SO2_LEVEL, CO_LEVEL, SPM_LEVEL)
```

对应官方表字段：`so2_level` / `co_level` / `spm_level` → `aqi_id`

### 4.2 限值表（与官方 `aqi` 表一致）

| 等级 | 表述 | SO2 | CO | SPM(PM2.5) | 颜色 |
|---|---|---|---|---|---|
| 1 | 优 | 0-50 | 0-5 | 0-35 | #02E300 |
| 2 | 良 | 51-150 | 6-10 | 36-75 | #FFFF00 |
| 3 | 轻度污染 | 151-475 | 11-35 | 76-115 | #FF7E00 |
| 4 | 中度污染 | 476-800 | 36-60 | 116-150 | #FE0000 |
| 5 | 重度污染 | 801-1600 | 61-90 | 151-250 | #98004B |
| 6 | 严重污染 | 1601-2620 | 91-150 | 251-500 | #7E0123 |

> 权威数据以库表 `aqi` 的 `so2_min/max`、`co_min/max`、`spm_min/max` 为准。

### 4.3 计算时机

- 前端实时算（输入即变）
- 后端再算一次（提交时校验）
- 后端算的为权威

### 4.4 边界处理

- 浓度 < 0 → 当 0 处理
- 浓度缺失 → 该项按 1（优）算
- 浓度超本表最大值 → 按最大等级 6 算

---

## 5. 安全与权限

### 5.1 密码

- **以官方库为准：明文存储**（`password varchar(20)`），登录直接比对
- 不再使用 MD5 + `nep_2026_`（早期约定已被官方 dump 覆盖）
- 不要在日志中打印密码字段
- 演示账号示例：监督员 `13147859658` / `123`；管理员 `admin` / `123`

### 5.2 SQL 注入

- 全部用 MyBatis-Plus 的 LambdaQueryWrapper
- 禁止用 `${}` 拼接
- 复杂查询用 XML，不用字符串拼接

### 5.3 越权

- 公众监督员 token 不能访问管理员接口
- 网格员只能看自己被指派的任务
- 管理员能看所有
- 跨角色访问统一返回 403
- Token：内存 UUID（`TokenUtil`），映射 `tel_id`；重启后端需重新登录

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
- 发现 4：已改用 `com.mysql.cj.jdbc.Driver`；早期 MD5 方案已被 2026-07-24 官方库明文方案取代
- 发现 5：`application.yml` 数据源已统一为 `root/root`

## [2026-07-23] NEPS 公众监督员端开发
- 发现 1：NEPS 提示词 API 路径与早期规划路径不一致；以 NEPS 落地 + `docs/api-list.md` 为准
- 发现 2：组件库选 Vant 4，状态管理选 Pinia；Axios 走 Vite proxy `/api`→`8080`
- 发现 3：Token 采用内存 UUID；鉴权排除注册/登录/查重与 `/api/test/**`
- 已确认 1：Token 方案固定为内存 UUID
- 已确认 2：接口路径以「已实现」为唯一权威

## [2026-07-24] 切换官方 nep.sql
- 发现 1：官方 dump 字段与早期脚手架完全不同（监督员主键 `tel_id`；反馈 `af_id/address/information/estimated_grade/state`；无 `deleted`）
- 发现 2：官方库密码为明文且 `varchar(20)`，登录/注册改为明文比对
- 发现 3：前端仍用 `supervisorId` 字段名，值为手机号字符串；AQI/反馈对外做映射层
- 已确认：数据库权威 = `nep.sql`；接口权威 = `docs/api-list.md`
- **本条补充**：已回写第 1/3/4/5 节硬约束，废弃脚手架字段约定与 MD5 密码约定，AQI 限值表改为与官方 `aqi` 表一致

---

## 7. 上下文文件清单

AI 必须了解这些文件的位置：

- `nep-backend/src/main/resources/sql/nep.sql` - **官方库 dump（字段定义权威）**
- `nep-backend/src/main/resources/sql/init.sql` / `seed.sql` - 早期脚手架（**已过时，禁止再当权威**）
- `nep-backend/src/main/java/com/neusoft/nep/entity/` - 8 个实体类（对齐官方库）
- `nep-frontend-neps/` - 公众监督员端前端
- `docs/api-list.md` - 接口清单（已实现 + 字段映射对照）
