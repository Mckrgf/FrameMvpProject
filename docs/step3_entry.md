# 关键类与入口点定位

本节定位项目关键类与入口。

## Application 入口
- `app/src/main/java/com/dahua/leapmotor/MyApp.java`：主 Application 类，负责全局初始化、第三方集成、配置加载。

## 主 Activity
- 通常在 `app/src/main/java/com/dahua/leapmotor/` 下，需进一步查找 MainActivity 或类似命名类。

## 业务核心类
- 各模块下 `main/java/` 目录，按业务分布。

## 入口流程
- Application 初始化 → 各业务模块注册 → Activity 启动 → 业务逻辑执行。

---
建议结合 MyApp.java 源码详细阅读，梳理初始化流程与全局配置。
