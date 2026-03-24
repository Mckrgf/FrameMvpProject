# `MyApp` 功能梳理

文件：`app/src/main/java/com/dahua/leapmotor/MyApp.java`

## 1. 角色定位

`MyApp` 继承自 `Application`，是全局初始化与运行状态管理中枢，主要负责：

- 进程级初始化（多 Dex、WebView 目录、SDK/服务注册）
- 隐私同意前后分阶段初始化
- Activity 生命周期监听与前后台状态管理
- 定时任务（内存/驻留行为、网络流量监控）
- 全局能力桥接（推送、路由、埋点、数据库、X5、Sentry 等）

---

## 2. 启动主链路

### 2.1 `attachBaseContext`

- `MultiDex.install(this)`：安装多 Dex。
- `initWebViewDataDirectory(base)`：Android P+ 非主进程设置 WebView 数据目录后缀，避免多进程 WebView 冲突。

### 2.2 `onCreate`

主要流程：

1. 预注册 IM 推送（注释说明要求“要提前”）。
2. Debug 下初始化全局 `CrashHandler`。
3. 记录启动时间（`startTime` / `runTime`），保存 `currentApp`。
4. 初始化调查工具、`SharedPreferences`、`ApplicationContextGetter`。
5. 判断是否主进程（`isAppMainProcess()`）。
6. 读取隐私同意状态（`PhoneUtils.hasAgreePrivacy()`）。
7. 初始化通用能力（`Toaster`、SP 参数、`initOnCreate()`）。
8. 若已同意隐私，继续执行 `initAfterAgreePrivacy()`。
9. 记录日志并初始化微博 SDK。

---

## 3. 分阶段初始化（按隐私状态）

## 3.1 始终执行：`initOnCreate()`

在主进程执行的内容：

- 车联网网络 SDK：`LPCarSdkManager.initLPCarNetWorking`
- 用户、缓存、服务注册：`UserUtil`、`CacheUtil`、`ServiceManager.registerService`
- 调查/通知首装时间初始化
- `initRandom()`、`initActivity()`、`initALog()`
- 线程池初始化、网络监听、路由初始化、RxJava 全局错误处理
- 异步初始化 Room 数据库 `AppDatabase`
- 悬浮窗状态重置

无论是否主进程都会执行：

- `startNetworkTrafficWatch()`：启动周期性流量统计任务

### 3.2 隐私同意后：`initAfterAgreePrivacy()`

仅主进程执行，核心内容：

- `FileProvider` 路径动态检查与补充
- 注册阿里云视频 SDK（`AlivcSdkCore.register`）
- 非 Debug 初始化 UMeng 调查
- 初始化砸金蛋状态、弹窗状态、城市/服务端时间偏移
- 初始化阿里推送、OneLogin、抖音分享
- 注册系统网络广播接收器（`MqttReceiver`）
- 配置并初始化 X5/TBS（`QbSdk.initTbsSettings` + `initX5Environment`）
- 初始化 Sentry（DSN、PII、Tracing、截图、View 树、Profile、Session Replay、Release、Environment）
- 设置 Sentry 用户信息（来自 `UserUtil.getUser()`）
- 启动内存/驻留行为定时任务（`startMemorySuvery()`）
- 初始化电台播放监听并控制悬浮窗显示逻辑

### 3.3 点击隐私后追加：`initAfterClickPrivacy()`

仅执行一次（`isInitAfterClick` 防重）：

- 视频预加载初始化
- 第三方服务：初始化电台与 IM 监听

### 3.4 运行时隐私状态变化：`userAgree(boolean)`

- 状态未变化直接返回
- 更新全局隐私状态与本地持久化
- 清空用户 `clientId` 并提交用户信息
- 同步车联网 SDK 隐私状态
- 同意时补做：设置 `phoneId`、`initTalkingData()`、`initAfterAgreePrivacy()`

---

## 4. Activity 生命周期与前后台切换

`initActivity()` 注册了 `ActivityLifecycleCallbacks`，是全局状态机核心。

### 4.1 关键状态变量

- `topActivity`：当前栈顶 Activity
- `appCount`：前台 Activity 计数
- `isRunInBackground`：App 是否在后台
- `isCodeStart`：冷启动标记

