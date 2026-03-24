# 构建与运行流程解析

本节梳理项目构建、打包、运行流程。

## 构建工具
- Gradle：主构建工具，配置在 `build.gradle`、`settings.gradle`、各模块 `build.gradle`。

## 构建流程
1. 配置环境（JDK、Android SDK、keystore）
2. 执行 `./gradlew assembleDebug` 或 `./gradlew assembleRelease` 构建 APK
3. 签名、混淆、资源处理
4. 输出 APK 至 `app/build/outputs/apk/`

## 运行流程
- 安装 APK 至设备或模拟器
- 启动主 Activity

## 常用命令
- `./gradlew clean`：清理构建缓存
- `./gradlew build`：完整构建

---
建议结合实际命令行操作，观察构建日志与输出。
