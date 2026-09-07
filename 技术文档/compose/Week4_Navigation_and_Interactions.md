Week 4 — 导航、交互模式与输入处理
====================================

目标
- 掌握 Navigation for Compose（NavHost、NavController、参数传递、返回栈）。
- 理解 Scaffold、Snackbar、Dialog、BottomSheet 等交互模式。
- 熟悉 TextField 键盘/焦点管理与表单验证流程。

时间建议：8–12 小时

核心概念
- NavHost + composable(route) 定义导航图；NavController 控制导航动作。
- 参数传递：通过路由参数或使用 Parcelable/JSON 方案传对象（注意避免大对象作为参数）。
- Scaffold：统一管理 topBar、bottomBar、floatingActionButton 与 snackbarHost。
- Keyboard 与 Focus：LocalFocusManager、keyboardOptions、keyboardActions。

最小示例（导航）
```kotlin
@Composable
fun AppNav() {
  val navController = rememberNavController()
  NavHost(navController, startDestination = "list") {
    composable("list") { TodoListScreen(onAdd = { navController.navigate("add") }) }
    composable("add") { AddTodoScreen(onSave = { navController.popBackStack() }) }
  }
}
```

练习
1. 实现列表页 -> 新建页 -> 编辑页 的导航流（可用简单 in-memory list 模拟持久化）。
2. 在 Scaffold 中展示 Snackbar（如保存成功提示）。
3. 实现 Dialog 确认删除操作与 ModalBottomSheet 展示更多操作。

检测点
- 导航流程正确（包含返回栈行为），参数传递与返回结果工作正常。
- 键盘与焦点在表单提交/取消时按预期处理。

资源
- 官方：Navigation for Compose 文档
- 示例：Compose Navigation codelabs

---