### 4.2 生命周期行为摘要

- `onActivityCreated`
  - 路由页面信息注入（排除 `HomePageActivity` / `LpSplashActivity`）
  - 首次且已同意隐私时初始化 TalkingData
  - 登录页链路标记与清理（`PathProvider`）
  - 更新 `topActivity`

- `onActivityStarted`
  - 首次启动触发：`isCodeStart=false`、加载热修复补丁、检查日志上传
  - `appCount++`，记录流量统计
  - `appCount==1` 视为切到前台，记录状态切换
  - 若从后台回前台：拉开屏数据、保活服务、发前台广播、下载 SSL 配置

- `onActivityResumed`
  - 页面路径注入、埋点进入上报
  - 电台悬浮窗的创建/移除/附着逻辑

- `onActivityPaused`
  - 结束中的 Activity 若不是登录页，清理登录路径统计

- `onActivityStopped`
  - 埋点离开上报，`appCount--`
  - `appCount==0` 视为进入后台：发后台广播、上报离开、设置别名图标、清理服务、记录退出、电台状态落盘

- `onActivityDestroyed`
  - 若销毁的是 `topActivity` 则清空
  - 释放路由 Bundle、登录链路结束标记、解绑视频视图、关闭 IM 提示弹窗

---

## 5. 定时任务与周期行为

### 5.1 `startMemorySuvery()`

- 每 10 秒执行一次。
- 仅主进程且只启动一次（`hasStartMemery` 防重）。
- 当 App 在后台时：
  - 持续记录电台信息 `recordRadioInfo(false)`
  - 后台停留达到 `ACTION_TIME`（默认 3600 秒）且未结束全部页面时，触发 `oneHourAction()`。

`oneHourAction()` 行为：

- 发送 `LP_CLEAR_OTHER_FRAGMENT` 广播
- 关闭 `HomePageActivity` 之外子页面（`ActivityCollector.finishAllSubpagePage`）

### 5.2 `startNetworkTrafficWatch()`

- 启动后 10 秒首次执行，之后每 10 分钟执行。
- 调用 `networkTrafficStatistic("routine", KEY_ROUTINE_WATCH)`，记录流量增量日志。

---

## 6. 关键模块清单

- **推送**：`LeapmotorIMInitManager.registerPush()`（提前），`initAliPush()` 初始化阿里推送。
- **WebView/X5**：多进程目录隔离 + X5 内核配置和预初始化。
- **稳定性监控**：Debug `CrashHandler`；Sentry 全链路（错误、性能、会话回放）。
- **路由**：`LPRouter` 页面与参数注入，`PathProvider` 登录链路追踪。
- **数据库**：Room `AppDatabase` 异步初始化。
- **线程与响应式**：全局线程池、RxJava 全局错误兜底。
- **业务埋点**：进入/离开、页面进出、冷启动节点相关埋点。
- **第三方服务桥接**：电台、IM、OneLogin、分享、车联网等统一在 Application 层调度。

---

## 7. 代码中可关注的风险点

- `stopMemorySuvery()` 直接 `memoryTimer.cancel()`，若未初始化可能空指针（如某些进程未启动该定时器）。
- `appCount` 依赖生命周期计数，极端场景（异常重入/多窗口）可能出现边界偏差，建议增加保护日志与下限保护。
- `Sentry` 采样参数配置较高（`traces/profiles/replay` 接近 100%），生产环境需评估性能和流量成本。
- `ACTION_TIME` 表达式在当前代码中 `Config.isOfficial ? 60 * 60 : 60 * 60` 两分支相同，可简化或明确区分环境策略。
- `initFileProvider()` 中检查结果变量 `configuredPaths/configuredPaths1` 未使用，建议改为明确校验日志，避免“看似校验但无行为”。

---

## 8. 一句话总结

`MyApp` 将“隐私分阶段初始化 + 生命周期状态机 + 定时治理任务 + 多 SDK 编排”集中在 `Application` 层，实现了较完整的全局控制能力，但也带来初始化体量大、时序复杂和可维护性压力，需要通过分层与可观测性持续治理。

