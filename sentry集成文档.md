# Sentry for Android 接入方式指南

本指南详细介绍了在 Android 项目中接入 Sentry SDK 的两种方式：**插件自动接入**（推荐）与 **纯手动接入**。

---

## 方式一：插件自动接入（官方强烈推荐）

使用 Sentry Android Gradle Plugin 介入构建过程。这是最省心、功能最全的接入方式。

### 核心优势
* **全自动 Mapping 上传**：打包 Release 版开启代码混淆（`minifyEnabled true`）时，插件会在后台自动将 `mapping.txt` 上传到 Sentry，彻底解决后台崩溃日志乱码问题。
* **自动注入依赖**：无需手动在 `dependencies` 中添加核心 SDK。
* **字节码插桩 (Auto-instrumentation)**：自动追踪网络请求耗时、数据库查询、UI 渲染等高级性能指标。

### ⚠️ 特别注意：针对 AGP 7.x 项目
由于最新的 Sentry 插件（4.x+）要求 AGP 8.0+ 和 Java 17，如果你的项目仍在使用 **AGP 7.x**，**必须降级使用 Sentry 3.x 版本的插件**（例如 `3.12.0` 是一个兼容性极佳的版本）。

### 接入步骤

**1. 引入插件依赖 (项目级 `build.gradle` 或 `settings.gradle`)**
```gradle
plugins {
    // 兼容 AGP 7.x，使用 3.x 版本插件
    id "io.sentry.android.gradle" version "3.12.0" apply false
}
```

**2. 应用插件与配置 (App 模块级 `build.gradle`)**
```gradle
plugins {
    id 'com.android.application'
    id 'io.sentry.android.gradle' // 应用 Sentry 插件
}

sentry {
    // 【可选配置】默认插件会自动引入 SDK。
    // 如果你想完全自己控制 SDK 的版本，可以关闭自动引入：
    includeDependencies = false
}

dependencies {
    // 如果上面 includeDependencies = false，或者你想强制覆盖为特定 SDK 版本，在这里手动声明：
    implementation 'io.sentry:sentry-android:7.10.0' // 替换为你想要的 SDK 版本
}
```

**3. 初始化配置 (`AndroidManifest.xml`)**
在 `<application>` 标签内配置你的 DSN：
```xml
<meta-data android:name="io.sentry.dsn" android:value="YOUR_SENTRY_DSN_HERE" />
```

---

## 方式二：手动接入（Manual Installation）

不使用任何 Sentry 编译期插件，仅仅作为普通的第三方库引入。

### 核心特点
* **零构建侵入**：不影响任何 Gradle 编译流程，不增加额外编译耗时。
* **完全自主可控**：按需引入所需的功能模块。
* **最大痛点：需手动处理混淆映射**：由于没有插件帮忙，开启混淆打包后，**必须手动使用 `sentry-cli` 或编写 CI/CD 脚本上传 `mapping.txt` 文件**，否则 Sentry 后台收到的崩溃日志将全是 `a.b.c` 这种无法阅读的乱码。
* **无自动性能追踪**：失去编译期的字节码插桩能力，需要手动编写代码拦截网络和埋点。

### 接入步骤

**1. 添加依赖 (App 模块级 `build.gradle`)**
不应用任何 Sentry 插件，直接在 dependencies 中添加：
```gradle
dependencies {
    // 完全手动引入指定版本的 SDK
    implementation 'io.sentry:sentry-android:7.10.0'
}
```

**2. 初始化配置 (`AndroidManifest.xml` 或 代码初始化)**
同样需要在 Manifest 中配置 DSN，或者在 Application 的 `onCreate` 中通过代码初始化：
```java
SentryAndroid.init(this, options -> {
    options.setDsn("YOUR_SENTRY_DSN_HERE");
    // 手动配置其他参数
});
```

**3. 手动上传 Mapping 文件（发版必备）**
每次打出 Release 包后，找到 `app/build/outputs/mapping/release/mapping.txt`，然后通过 Sentry 提供的命令行工具 (`sentry-cli`) 或在 CI/CD 流水线中手动执行上传命令。

---

## 综合对比总结

| 特性 / 方式 | 插件自动接入 (Sentry Gradle Plugin) | 手动接入 (Manual Installation) |
| :--- | :--- | :--- |
| **推荐程度** | ⭐⭐⭐⭐⭐ (官方推荐) | ⭐⭐ |
| **SDK 依赖管理** | 插件自动引入（也可手动覆盖版本） | 纯手动写死版本 |
| **混淆文件上传 (Mapping)** | **全自动无感上传 (极大提升幸福感)** | **必须手动用命令行或脚本上传** |
| **日志可读性** | 完美（自动反混淆） | 极易出现乱码（除非配置正确的手动上传） |
| **高级性能监控 (APM)** | 支持（自动插桩网络、UI、数据库等耗时）| 仅支持基础功能，高级功能需手写埋点 |
| **构建侵入性** | 中等（增加编译期间的插桩和上传耗时） | 极低（与普通三方库无异） |
| **适用场景** | 99% 的商业项目 | 对编译耗时极其敏感，或安全规范严禁三方插件的项目 |

### 💡 针对 Release 发版与加固的最佳实践建议：
为了保证代码安全、包体积最优以及崩溃日志的准确性，无论使用哪种接入方式，请确保：
1. **主工程开启混淆**：`app/build.gradle` 中设置 `minifyEnabled true`。
2. **正确上传映射文件**：强烈建议用插件自动上传。
3. **加固顺序**：生成正常的、带混淆的 Release 包 -> (此时插件已将 mapping 上传) -> 对最终的 APK/AAB 进行第三方加固。