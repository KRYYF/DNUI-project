# PR #1 修改清单

**PR:** https://github.com/asamu18/DNUI-project/pull/1  
**作者:** Liu-mingyi  
**模块:** 决策者端 + 管理员端  
**目标:** 合入管理员/决策大屏；为 PR#2 rebase 预留接口空间

## Must fix

1. 恢复误删的 `README.md`、`doc/` 等文档
2. 删除 `.idea/`，`.gitignore` 忽略 `.idea/`、`target/`、`node_modules/`
3. `WebMvcConfig` 不要整段放行 `/api/statistics/**`
   - 只放行大屏只读接口（`provinceExceed` / `aqiDistribution` / `aqiTrend` / `realTimeCount` / `gridCoverage`）
   - `confirmedPageQuery`、`confirmedDetail` 保留鉴权

## Should fix

4. 反馈详情查 `statistics` 不要用 `fdId=telId + information`，改用稳定关联键（如反馈单号）
5. 与 PR#2 冲突文件预留扩展，不要删/改空：
   - `StatisticsService(+Impl)` 保留完整统计方法
   - `GridMemberController` 统一 `/api/gridMember/**`，为网格员登录/任务/提交留接口

## OK for now

- 明文密码、本地 DB 配置
- 统计全表内存聚合
