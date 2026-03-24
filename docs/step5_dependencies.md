# 依赖与第三方库梳理

本节梳理项目依赖与第三方库。

## Gradle 依赖
- `build.gradle`、各模块 `build.gradle`：声明依赖库
- `libs/`、`thirdAArRelease/`：本地 AAR/JAR 包

## 主要第三方库
- 阿里云、埋点、UI、权限、弹窗、视频、零域等相关库
- 具体依赖版本与来源建议查阅 build.gradle 与 libs 目录

## 管理方式
- 远程仓库（Maven、JCenter）
- 本地依赖（libs、thirdAArRelease）

---
建议结合 Gradle 文件与 libs 目录，梳理依赖关系与版本。
