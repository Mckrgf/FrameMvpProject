Week 5 — 主题（Material3）与动画
=====================================

目标
- 理解 Material3 主题系统、颜色、Typography、暗黑模式。
- 掌握常用动画 API：animate* 系列、AnimatedVisibility、updateTransition、animateContentSize。
- 为 UI 添加主题切换与增删动画，使界面更流畅。

时间建议：8–12 小时

核心概念
- Material3 与 Compose Material：颜色系统、Surface、elevation 与动态颜色（可选）。
- 主题结构：Theme -> ColorScheme、Typography、Shapes。通过 CompositionLocal 提供主题值。
- 动画是状态驱动的：改变状态触发动画，避免直接在动画中做状态逻辑。

最小示例（主题切换 + 动画）
```kotlin
@Composable
fun ThemeToggleApp() {
  var dark by rememberSaveable { mutableStateOf(false) }
  MyTheme(darkTheme = dark) {
    Column { 
      Switch(checked = dark, onCheckedChange = { dark = it })
      AnimatedVisibility(visible = !dark) {
        Text("Light mode content", modifier = Modifier.animateContentSize().padding(8.dp))
      }
    }
  }
}
```

练习
1. 实现浅/深色主题切换并验证颜色与文字可读性。
2. 为新增 TODO 和删除 TODO 添加入场/退出动画（AnimatedVisibility 或 updateTransition）。
3. 使用 animateFloatAsState 为按钮添加缩放反馈。

检测点
- 主题切换覆盖主要控件（Text/Button/Surface）；暗色模式下无可读性问题。
- 新增/删除行为带动画且不卡顿。

资源
- 官方：Material3 in Compose
- 动画文档：Animation in Jetpack Compose

---
