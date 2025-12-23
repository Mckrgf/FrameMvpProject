# Android Jetpack Compose 学习教程：从入门到进阶到精通

## 入门篇

### 1. 什么是 Jetpack Compose？
Jetpack Compose 是 Google 推出的现代 Android UI 工具包，使用声明式编程范式构建用户界面。它简化了 UI 开发，减少了样板代码，并提供了更好的性能和可维护性。与传统的 View 系统不同，Compose 直接在 Kotlin 中编写 UI，无需 XML。

### 2. 设置项目
要使用 Compose，你需要：
- Android Studio Arctic Fox 或更高版本。
- 在 `build.gradle` (Module) 中启用 Compose：
  ```kotlin
  android {
      composeOptions {
          kotlinCompilerExtensionVersion = "1.5.8"  // 根据最新版本调整
      }
  }

  dependencies {
      implementation "androidx.compose.ui:ui:1.6.0"  // 核心库
      implementation "androidx.compose.material:material:1.6.0"  // Material Design 组件
      implementation "androidx.compose.ui:ui-tooling-preview:1.6.0"  // 预览工具
      implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0"  // ViewModel 支持
  }
  ```
- 在 `build.gradle` (Project) 中确保 Kotlin 版本兼容。

### 3. 第一个 Composable 函数
Composable 函数是 Compose 的核心。它们是使用 `@Composable` 注解的函数，用于描述 UI。

```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello, $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Greeting("World")
}
```

- `@Composable`：标记函数为可组合。
- `@Preview`：在 Android Studio 中预览 UI。

### 4. 基本组件
- **Text**：显示文本。
  ```kotlin
  Text(text = "Hello World", style = MaterialTheme.typography.h1)
  ```
- **Button**：可点击按钮。
  ```kotlin
  Button(onClick = { /* 处理点击 */ }) {
      Text("Click Me")
  }
  ```
- **TextField**：输入框。
  ```kotlin
  var text by remember { mutableStateOf("") }
  TextField(value = text, onValueChange = { text = it })
  ```

### 5. 状态和重组
Compose 使用状态驱动 UI。当状态改变时，UI 会自动重组（重新执行 Composable 函数）。

- 使用 `remember` 保存状态：
  ```kotlin
  var count by remember { mutableStateOf(0) }
  Button(onClick = { count++ }) {
      Text("Count: $count")
  }
  ```
- `mutableStateOf` 创建可观察状态。

## 进阶篇

### 1. 布局组件
- **Column**：垂直布局。
  ```kotlin
  Column {
      Text("Item 1")
      Text("Item 2")
  }
  ```
- **Row**：水平布局。
  ```kotlin
  Row {
      Text("Left")
      Text("Right")
  }
  ```
- **Box**：重叠布局。
  ```kotlin
  Box {
      Text("Background")
      Text("Foreground", modifier = Modifier.align(Alignment.Center))
  }
  ```

### 2. 自定义 Composable
创建可复用的 UI 组件：
```kotlin
@Composable
fun CustomCard(title: String, content: String) {
    Card(modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.h6)
            Text(content)
        }
    }
}
```

### 3. 状态管理
- 与 ViewModel 集成：
  ```kotlin
  class MyViewModel : ViewModel() {
      var name by mutableStateOf("")
          private set
  }

  @Composable
  fun MyScreen(viewModel: MyViewModel = viewModel()) {
      val name = viewModel.name
      TextField(value = name, onValueChange = { viewModel.name = it })
  }
  ```
- 使用 `collectAsState` 与 Flow 集成。

### 4. 动画
Compose 提供强大的动画 API：
```kotlin
var visible by remember { mutableStateOf(true) }
AnimatedVisibility(visible = visible) {
    Text("Fade in/out")
}
Button(onClick = { visible = !visible }) { Text("Toggle") }
```

### 5. 主题和样式
- 定义主题：
  ```kotlin
  @Composable
  fun MyTheme(content: @Composable () -> Unit) {
      MaterialTheme(
          colors = darkColors(),  // 或 lightColors()
          typography = Typography(),
          shapes = Shapes()
      ) {
          content()
      }
  }
  ```
- 自定义颜色、字体等。

### 6. 导航
使用 Navigation Compose：
```kotlin
val navController = rememberNavController()
NavHost(navController, startDestination = "home") {
    composable("home") { HomeScreen() }
    composable("details") { DetailsScreen() }
}
```

### 7. 测试
- 使用 Compose Test Rule：
  ```kotlin
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun testGreeting() {
      composeTestRule.setContent {
          Greeting("Test")
      }
      composeTestRule.onNodeWithText("Hello, Test!").assertExists()
  }
  ```

## 精通篇

### 1. 高级状态管理
- **State Hoisting**：将状态提升到父组件，提高可复用性。
  ```kotlin
  @Composable
  fun Counter(count: Int, onCountChange: (Int) -> Unit) {
      Button(onClick = { onCountChange(count + 1) }) {
          Text("Count: $count")
      }
  }
  ```
- **Snapshot Flow**：将状态转换为 Flow，用于复杂逻辑。
- **SavedStateHandle**：在 ViewModel 中保存状态，支持配置变化。

### 2. 性能优化
- **重组优化**：使用 `remember` 和 `derivedStateOf` 避免不必要的重组。
  ```kotlin
  val derivedValue = remember { derivedStateOf { expensiveCalculation(state) } }
  ```
- **Lazy 组件**：如 `LazyColumn` 用于大列表，虚拟化渲染。
- **Compose Compiler Metrics**：分析重组性能。

### 3. 自定义布局
- 实现 `Layout` 接口创建自定义布局：
  ```kotlin
  @Composable
  fun CustomLayout(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
      Layout(content = content, modifier = modifier) { measurables, constraints ->
          // 自定义测量和放置逻辑
      }
  }
  ```

### 4. 与现有 View 系统的集成
- **AndroidView**：在 Compose 中嵌入传统 View。
  ```kotlin
  AndroidView(factory = { context -> TextView(context).apply { text = "Legacy View" } })
  ```
- **ComposeView**：在传统 View 中嵌入 Compose。

### 5. 复杂动画和手势
- **Transition**：多状态动画。
  ```kotlin
  val transition = updateTransition(targetState = state)
  val alpha by transition.animateFloat { if (it == Visible) 1f else 0f }
  ```
- **PointerInput**：处理触摸手势。
  ```kotlin
  Modifier.pointerInput(Unit) {
      detectTapGestures(onTap = { /* 处理点击 */ })
  }
  ```

### 6. 架构模式
- **MVVM**：使用 ViewModel 和 LiveData/Flow。
- **MVI**：单向数据流，使用 StateFlow。
- **依赖注入**：与 Hilt 或 Dagger 集成。

### 7. 高级测试
- **截图测试**：使用 Paparazzi 或 Compose Test Rule 的截图功能。
- **端到端测试**：与 Espresso 结合。
- **多设备测试**：考虑不同屏幕尺寸和方向。

### 8. 最佳实践和陷阱
- 避免在 Composable 中执行副作用，使用 `LaunchedEffect` 或 `DisposableEffect`。
- 使用 `SideEffect` 处理全局状态。
- 关注 Compose 的更新（截至 2025 年，版本已达 1.7+），如新的 API 和性能改进。
- 学习社区资源：官方文档、Kotlin YouTube 频道、Compose 样本应用。

这个教程从基础到高级全面覆盖。精通需要大量实践，建议构建真实项目并参与开源社区。官方文档：https://developer.android.com/jetpack/compose。如果有特定主题需要深入，可以进一步询问！
