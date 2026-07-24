# nep-frontend-neps

东软环保公众监督系统 - 公众监督员端（NEPS）

## 技术栈

- Vue 3 + Vite
- Vue Router 4
- Pinia
- Axios
- Vant 4（移动端风格）

## 启动方式

前置：后端 `nep-backend` 已启动（端口 8080）

```bash
cd nep-frontend-neps
npm install
npm run dev
```

访问：http://localhost:8081  
代理：`/api` → `http://localhost:8080`

## 演示账号（官方 nep.sql）

| 账号 | 密码 | 说明 |
|---|---|---|
| 13147859658 | 123 | 监督员「柯镇恶」 |
| 13776567898 | 123456 | 监督员「内蒙古大学」 |
| （新注册） | ≤20 位明文 | 注册后即可登录 |

## 核心流程

注册 → 登录 → 选择网格（省-市） → 预估 AQI 并提交反馈 → 查看历史

## 目录结构

```
nep-frontend-neps/
├── vite.config.js          # 端口 8081 + /api 代理
├── src/
│   ├── api/                # 接口封装
│   │   ├── supervisor.js
│   │   ├── region.js
│   │   ├── aqi.js
│   │   └── feedback.js
│   ├── utils/
│   │   ├── request.js      # Axios 拦截器
│   │   └── aqi.js          # AQI 兜底数据
│   ├── store/user.js       # token / supervisorId / realName
│   ├── router/index.js     # 路由 + 登录守卫
│   ├── components/NavBar.vue
│   └── views/
│       ├── Register.vue
│       ├── Login.vue
│       ├── SelectGrid.vue
│       ├── Feedback.vue
│       └── HistoryList.vue
└── README.md
```

## 说明

- Token 存 `localStorage`，key：`neps_user`
- 页面最大宽度 480px 居中，移动端风格
- 字段命名与后端 / `rules.md` 对齐（驼峰）
