# Jetpack Compose 实用教程

> 面向有经验的 Android 开发者，快速掌握 Compose 核心概念和最佳实践

---

## 目录

1. [基础概念](#1-基础概念)
2. [常用布局](#2-常用布局)
3. [状态管理](#3-状态管理)
4. [列表与滚动](#4-列表与滚动)
5. [Material 3 组件](#5-material-3-组件)
6. [动画](#6-动画)
7. [副作用](#7-副作用)
8. [导航](#8-导航)
9. [与 View 系统互操作](#9-与-view-系统互操作)
10. [性能优化](#10-性能优化)

---

## 1. 基础概念

### 1.1 Composable 函数

```kotlin
@Composable
fun Greeting(name: String) {
    Text(text = "Hello, $name!")
}
```

- `@Composable` 注解标记可组合函数
- 函数名首字母大写（与 View 命名一致）
- 返回 Unit，不返回视图对象

### 1.2 Modifier 链式调用

```kotlin
Text(
    text = "Hello",
    modifier = Modifier
        .fillMaxWidth()           // 填充最大宽度
        .padding(16.dp)           // 内边距
        .background(Color.Blue)   // 背景色
        .clickable { }            // 点击事件
)
```

**重要**：Modifier 顺序影响结果！

```kotlin
// 先 padding 再 background -> 背景包含 padding 区域
Modifier.padding(16.dp).background(Color.Blue)

// 先 background 再 padding -> 背景不包含 padding 区域
Modifier.background(Color.Blue).padding(16.dp)
```

### 1.3 尺寸单位

```kotlin
// dp - 密度无关像素
Modifier.size(100.dp)
Modifier.padding(16.dp)

// sp - 可缩放像素（用于字体）
TextStyle(fontSize = 16.sp)

// 使用 IntOffset（像素值）
Modifier.offset { IntOffset(x = 0, y = -100) }
```

---

## 2. 常用布局

### 2.1 Box - 层叠布局

```kotlin
Box(
    modifier = Modifier.fillMaxSize()
) {
    // 底层
    Image(
        painter = painterResource(R.drawable.bg),
        modifier = Modifier.fillMaxSize()
    )
    
    // 中层
    Text(
        text = "Title",
        modifier = Modifier.align(Alignment.Center)
    )
    
    // 顶层
    Button(
        onClick = { },
        modifier = Modifier.align(Alignment.BottomEnd)
    ) {
        Text("Click")
    }
}
```

### 2.2 Column - 垂直布局

```kotlin
Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(8.dp),  // 子项间距
    horizontalAlignment = Alignment.CenterHorizontally  // 水平对齐
) {
    Text("Item 1")
    Text("Item 2")
    Text("Item 3")
}
```

### 2.3 Row - 水平布局

```kotlin
Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,  // 两端对齐
    verticalAlignment = Alignment.CenterVertically      // 垂直居中
) {
    Text("Left")
    Text("Right")
}
```

### 2.4 Scaffold - 页面骨架

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My App") },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, "Search")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                // 底部导航项
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { paddingValues ->
        // 主内容
        Content(
            modifier = Modifier.padding(paddingValues)
        )
    }
}
```

---

## 3. 状态管理

### 3.1 remember 与 mutableStateOf

```kotlin
@Composable
fun Counter() {
    // 记住状态，重组时保持值
    var count by remember { mutableIntStateOf(0) }
    
    Button(onClick = { count++ }) {
        Text("Clicked $count times")
    }
}
```

### 3.2 rememberSaveable - 跨配置变更保存

```kotlin
@Composable
fun MyScreen() {
    // 屏幕旋转等配置变更时保持状态
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    var inputText by rememberSaveable { mutableStateOf("") }
}
```

### 3.3 状态提升

```kotlin
// 子组件不持有状态，由父组件管理
@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange
    )
}

// 父组件持有状态
@Composable
fun ParentScreen() {
    var text by remember { mutableStateOf("") }
    MyTextField(
        value = text,
        onValueChange = { text = it }
    )
}
```

### 3.4 ViewModel 集成

```kotlin
class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MyUiState>(MyUiState.Loading)
    val uiState: StateFlow<MyUiState> = _uiState
    
    fun loadData() {
        viewModelScope.launch {
            _uiState.value = MyUiState.Loading
            try {
                val data = repository.fetchData()
                _uiState.value = MyUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = MyUiState.Error(e.message)
            }
        }
    }
}

sealed class MyUiState {
    object Loading : MyUiState()
    data class Success(val data: Data) : MyUiState()
    data class Error(val message: String?) : MyUiState()
}

@Composable
fun MyScreen(viewModel: MyViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    
    when (val state = uiState) {
        is MyUiState.Loading -> LoadingIndicator()
        is MyUiState.Success -> Content(state.data)
        is MyUiState.Error -> ErrorScreen(state.message)
    }
}
```

---

## 4. 列表与滚动

### 4.1 LazyColumn - 垂直列表

```kotlin
@Composable
fun ItemList(items: List<String>) {
    val listState = rememberLazyListState()
    
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        // 单个 item
        items(items) { item ->
            ItemCard(item)
        }
        
        // 带 key 的 item（推荐，提升性能）
        items(items, key = { it.id }) { item ->
            ItemCard(item)
        }
        
        // 带索引
        itemsIndexed(items) { index, item ->
            Text("$index: $item")
        }
    }
}
```

### 4.2 LazyRow - 水平列表

```kotlin
@Composable
fun HorizontalList(items: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(items) { item ->
            Chip(item)
        }
    }
}
```

### 4.3 监听滚动状态

```kotlin
@Composable
fun ScrollableList() {
    val listState = rememberLazyListState()
    
    // 监听滚动位置变化
    LaunchedEffect(listState.firstVisibleItemIndex) {
        // 滚动到顶部时刷新数据
        if (listState.firstVisibleItemIndex == 0) {
            // refresh
        }
    }
    
    // 检测滚动方向
    var previousIndex by remember { mutableIntStateOf(0) }
    var previousOffset by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(listState.firstVisibleItemIndex, listState.firstVisibleItemScrollOffset) {
        val currentIndex = listState.firstVisibleItemIndex
        val currentOffset = listState.firstVisibleItemScrollOffset
        
        val isScrollingUp = when {
            currentIndex > previousIndex -> true
            currentIndex < previousIndex -> false
            currentOffset > previousOffset -> true
            currentOffset < previousOffset -> false
            else -> null
        }
        
        previousIndex = currentIndex
        previousOffset = currentOffset
    }
    
    LazyColumn(state = listState) {
        // items
    }
}
```

### 4.4 滚动到指定位置

```kotlin
@Composable
fun ScrollToTopButton() {
    val listState = rememberLazyListState()
    
    Box {
        LazyColumn(state = listState) {
            // items
        }
        
        Button(
            onClick = { 
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text("Scroll to Top")
        }
    }
}
```

---

## 5. Material 3 组件

### 5.1 Tab 标签页

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabScreen() {
    val tabs = listOf("推荐", "关注", "热榜")
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    
    Column {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(3.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color.Blue, Color.Green)
                            ),
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                        )
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }
        
        // Tab 内容
        when (selectedTab) {
            0 -> RecommendContent()
            1 -> FollowContent()
            2 -> HotContent()
        }
    }
}
```

### 5.2 Card 卡片

```kotlin
@Composable
fun ItemCard(item: Item) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(item.imageRes),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
```

### 5.3 TextField 输入框

```kotlin
@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("搜索...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )
}
```

### 5.4 Dialog 对话框

```kotlin
@Composable
fun ConfirmDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("确认") },
            text = { Text("确定要执行此操作吗？") },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("取消")
                }
            }
        )
    }
}
```

---

## 6. 动画

### 6.1 animate*AsState - 值动画

```kotlin
@Composable
fun AnimatedVisibilityExample() {
    var expanded by remember { mutableStateOf(false) }
    
    // 尺寸动画
    val size by animateDpAsState(
        targetValue = if (expanded) 200.dp else 100.dp,
        animationSpec = tween(durationMillis = 300)
    )
    
    // 透明度动画
    val alpha by animateFloatAsState(
        targetValue = if (expanded) 1f else 0.5f
    )
    
    Box(
        modifier = Modifier
            .size(size)
            .alpha(alpha)
            .background(Color.Blue)
            .clickable { expanded = !expanded }
    )
}
```

### 6.2 AnimatedVisibility - 显示/隐藏动画

```kotlin
@Composable
fun AnimatedVisibilityExample() {
    var visible by remember { mutableStateOf(true) }
    
    Column {
        Button(onClick = { visible = !visible }) {
            Text("Toggle")
        }
        
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Card {
                Text("Animated Content")
            }
        }
    }
}
```

### 6.3 animateContentSize - 内容尺寸动画

```kotlin
@Composable
fun ExpandableCard() {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()  // 内容变化时自动动画
            .clickable { expanded = !expanded }
    ) {
        Column {
            Text("Title")
            if (expanded) {
                Text("Expanded content here")
            }
        }
    }
}
```

### 6.4 无限动画

```kotlin
@Composable
fun PulsingButton() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Button(
        onClick = { },
        modifier = Modifier.scale(scale)
    ) {
        Text("Pulsing")
    }
}
```

---

## 7. 副作用

### 7.1 LaunchedEffect - 启动协程

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel) {
    // 组合进入时执行，退出时取消
    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
    
    // 依赖变化时重新执行
    LaunchedEffect(userId) {
        viewModel.loadUserData(userId)
    }
}
```

### 7.2 DisposableEffect - 资源清理

```kotlin
@Composable
fun TimerScreen(onTimeout: () -> Unit) {
    DisposableEffect(Unit) {
        val job = CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
            onTimeout()
        }
        
        // 组件销毁时取消
        onDispose {
            job.cancel()
        }
    }
}
```

### 7.3 SideEffect - 每次重组后执行

```kotlin
@Composable
fun AnalyticsScreen(screenName: String) {
    // 每次重组后都会执行（谨慎使用）
    SideEffect {
        Analytics.logScreenView(screenName)
    }
}
```

### 7.4 rememberCoroutineScope - 获取协程作用域

```kotlin
@Composable
fun ScrollToTopButton() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    Button(
        onClick = {
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        }
    ) {
        Text("Top")
    }
}
```

---

## 8. 导航

### 8.1 Navigation Compose

```kotlin
// build.gradle
// implementation "androidx.navigation:navigation-compose:2.7.7"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToDetail = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
        
        composable(
            route = "detail/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            DetailScreen(id = id)
        }
    }
}
```

### 8.2 类型安全导航（推荐）

```kotlin
// 定义导航路由
object Routes {
    const val HOME = "home"
    const val PROFILE = "profile/{userId}"
    
