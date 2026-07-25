# 东软环保公众监督系统 - 决策者大屏端 (NEPV)

## 快速启动

```bash
# 1. 进入前端目录
cd D:\dongruan\DNUI-project\nep-frontend-nepv

# 2. 安装依赖
npm install

# 3. 启动开发服务器
npm run dev

# 4. 浏览器访问
http://localhost:8084
```

浏览器打开 `http://localhost:8084/`

## 后端依赖

```bash
# 启动后端
cd D:\dongruan\DNUI-project\nep-backend
mvn spring-boot:run -DskipTests
```

## 构建部署

```bash
npm run build     # 输出到 dist/
npm run preview   # 预览构建结果
```
