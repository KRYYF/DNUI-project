# NEP 接口清单

> 维护说明：后端每新增/变更接口，必须同步更新本文件  
> 基础地址：`http://localhost:8080`  
> 统一前缀：`/api`  
> 统一返回：`R{ code, msg, data }`（成功 `200`，业务错误 `500`，未授权 `401`，权限不足 `403`）  
> **数据库权威**：`nep-backend/src/main/resources/sql/nep.sql`（官方 dump）

---

## 0. 通用约定

| 项 | 说明 |
|---|---|
| Content-Type | `application/json` |
| Authorization | 登录后返回的 token（除注册/登录/查重/测试接口外必带）。**方案：内存 UUID**（`TokenUtil`），映射监督员手机号 `tel_id`；后端重启后需重新登录 |
| 路径权威 | 以本文件「已实现」为准 |
| 密码 | 官方库为**明文**（`varchar(20)`），与 dump 一致；不再使用 MD5 |
| 主键说明 | 监督员主键为手机号 `tel_id`；前端字段名仍用 `supervisorId`，值为手机号字符串 |

### 0.1 表字段对照（官方库）

| 表 | 主键 | 关键字段 |
|---|---|---|
| `supervisor` | `tel_id` | `password`, `real_name`, `birthday`, `sex`(1男/0女) |
| `grid_province` | `province_id` | `province_name`, `province_abbr` |
| `grid_city` | `city_id` | `city_name`, `province_id` |
| `aqi` | `aqi_id` | `aqi_explain`, `color`, `health_impact`, 浓度上下限 |
| `aqi_feedback` | `af_id` | `tel_id`, `address`, `information`, `estimated_grade`, `af_date`, `af_time`, `state`(0未指派/1已指派/2已确认) |
| `grid_member` | `gm_id` | `gm_code`, `gm_name`, `password`, `province_id`, `city_id`, `state` |
| `admins` | `admin_id` | `admin_code`, `password` |
| `statistics` | `id` | `so2_value/level`, `co_value/level`, `spm_value/level`, `aqi_id`, `gm_id`, `af_id`(关联反馈单号), `fd_id`(监督员电话) |

---

## 1. 已实现接口

### 1.1 连通性测试

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| GET | `/api/test/hello` | 验证服务启动 | 无 |
| GET | `/api/test/db` | 返回 aqi 表记录数 | 无 |

### 1.2 公众监督员（NEPS）

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| POST | `/api/supervisor/register` | 注册 | 无 |
| GET | `/api/supervisor/checkPhone?phone=` | 手机号是否已注册 | 无 |
| POST | `/api/supervisor/login` | 登录 | 无 |

**注册入参**

```json
{ "phone": "13800000003", "password": "123456", "realName": "测试", "birthDate": "2000-01-01", "gender": "男" }
```

| 前端字段 | 写入库字段 | 说明 |
|---|---|---|
| phone | `tel_id` | 主键 |
| password | `password` | 明文，≤20 |
| realName | `real_name` | |
| birthDate | `birthday` | 缺省 `2000-01-01` |
| gender | `sex` | 男→1，女→0 |

**登录入参：** `{ phone, password }`  
**登录成功 data：**

```json
{ "token": "...", "supervisorId": "13147859658", "realName": "柯镇恶" }
```

> `supervisorId` = 手机号 `tel_id`（字符串）

**业务错误：** 该手机号已注册 / 账号不存在 / 密码错误 → `code=500`

### 1.3 区域

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| GET | `/api/region/provinces` | 省份列表 | 需 token |
| GET | `/api/region/cities/{provinceId}` | 城市列表 | 需 token |

**省份 data 项：** `{ "id": 1, "provinceName": "北京市" }`（`id`←`province_id`）  
**城市 data 项：** `{ "id": 1, "cityName": "北京市", "provinceId": 1 }`（`id`←`city_id`）

### 1.4 AQI 等级

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| GET | `/api/aqi/levels` | AQI 1-6 等级 | 需 token |

**data 项（对外字段 ← 库字段）：**

| 对外 | 库字段 |
|---|---|
| level | `aqi_id` |
| grade | `aqi_explain`（优/良/...） |
| color | `color` |
| description | `health_impact` |

### 1.5 反馈

| 方法 | 路径 | 说明 | 权限 |
|---|---|---|---|
| POST | `/api/aqiFeedback/submit` | 提交反馈 | 需 token |
| GET | `/api/aqiFeedback/myList?supervisorId=` | 我的反馈列表 | 需 token |

**提交入参**

```json
{
  "supervisorId": "13147859658",
  "provinceId": 1,
  "cityId": 1,
  "detailAddress": "朝阳区xx",
  "estimatedLevel": 3,
  "feedbackDesc": "有雾霾"
}
```

| 前端字段 | 写入库字段 |
|---|---|
| supervisorId | `tel_id` |
| provinceId | `province_id` |
| cityId | `city_id` |
| detailAddress | `address` |
| estimatedLevel | `estimated_grade` |
| feedbackDesc | `information` |
| （服务端） | `af_date`/`af_time` 当前时间，`gm_id=0`，`state=0` |

**列表项 data：**

```json
{
  "id": 1,
  "provinceName": "北京市",
  "cityName": "北京市",
  "detailAddress": "朝阳区建国路123号",
  "estimatedLevel": 3,
  "feedbackDesc": "空气能见度不足，稍有异味。",
  "feedbackTime": "2022-01-26 09:28:04",
  "status": "未指派"
}
```

| 对外 | 来源 |
|---|---|
| id | `af_id` |
| detailAddress | `address` |
| estimatedLevel | `estimated_grade` |
| feedbackDesc | `information` |
| feedbackTime | `af_date` + ` ` + `af_time` |
| status | `state`：0未指派 / 1已指派 / 2已确认 |

---

## 2. 规划中接口（其他端）

| 方法 | 路径 | 说明 | 角色 | 状态 |
|---|---|---|---|---|
| 网格员登录/任务 | `/api/...` | 基于 `grid_member` / `aqi_feedback.state` | 网格员 | 规划中 |
| 管理员指派 | `/api/...` | 更新 `gm_id`/`state` | 管理员 | 规划中 |
| 实测提交 | `/api/...` | 写入 `statistics` | 网格员 | 规划中 |

---

## 3. 演示账号（官方 nep.sql）

| 角色 | 账号 | 密码 |
|---|---|---|
| 公众监督员 | 13147859658 | 123 |
| 公众监督员 | 13776567898 | 123456 |
| 网格员 | caocao | 123 |
| 管理员 | admin | 123 |

---

## 4. 变更记录

| 日期 | 变更 |
|---|---|
| 2026-07-23 | 初版脚手架字段（已废弃） |
| 2026-07-23 | NEPS 联调（旧表结构） |
| 2026-07-24 | **切换官方 `nep.sql`**：实体/接口映射对齐；密码改明文；`supervisorId`=手机号；同步本清单 |