    fun profile(userId: String) = "profile/$userId"
}

// 使用
NavHost(startDestination = Routes.HOME) {
    composable(Routes.HOME) {
        HomeScreen(
            onNavigateToProfile = { userId ->
                navController.navigate(Routes.profile(userId))
            }
        )
    }
}
```

---

## 9. 与 View 系统互操作

### 9.1 在 Compose 中使用 Android View

```kotlin
@Composable
fun WebViewScreen(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        update = { webView ->
            // 状态变化时更新 View
            webView.loadUrl(url)
        },
        modifier = Modifier.fillMaxSize()
    )
}
```

### 9.2 在 Android View 中使用 Compose

```kotlin
class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContentView(R.layout.activity_main)
        
        val composeView = findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            MaterialTheme {
                MyComposable()
            }
        }
    }
}
```

### 9.3 嵌入传统 View 到 Compose

```kotlin
@Composable
fun MapScreen() {
    AndroidView(
        factory = { context ->
            MapView(context).apply {
                // 初始化 MapView
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}
```

---

## 10. 性能优化

### 10.1 使用 key 优化列表

```kotlin
// 不推荐 - 没有 key，重组时可能复用错误的 item
LazyColumn {
    items(items) { item ->
        ItemRow(item)
    }
}

// 推荐 - 使用唯一 key
LazyColumn {
    items(items, key = { it.id }) { item ->
        ItemRow(item)
    }
}
```

### 10.2 避免不必要的重组

```kotlin
// 不推荐 - 每次重组都创建新对象
@Composable
fun MyScreen() {
    LazyColumn {
        items(listOf(1, 2, 3)) { item ->
            Text(
                text = "Item $item",
                modifier = Modifier.padding(8.dp)  // 每次创建新 Modifier
            )
        }
    }
}

// 推荐 - 提取常量
private val ITEM_PADDING = 8.dp

@Composable
fun MyScreen() {
    LazyColumn {
        items(listOf(1, 2, 3)) { item ->
            Text(
                text = "Item $item",
                modifier = Modifier.padding(ITEM_PADDING)
            )
        }
    }
}
```

### 10.3 使用 derivedStateOf 优化计算

```kotlin
@Composable
fun ExpensiveList() {
    val listState = rememberLazyListState()
    
    // 只有 firstVisibleItemIndex 变化时才重新计算
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    
    Box {
        LazyColumn(state = listState) {
            // items
        }
        
        if (showButton) {
            ScrollToTopButton()
        }
    }
}
```

### 10.4 延迟加载优化

```kotlin
// 使用 LazyColumn 而非 Column，避免一次性加载所有内容
// 不推荐
Column {
    items(1000) { index ->
        HeavyItem(index)
    }
}

// 推荐
LazyColumn {
    items(1000) { index ->
        HeavyItem(index)
    }
}
```

---

## 附录：常用速查

### 常用 Modifier

| Modifier | 用途 |
|----------|------|
| `fillMaxSize()` | 填充最大尺寸 |
| `fillMaxWidth()` | 填充最大宽度 |
| `wrapContentWidth()` | 包裹内容宽度 |
| `padding()` | 内边距 |
| `margin()` | 外边距（通过父布局实现） |
| `background()` | 背景色 |
| `clip()` | 裁剪形状 |
| `border()` | 边框 |
| `clickable()` | 点击事件 |
| `alpha()` | 透明度 |
| `offset()` | 偏移 |
| `zIndex()` | 层级 |

### 常用布局

| 布局 | 用途 |
|------|------|
| `Box` | 层叠 |
| `Column` | 垂直 |
| `Row` | 水平 |
| `Scaffold` | 页面骨架 |
| `LazyColumn` | 垂直列表 |
| `LazyRow` | 水平列表 |
| `Surface` | Material 表面 |

### 常用动画

| API | 用途 |
|-----|------|
| `animateDpAsState` | dp 值动画 |
| `animateFloatAsState` | float 值动画 |
| `animateColorAsState` | 颜色动画 |
| `AnimatedVisibility` | 显示/隐藏动画 |
| `animateContentSize` | 内容尺寸动画 |
| `rememberInfiniteTransition` | 无限动画 |

---

## 参考资源

- [官方文档](https://developer.android.com/jetpack/compose)
- [Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Material 3 Components](https://m3.material.io/develop/android/compose)
- [Compose Samples](https://github.com/android/compose-samples)
