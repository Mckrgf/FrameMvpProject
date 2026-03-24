# 项目结构梳理

本节详细梳理 D:/aaWorkCode/android 项目的目录结构及各部分作用。

## 主目录
- `app/`：主应用模块，包含核心业务代码、资源、配置。
- `AliyunMedia/`、`AliyunVideoCommon/`、`LPAnalytics/`、`LPUIKitLibrary/`、`listenerlibrary/`、`NeighborLibrary/`、`PermissionLibrary/`、`popLibrary/`、`ThirdDevelopLibrary/`、`videoselector/`、`zerozone/` 等：各自独立功能模块或库。
- `thirdAArRelease/`、`thirdparty-lib/`：第三方库及依赖。
- `build.gradle`、`settings.gradle`、`gradle.properties`、`local.properties`：项目构建与配置文件。
- `keystore/`、`app/keystore/`：签名证书。
- `config/`、`submodules/`、`repo/`：配置、子模块、仓库。

## 资源与配置
- `fonts.otf`：字体资源。
- `proguard-rules.pro`、`consumer-rules.pro`：混淆与消费者规则。
- `aliyun-emas-services.json`、`robust.xml`：第三方服务与热修复配置。

## 代码分布
- 各模块下 `src/main/` 为主业务代码，`src/androidTest/`、`src/test/` 为测试代码。

---
本节为后续模块分析、入口定位等提供结构基础。
