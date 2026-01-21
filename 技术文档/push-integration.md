# 阿里云推送 + 厂商通道集成指引

> 适用：当前仓库（包名 `com.dahua.leapmotor`），阿里云移动推送 + 华为/荣耀/小米/OPPO/vivo/魅族厂商通道，结合 `TIMPushManager` 聚合（见 `ThirdDevelopLibrary/src/main/java/com/third/develop/im/LeapmotorIMInitManager.kt`）。

## 1. 概览
- 目标：在国内机型上通过厂商系统级通道提升送达率，阿里云自有通道做兜底。
- 架构：App 集成阿里云 Push SDK + 各厂商 AAR；`TIMPushManager` 调用 `setPushConfig`/`getPushToken`；服务端按厂商 token 路由，失败回退阿里云。
- 环境：生产配置 `app/src/main/assets/timpush-configs.json`，测试配置 `app/src/main/assets/debug-timpush-configs.json`。`LeapmotorIMInitManager.setPushConfig()` 在非正式环境加载 debug 配置后获取 token。

## 2. 前置条件
- 账号：阿里云移动推送账号 + 各厂商推送平台账号（个人/企业均可）。
- 包名/签名：控制台配置必须与最终包一致；测试包建议独立包名/签名 + 独立密钥。
- 资质：特殊行业（金融/医疗/新闻/直播/游戏等）可能需额外资质；隐私合规需在启动前弹隐私授权。
- 证书/密钥：
  - 阿里云：Android AppKey/AppSecret（客户端），AccessKey（服务端）。
  - 厂商：华为/荣耀、小米、OPPO、vivo、魅族各自的 AppId/AppKey/AppSecret/证书文件。

## 3. 控制台配置
### 3.1 阿里云移动推送
1) 创建应用：填包名 `com.dahua.leapmotor`，生成 Android AppKey/AppSecret。
2) 厂商通道页：逐一填入厂商的 AppId/AppKey/AppSecret/证书文件，开启对应通道。
3) 消息模板：配置通知/透传、点击动作（deeplink/activity）、角标/富媒体（可选）。
4) 分环境：测试应用使用测试包名/签名与独立密钥，避免串号。

### 3.2 厂商开放平台要点（与 `timpush-configs*.json` 对应）
- 华为/荣耀：AGC 创建应用，启用 Push Kit；下载 `agconnect-services.json` 放入 `app/`；控制台记录 `huaweiPushBussinessId`。生产: `42798`，测试: `44268`。
- vivo：开放平台创建应用，上传签名；使用 `manifestPlaceholders` 中的 `VIVO_APPID`/`VIVO_APPKEY`；业务 ID 生产: `42800`，测试: `44271`。
- OPPO：创建应用并上传签名，获取 `oppoPushAppKey`/`oppoPushAppSecret`，业务 ID 生产: `42801`，测试: `44273`。
- 小米：创建应用获取 `xiaomiPushAppId`/`xiaomiPushAppKey`，业务 ID 生产: `42207`，测试: `44266`；确保 MIUI 推送开关开启。
- 魅族：记录 `meizuPushAppId`/`meizuPushAppKey`，业务 ID 生产: `42799`，测试: `44270`。
- 荣耀（独立值）：`honorPushBussinessId` 生产: `44588`，测试: `44589`。

## 4. Android 端集成
### 4.1 依赖与资源
- 引入阿里云 Push SDK 及各厂商 AAR（已在工程 libs/gradle 配置中）。
- 将 `timpush-configs.json` 与 `debug-timpush-configs.json` 放在 `app/src/main/assets/`。
- 华为/荣耀需 `agconnect-services.json` 放在 `app/`。
- Manifest：保留厂商 SDK 自动合并的 Service/Receiver；申请通知权限（Android 13+ 运行时）。

### 4.2 初始化流程（参考 `LeapmotorIMInitManager`）
```kotlin
fun init() {
    setPushConfig() // Debug 时加载 debug 配置
    TIMPushManager.getInstance().disablePostNotificationInForeground(false)
    registerPush() // 监听通知点击/透传
}

private fun setPushConfig() {
    if (!URLConfig.isOfficial) {
        val param = JSONObject().put("customConfigFile", "debug-timpush-configs.json")
        TIMPushManager.getInstance().callExperimentalAPI("setPushConfig", param.toString(), callback)
    }
    getPushToken()
}

private fun getPushToken() {
    TIMPushManager.getInstance().callExperimentalAPI("getPushToken", null, callback)
    // onSuccess -> reportPushToken(token)
}
```
- 获取 token 后调用 `ServiceManager.getService(MainAppService)::reportPushToken(token)` 上报。
- 登录成功回调中调用 `registerTIMPush()`，再次触发 token 获取与上报。
- `TIMPushListener` 的 `onNotificationClicked`/`onRecvPushMessage` 负责路由与前台提醒。

### 4.3 Token 上报策略
- 成功获取立即上报；应用重启、升级、清数据、切换账号后重上报。
- 绑定维度：未登录绑定 deviceId，登录后绑定 userId + deviceId。
- 记录失败码用于埋点与重试。

### 4.4 前台/后台行为
- `disablePostNotificationInForeground(false)` 允许前台弹通知；如需前台自渲染可改为 true 并在回调中手动展示。
- 透传/通知点击通过 `resolveNotificationExt` 解析 `ext` 字段并路由。

## 5. 服务端下发与路由
- 绑定表：userId/deviceId ↔ 厂商 token、阿里云 deviceId、最近活跃通道。
- 路由策略：
  1) 优先按设备厂商走对应厂商通道（使用上报的厂商 token）。
  2) 无 token 或失败回退到阿里云自有通道；海外可走 FCM/自建。
  3) 记录结果码，避免重复弹窗，必要时重试。
- Payload 规范：
  - 通知：title/body + 点击动作（deeplink/activity）+ 自定义 ext（供客户端路由）。
  - 透传：精简字段，可附签名校验。
- 环境隔离：生产/测试的 AppKey、密钥、包名、签名、Topic/Tag 必须分离。

## 6. 验证与排查
- 真机对应厂商安装测试包，观察日志 `getPushToken onSuccess ...` 是否产出 token。
- 在阿里云或厂商控制台用该 token 做单播测试，确认送达与点击路由。
- 常用命令：
```bash
adb logcat | findstr -i push
adb shell dumpsys notification
```
- 检查通知权限（Android 13+）、系统推送开关（小米/OPPO/vivo 等）。
- 查看 `setPushConfig errorCode/errMsg` 与 `getPushToken error` 日志定位密钥/配置问题。

## 7. 常见坑
- 包名/签名与控制台不一致 → 厂商 token 为空。
- 未在隐私同意前初始化 SDK → 审核/合规风险，可能被系统限制。
- 未申请通知权限（Android 13+）→ 首次安装无法弹通知。
- 厂商通道密钥/证书填错或未开启 → 长期 0 送达。
- token 未随账号变更刷新 → 推送命中错误用户。
- 华为/荣耀需启用 Push Kit 并使用 HMS 包；OPPO/vivo 需上传签名文件；小米需确保系统级推送开关未被关闭。

## 8. 建议动作
- 把本文提交到仓库 `docs/push-integration.md`（已创建）。
- 在 `getPushToken` 回调处增加上报重试与埋点（失败码统计）。
- 服务端监控各通道命中率/失败码/回退率，定期清理失效 token。
- 测试/生产包使用独立配置与签名，避免串环境。
