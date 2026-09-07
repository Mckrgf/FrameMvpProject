Week 2 — 状态管理与单向数据流
=================================

目标
- 理解 Compose 状态模型、remember、mutableStateOf、derivedStateOf。
- 掌握 State hoisting（状态提升）和将状态放到 ViewModel 中。
- 能用受控组件实现表单输入与验证。

时间建议：8–12 小时

核心概念
- 可组合函数中的状态会触发重组（recomposition）。
- remember 用于在重组间保留对象；rememberSaveable 用于进程死亡恢复（保存可序列化状态）。
- State hoisting：将局部状态提升到父组件或 ViewModel，使组件更可复用、可测试。
- Compose 与 Flow/StateFlow/LiveData 的桥接：collectAsState(), observeAsState()。

最小示例（计数器）
```kotlin
@Composable
fun Counter() {
  var count by remember { mutableStateOf(0) }
  Column { 
    Text("count: $count")
    Button(onClick = { count++ }) { Text("+1") }
  }
}
```

使用 ViewModel 保存状态
```kotlin
class MainViewModel: ViewModel() {
  private val _count = MutableStateFlow(0)
  val count: StateFlow<Int> = _count
  fun inc() { _count.value += 1 }
}

@Composable
fun CounterScreen(vm: MainViewModel = viewModel()) {
  val count by vm.count.collectAsState()
  Button(onClick = { vm.inc() }) { Text("$count") }
}
```

练习
1. 实现受控输入框：父组件持有文本状态并传入子组件。
2. 用 rememberSaveable 保持输入值在配置变更和进程死亡间尽量恢复。
3. 用 ViewModel + StateFlow 管理页面状态，并在 Compose 中 collectAsState 显示。

检测点
- 理解何时用 remember/rememberSaveable/StateFlow。
- 完成一个带验证（如邮箱格式校验）的表单，并写测试用例说明验证逻辑。

资源
- 官方文档：State in Compose
- Codelab：State and Compose

---
