# NEP 接口清单

> 维护说明：后端每新增/变更接口，必须同步更新本文件  
> 基础地址：`http://localhost:8080`  
> 统一前缀：`/api`  
> 统一返回：`R{ code, msg, data }`（成功 `200`，业务错误 `500`，未授权 `401`，权限不足 `403`）

---

## 0. 通用约定

| 项 | 说明 |
|---|---|
| Content-Type | `application/json` |
| Authorization | 登录后返回的 token（除注册/登录/查重/测试接口外必带）。**方案：内存 UUID**（`TokenUtil`），后端重启后需重新登录；不使用 JWT |
| 路径权威 | 以本文件「已实现」为准；其他端禁止使用早期规划路径（如 `/api/provinces`） |
| 分页结构 | `PageResult{ total, current, size, records }` |
| 逻辑删除 | 表字段 `deleted`，查询默认过滤已删除 |
| 跨域 | 已开放（开发环境）；NEPS 前端用 Vite proxy |

---

## 1. 已实现接口

### 1.1 连通性测试

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| GET | `/api/test/hello` | 验证服务启动 | 无 |
| GET | `/api/test/db` | 验证数据库：返回 aqi 表记录数 | 无 |

### 1.2 公众监督员（NEPS）

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| POST | `/api/supervisor/register` | 注册 | 无 |
| GET | `/api/supervisor/checkPhone?phone=` | 手机号是否已注册 | 无 |
| POST | `/api/supervisor/login` | 登录，返回 `{token, supervisorId, realName}` | 无 |

**注册入参：** `{ phone, password, realName, birthDate, gender }`  
**登录入参：** `{ phone, password }`  
**业务错误：** 手机号已注册 / 密码错误 → `code=500`

### 1.3 区域

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| GET | `/api/region/provinces` | 省份列表 `{id, provinceName}` | 需 token |
| GET | `/api/region/cities/{provinceId}` | 城市列表 `{id, cityName, provinceId}` | 需 token |

### 1.4 AQI 等级

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| GET | `/api/aqi/levels` | AQI 1-6 等级 `{level, grade, color, description}` | 需 token |

### 1.5 反馈

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| POST | `/api/aqiFeedback/submit` | 提交反馈 | 需 token |
| GET | `/api/aqiFeedback/myList?supervisorId=` | 我的反馈列表 | 需 token |

**提交入参：** `{ supervisorId, provinceId, cityId, detailAddress, estimatedLevel, feedbackDesc }`  
**列表项：** `{ id, provinceName, cityName, detailAddress, estimatedLevel, feedbackDesc, feedbackTime, status }`

---

## 2. 规划中接口（其他端）

| 方法 | 路径 | 说明 | 角色 | 状态 |
|---|---|---|---|---|
| POST | `/api/auth/logout` | 退出登录 | 全部 | 规划中 |
| PUT | `/api/aqiFeedbacks/{id}/assign` | 管理员指派网格员 | 管理员 | 规划中 |
| POST | `/api/statistics` | 网格员提交实测 | 网格员 | 规划中 |
| GET | `/api/statistics` | 统计/大屏 | 管理员/决策者 | 规划中 |
| GET/POST | `/api/gridMembers` | 网格员管理 | 管理员 | 规划中 |

---

## 3. 变更记录

| 日期 | 变更 |
|---|---|
| 2026-07-23 | 初版：测试接口 + 规划占位 |
| 2026-07-23 | NEPS 联调：落地监督员/区域/AQI/反馈 8 个接口，路径以 NEPS 提示词为准 |
| 2026-07-23 | 团队确认：Token=内存 UUID；接口路径以本清单「已实现」为唯一权威 |
