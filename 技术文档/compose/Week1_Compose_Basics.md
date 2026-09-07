Week 1 — Compose 基础（环境与核心构建块）
=====================================

目标
- 在 Android Studio 中新建并运行 Compose 项目。
- 理解 @Composable、@Preview、Modifier、Text/Button/Image、Row/Column/Box。
- 编写并在 Preview/真机上查看静态用户卡片 UI。

时间建议：8–12 小时（分三日完成）

核心概念
- @Composable：标记可组合函数，由 Compose 管理其生命周期与重组。
- @Preview：仅用于 IDE 预览，不参与运行时。
- Modifier：链式描述布局/绘制/交互（padding、size、background、clickable、weight 等）。
- 基础组件：Text、Button、Image、Icon、Card、Surface。
- 布局容器：Row（水平）、Column（垂直）、Box（堆叠）；weight 用于分配剩余空间。

最小示例
```kotlin
@Composable
fun GreetingCard(name: String) {
  Card(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
      Box(modifier = Modifier.size(56.dp).background(Color.Gray, CircleShape))
      Spacer(Modifier.width(12.dp))
      Column(modifier = Modifier.weight(1f)) {
        Text(name, style = MaterialTheme.typography.titleMedium)
        Text("Android 开发者", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
      }
      Button(onClick = {}) { Text("关注") }
    }
  }
}
```

练习
1. 完成静态用户卡片（头像、姓名、职位、按钮），在不同屏宽下验证布局。
2. 实现 TopBar（标题 + 右侧 IconButton）。
3. 静态设置列表：左图标、主/副文本、右侧开关（Switch）。

检测点
- Preview 能渲染示例。
- 模拟器/真机可运行并显示相同 UI。
- 提交一次 commit 并写 3–5 行学习笔记。

资源
- 官方 Codelab: Jetpack Compose Basics
- Compose API 文档（Modifier、Layouts、Material）

---
