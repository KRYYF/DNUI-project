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
| 分页结构 | `PageResult{ total, current, size, records }` |
| 逻辑删除 | 表字段 `deleted`，查询默认过滤已删除 |
| 跨域 | 已开放（开发环境） |

---

## 1. 已实现接口

### 1.1 连通性测试

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| GET | `/api/test/hello` | 验证服务启动 | 无 |
| GET | `/api/test/db` | 验证数据库：返回 aqi 表记录数 | 无 |

#### GET `/api/test/hello`

**响应示例**

```json
{
  "code": 200,
  "msg": "ok",
  "data": "hello nep"
}
```

#### GET `/api/test/db`

**响应示例**

```json
{
  "code": 200,
  "msg": "ok",
  "data": "aqi 表共 6 条"
}
```

---

## 2. 规划中接口（脚手架阶段尚未实现）

> 路径风格按 `rules.md`：复数名词 + RESTful 动词  
> 以下为业务模块占位，实现后请把状态改为「已实现」并补齐入参/出参。

### 2.1 认证登录

| 方法 | 路径 | 说明 | 角色 | 状态 |
|---|---|---|---|---|
| POST | `/api/auth/login` | 多角色登录（监督员手机号 / 网格员 loginCode / 管理员 loginCode） | 全部 | 规划中 |
| POST | `/api/auth/logout` | 退出登录 | 全部 | 规划中 |

### 2.2 区域

| 方法 | 路径 | 说明 | 角色 | 状态 |
|---|---|---|---|---|
| GET | `/api/provinces` | 省份列表 | 全部 | 规划中 |
| GET | `/api/cities` | 城市列表（可按 `provinceId` 过滤） | 全部 | 规划中 |

### 2.3 AQI 等级

| 方法 | 路径 | 说明 | 角色 | 状态 |
|---|---|---|---|---|
| GET | `/api/aqi` | AQI 等级字典（1-6） | 全部 | 规划中 |

### 2.4 公众监督反馈

| 方法 | 路径 | 说明 | 角色 | 状态 |
|---|---|---|---|---|
| POST | `/api/aqiFeedbacks` | 监督员提交反馈 | 监督员 | 规划中 |
| GET | `/api/aqiFeedbacks` | 反馈列表（按角色过滤） | 监督员/管理员/网格员 | 规划中 |
| GET | `/api/aqiFeedbacks/{id}` | 反馈详情 | 相关角色 | 规划中 |
| PUT | `/api/aqiFeedbacks/{id}/assign` | 管理员指派网格员 | 管理员 | 规划中 |

### 2.5 实测统计

| 方法 | 路径 | 说明 | 角色 | 状态 |
|---|---|---|---|---|
| POST | `/api/statistics` | 网格员提交实测（含 AQI 计算校验） | 网格员 | 规划中 |
| GET | `/api/statistics` | 统计列表/大屏数据 | 管理员/决策者 | 规划中 |
| GET | `/api/statistics/{id}` | 统计详情 | 相关角色 | 规划中 |

### 2.6 用户管理

| 方法 | 路径 | 说明 | 角色 | 状态 |
|---|---|---|---|---|
| GET | `/api/gridMembers` | 网格员列表 | 管理员 | 规划中 |
| POST | `/api/gridMembers` | 新增网格员 | 管理员 | 规划中 |
| GET | `/api/supervisors` | 监督员列表 | 管理员 | 规划中 |
| POST | `/api/supervisors` | 监督员注册 | 公开/监督员 | 规划中 |

---

## 3. 变更记录

| 日期 | 变更 |
|---|---|
| 2026-07-23 | 初版：记录脚手架已实现的 2 个测试接口，并列出业务模块规划路径 |
