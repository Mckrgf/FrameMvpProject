# Sentry Android 术语与读图指南

> 适用项目：当前 Android 工程  
> 相关代码：
> - `file:///D:/aaWorkCode/android/app/src/main/java/com/dahua/leapmotor/MyApp.java`
> - `file:///D:/aaWorkCode/android/app/src/main/java/com/dahua/leapmotor/ui/HomePageActivity.java`

---

## 1. 先用一句话理解 Sentry

Sentry 可以理解为一个**“线上问题和性能链路观测平台”**。

它主要帮我们看三类东西：

1. **错误/异常**：Crash、ANR、Java 异常
2. **性能**：页面打开慢、接口慢、卡顿、冻结帧、启动慢
3. **运行上下文**：用户、版本、环境、设备、Breadcrumb、Profile、Replay

---

## 2. 最核心的 4 个概念

## 2.1 Event 是什么

`Event` 是 Sentry 里最基础的一条记录。

可以把它理解为：

- 一条错误上报
- 一条性能上报
- 一条消息上报
- 一次事务记录

也就是说，**Sentry 里很多东西底层都可以视为 Event**。

### 常见 Event 类型
- Error Event：异常/崩溃
- Transaction Event：性能事务
- Message Event：普通日志消息

---

## 2.2 Transaction 是什么

`Transaction` 是一种特殊的 `Event`，专门用来表示**一次完整操作的耗时过程**。

它通常表示：

- 打开一个页面
- 一次点击动作
- 一次启动过程
- 一次业务流程

### 例子
- 打开首页
- 冷启动到首页首屏
- 用户点击“提交订单”直到返回结果

### 在你们项目里的例子
在 `HomePageActivity.java` 里，你们手动创建了一个 transaction：

- `Sentry.startTransaction(HOME_PROFILE_TRANSACTION_NAME, "ui.load", options)`

这表示你们人为定义了一个首页加载的性能事务。

可以理解成：

> “我要告诉 Sentry：现在开始记录一次首页可见链路的耗时。”

---

## 2.3 Span 是什么

`Span` 是 `Transaction` 里面的一个**时间片/子步骤**。

如果说 transaction 是“整件事”，那 span 就是“这件事里的每个阶段”。

### 例子
一次“打开首页”的 transaction，里面可能包含这些 span：

- `ui.load.initial_display`
- `ui.load`
- `http.client`
- `db.query`
- `json.parse`
- `image.decode`

### 怎么理解
例如：

> Transaction = “打开首页”  
> Span = “首页里每个子动作分别花了多久”

所以：

- transaction 看**整体慢不慢**
- span 看**具体慢在哪一步**

---

## 2.4 Trace 是什么

`Trace` 是一条完整链路。

它会把同一次操作里相关的 transaction / span 串起来。

### 例子
用户点击首页 -> 触发接口 -> 服务端处理 -> 返回客户端 -> 客户端渲染

这整个链条，如果都带同一个 `trace_id`，就构成一条 trace。

### 你可以这样记
- `Trace`：整条链路
- `Transaction`：链路中的一个大阶段
- `Span`：这个阶段下的细分步骤

---

## 3. 三者关系：Event / Transaction / Span

可以记成下面这句：

> **Transaction 是一种 Event，Span 是 Transaction 里的子步骤。**

关系图如下：

```text
Trace
└── Transaction (一种 Event)
    ├── Span 1
    ├── Span 2
    ├── Span 3
    └── Span N
