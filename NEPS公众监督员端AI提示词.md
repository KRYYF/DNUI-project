# 东软环保公众监督系统 - NEPS 公众监督员端 AI 提示词

> 用途：直接复制到 Cursor / Claude Code / 通义灵码等 AI 编程工具执行
> 执行人：成员 A（公众监督员端负责人）
> 预计耗时：1 个工作日
> 输出位置：项目根目录的 `nep-frontend-neps/` 文件夹
> 前置条件：后端工程 `nep-backend` 已完成并启动，端口 8080

---

## 使用说明

1. 确认 `rules.md` 在项目根目录（与 `nep-backend/`、`nep-frontend-neps/` 同级）
2. 确认后端服务已启动：`curl http://localhost:8080/api/test/hello` 返回 200
3. 整段复制下面"主提示词"到 AI 工具
4. AI 会按 6 步执行，每步完成后停下来等你确认
5. 全部跑通后输出工程在 `nep-frontend-neps/` 目录
6. AI 完成后必须回写 `rules.md` 第 6 节

---

## 主提示词（从这里开始复制）

```
你是前端工程师，正在为「东软环保公众监督系统」开发 NEPS 公众监督员端。

=========================================
【第一步：阅读规则】
=========================================
开工前必须先完整阅读项目根目录的 rules.md 文件。
这个文件定义了：
- 字段命名硬约束（与后端接口强对齐，违反即失败）
- 接口契约（路径、端口、返回结构）
- 代码规范（命名、注释、错误处理）
- AQI 计算规范（公式、限值表）
- 安全权限规则

阅读完后回复：「已阅读 rules.md，共 X 条规则，关键约束摘要：...」
不要跳过这一步，不要假装读过。

=========================================
项目背景
=========================================
- 项目名：东软环保公众监督系统 (NEP)
- 当前任务端：NEPS 公众监督员端（移动端风格，但实际是 Web）
- 用户角色：公众监督员（普通中国公民注册后使用）
- 核心流程：注册 → 登录 → 选择网格（省-市） → 预估 AQI 等级并填写反馈 → 查看历史
- 工程名：nep-frontend-neps
- 端口：8081（devServer）
- 后端地址：http://localhost:8080（已在运行）
- 浏览器目标：Chrome / Firefox / Edge 最新版

技术栈你自己定合理方案，要求：
- 主流稳定、移动端适配友好
- 组件库用 Element Plus / Vant / NutUI 任选（移动端风格优先）
- HTTP 用 Axios
- 状态管理用 Pinia 或 Vuex
- 路由用 Vue Router 4
- 构建工具用 Vite 或 Vue CLI 任选
- 不引入与业务无关的大型库

=========================================
目录结构要求
=========================================
nep-frontend-neps/
├── package.json
├── vite.config.js 或 vue.config.js
├── index.html
├── src/
│   ├── main.js
│   ├── App.vue
│   ├── api/                  # 接口定义（按业务模块分文件）
│   │   ├── supervisor.js     # 监督员相关接口
│   │   ├── region.js         # 省-市接口
│   │   ├── aqi.js            # AQI 等级接口
│   │   └── feedback.js       # 反馈接口
│   ├── utils/
│   │   ├── request.js        # Axios 封装（4 端共用模板）
│   │   └── aqi.js            # AQI 等级 → 颜色/描述工具
│   ├── store/
│   │   └── user.js           # 用户信息 Pinia/Vuex
│   ├── router/
│   │   └── index.js          # 路由 + 全局守卫
│   ├── components/
│   │   └── NavBar.vue        # 顶部导航（4 端共用）
│   ├── views/
│   │   ├── Register.vue
│   │   ├── Login.vue
│   │   ├── SelectGrid.vue
│   │   ├── Feedback.vue
│   │   └── HistoryList.vue
│   └── assets/
└── README.md

=========================================
【API 清单】（必须严格按这个写）
=========================================
所有接口 baseURL = http://localhost:8080/api
所有请求必须带 token（除登录注册外）
返回结构统一：{ code, msg, data }

1. POST /supervisor/register
   入参：{ phone, password, realName, birthDate, gender }
   返回：R{ code, msg, data: null }
   业务规则：phone 已注册则返回 code=500 msg="该手机号已注册"

2. GET /supervisor/checkPhone?phone=xxx
   返回：R{ code, msg, data: { exists: true|false } }

3. POST /supervisor/login
   入参：{ phone, password }
   返回：R{ code, msg, data: { token, supervisorId, realName } }

4. GET /region/provinces
   返回：R{ code, msg, data: [ {id, provinceName}, ... ] }

5. GET /region/cities/{provinceId}
   返回：R{ code, msg, data: [ {id, cityName, provinceId}, ... ] }

6. GET /aqi/levels
   返回：R{ code, msg, data: [ {level, grade, color, description}, ... ] }

7. POST /aqiFeedback/submit
   入参：{ supervisorId, provinceId, cityId, detailAddress, estimatedLevel, feedbackDesc }
   返回：R{ code, msg, data: null }

8. GET /aqiFeedback/myList?supervisorId=xxx
   返回：R{ code, msg, data: [ {id, provinceName, cityName, detailAddress, estimatedLevel, feedbackDesc, feedbackTime, status}, ... ] }

如果后端某个接口路径不同，你必须以 rules.md 第 1.1 节的字段命名为准去调通，不要擅自改后端。

=========================================
第二步：工程初始化
=========================================
1. 用 Vite 或 Vue CLI 创建工程，参数：
   - 工程名：nep-frontend-neps
   - 框架：Vue 3
   - TypeScript 可选（建议不用，加快速度）
2. 安装依赖：vue-router@4、pinia、axios、element-plus 或 vant
3. 配 vite.config.js / vue.config.js：
   - devServer port = 8081
   - proxy: '/api' → 'http://localhost:8080'，changeOrigin: true
4. 验证：npm run dev 能跑通，浏览器访问 http://localhost:8081 能看到 Vue 欢迎页

完成后回复：「第二步完成，工程跑起来了」

=========================================
第三步：写公共模块
=========================================
1. utils/request.js（Axios 封装，4 端共用模板）
   - baseURL: '/api'（用 proxy 转发到 8080）
   - 请求拦截器：从 Pinia user store 取 token，加到 Authorization 头
   - 响应拦截器：
     - code == 200 → return res.data
     - code == 401 → 清空 token + 跳登录页
     - code == 403 → 提示"无权限"
     - 其它 → 弹 msg 提示
   - 超时 10s

2. router/index.js
   - 路由表：
     /login → Login.vue
     /register → Register.vue
     /selectGrid → SelectGrid.vue（需登录）
     /feedback → Feedback.vue（需登录）
     /history → HistoryList.vue（需登录）
     / → 重定向到 /login
   - 全局守卫：
     - 未登录访问需登录页面 → 跳 /login
     - 已登录访问 /login 或 /register → 跳 /selectGrid

3. store/user.js
   - state: token, supervisorId, realName
   - actions: setUser, clearUser
   - 从 localStorage 恢复（key: 'neps_user'）

4. utils/aqi.js
   - export const AQI_LEVELS = [ {level, grade, color, description}, ... ]
   - export function getAqiInfo(level) → 返回该等级的 grade/color/description
   - 这份数据是写死的兜底，如果后端 /aqi/levels 接口不通就用这个

完成后回复：「第三步完成，关键代码片段：...」（贴 request.js 和 router.js 的核心逻辑）

=========================================
第四步：写 5 个页面
=========================================
页面风格：移动端风格（375px 宽为主），但实际是 Web 页面
所有页面顶部用 NavBar 组件（标题 + 返回按钮）

1. Register.vue（注册）
   表单字段（按这个顺序）：
   - 手机号（11 位，Blur 触发 /supervisor/checkPhone 查重）
   - 真实姓名
   - 出生日期（date 类型 input）
   - 性别（单选 男/女）
   - 密码（6-20 位）
   - 确认密码（要等于密码）
   - 提交按钮（loading 态，调用 /supervisor/register）
   - 底部"已有账号？去登录"链接
   校验：手机号格式、密码一致性、是否已注册（实时提示）

2. Login.vue（登录）
   - 手机号输入
   - 密码输入
   - 登录按钮（调用 /supervisor/login，成功后存 token + 跳 /selectGrid）
   - 底部"没有账号？去注册"链接
   - 失败时显示后端返回的 msg

3. SelectGrid.vue（选择网格）
   - 两个级联下拉：省 → 市
   - 页面加载时调 /region/provinces 填充省下拉
   - 省 change 时调 /region/cities/{provinceId} 填充市下拉，并清空已选项
   - "下一步"按钮（要选了省和市才能点），跳 /feedback 并把 provinceId/cityId 通过 query 传过去

4. Feedback.vue（提交反馈）
   - 接收 query 里的 provinceId/cityId
   - 顶部显示"反馈：{省名}-{市名}"（用 /region 接口或暂存里取）
   - 显示地址输入框（detailAddress）
   - 加载 AQI 6 等级参考表（调 /aqi/levels 渲染）
   - 单选预估等级（estimatedLevel）
   - 多行文本反馈描述（feedbackDesc，限 200 字，带计数器）
   - "提交"按钮：调 /aqiFeedback/submit，成功后跳 /history

5. HistoryList.vue（历史列表）
   - 页面加载时从 store 取 supervisorId，调 /aqiFeedback/myList
   - 列表项展示：时间 / 省-市 / 预估等级（带颜色标签）/ 反馈描述前 30 字
   - 下拉刷新（可选）
   - 空数据时显示"暂无反馈记录"
   - 顶部"返回"按钮回 /selectGrid

所有页面都要：
- 加载状态（loading）
- 错误处理（用 ElMessage / showToast 提示）
- 移动端风格（最大宽度 480px 居中）

完成后回复：「第四步完成，5 个页面路径：...」（列出每个文件）

=========================================
第五步：联调测试
=========================================
1. 启动后端（如果没启动）：cd ../nep-backend && mvn spring-boot:run
2. 启动前端：npm run dev
3. 用浏览器（或手机模拟器，DevTools 切到 iPhone 视图）测试完整流程：

   ① 访问 http://localhost:8081
   ② 点"去注册" → 填表：13800000002 / 测试名字 / 2000-01-01 / 男 / 123456 / 123456
   ③ 提交 → 跳登录页
   ④ 输入刚才的手机号和密码 → 登录
   ⑤ 跳选择网格页 → 选"北京市" → 市下拉自动出"北京市" → 选中
   ⑥ 点下一步 → 跳反馈页
   ⑦ 顶部显示"反馈：北京市-北京市"
   ⑧ 填具体地址、选等级 3、填描述"测试反馈"
   ⑨ 提交 → 跳历史列表
   ⑩ 看到刚才提交的那条记录

4. 验证错误场景：
   - 注册时手机号已存在 → 提示"该手机号已注册"
   - 登录密码错 → 提示"密码错误"
   - 提交反馈不选等级 → 按钮 disabled 或提示"请选择 AQI 等级"

完成后回复：
「第五步完成，测试结果：
- 注册 13800000002 成功
- 登录成功
- 选择 北京-北京 成功
- 提交反馈成功
- 历史列表显示刚才的记录
- 错误场景处理正确
截图/日志：...」

=========================================
第六步：回写 rules.md（必须执行）
=========================================
任务全部完成后，必须在 rules.md 的第 6 节追加本次发现：

## [今天的日期] NEPS 公众监督员端开发
- 发现 1：实际开发中遇到的坑（如：axios 拦截器、跨域、Vite 代理配置等）
- 发现 2：临时做的决策（如：组件库选了 vant 而不是 element-plus、状态库选了 Pinia 等）
- 发现 3：后端接口与文档不一致的地方（如有）
- 待确认 1：还没跟用户对齐的问题
- 待确认 2：...

格式严格按 rules.md 第 6 节的注释示例。

=========================================
最终交付物
=========================================
1. nep-frontend-neps/ 完整工程
2. README.md（含：启动方式、演示账号 13800000002/123456、目录结构）
3. rules.md 第 6 节已回写
4. 完整跑通的截图（注册→登录→选择→反馈→历史）

=========================================
注意事项
=========================================
- 第一步必须先读 rules.md，没读不要开工
- 严格按 API 清单调用后端，字段名不许改
- 所有接口统一走 /api 前缀（用 proxy）
- Token 存 localStorage，key 统一用 'neps_user'
- 移动端风格，最大宽 480px 居中
- 错误提示用 Element Plus 的 ElMessage 或 Vant 的 showToast
- 完成后必须回写 rules.md，否则视为任务未完成
- 遇到不存在的后端接口先停下问用户，不要自己造
```

---

## 配套信息

### 演示账号（后端已有 seed 数据）

| 角色 | 账号 | 密码 |
|---|---|---|
| 公众监督员 | 13800000001 | 123456 |
| 公众监督员（本次新注册） | 13800000002 | 123456 |

### 字段命名对齐（与后端 rules.md 1.1 节一致）

- 路径：supervisorId、provinceId、cityId、estimatedLevel、feedbackDesc
- 返回：provinceName、cityName、feedbackTime、status

### 完成标志

- [ ] 第一步确认已读 rules.md
- [ ] 工程跑起来端口 8081
- [ ] /api 代理到 8080 生效
- [ ] 5 个页面全部写好
- [ ] 完整流程跑通：注册→登录→选网格→反馈→历史
- [ ] 错误场景处理（手机号已注册、密码错、未选等级）
- [ ] README.md 写好
- [ ] rules.md 第 6 节已回写

---

## 提示词结束

> 复制上面"主提示词"部分（从 ``` 开始到最后一个 ``` 结束）即可使用。
> 如果 AI 一次跑不完，可以分步执行：只复制"第 X 步"开始的段落。
> rules.md 必须在项目根目录，且 AI 第一次回复必须确认读过。
