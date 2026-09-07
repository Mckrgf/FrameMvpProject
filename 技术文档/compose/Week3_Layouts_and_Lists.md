Week 3 — 复杂布局与列表（LazyColumn/LazyRow）
=============================================

目标
- 掌握 Lazy 列表（LazyColumn/LazyRow）、items、key 的用法。
- 理解列表性能注意点与避免不必要重组的方法。
- 熟悉 ConstraintLayout for Compose 与自定义 Layout 基础。

时间建议：8–12 小时

核心概念
- Lazy 系列组件：按需创建子项，支持 items(itemsList){}, item{}, key 参数用于稳定化。
- 列表滚动状态：rememberLazyListState、滚动到位置 scrollToItem/animateScrollToItem。
- 性能优化：使用 key、避免在 item 中创建非必要对象；将不可变 UI 分离为小的可组合函数。
- 空状态、加载占位、分组头（stickyHeader 可选）

最小示例（列表）
```kotlin
@Composable
fun TodoList(items: List<String>, onClick: (String)->Unit) {
  val state = rememberLazyListState()
  LazyColumn(state = state) {
    items(items, key = { it }) { item ->
      Text(item, modifier = Modifier.fillMaxWidth().clickable{ onClick(item) }.padding(16.dp))
      Divider()
    }
  }
}
```

练习
1. 实现可点击的 TODO 列表（假数据），点击进入详情页（本周可用简单回调模拟）。
2. 实现加载占位（Shimmer/简单占位）与空状态视图。
3. 用 rememberLazyListState 实现点击返回到顶部的按钮（滚动到第一项）。

检测点
- 列表渲染流畅，使用 key 避免删除/插入时错乱。
- 将列表项拆成小组件并验证性能（手动检查重组频率或用日志）。

资源
- 官方文档：Lazy layouts
- 示例项目：compose-samples 中的列表示例

---
