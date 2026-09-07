Week 6 — 持久化、DI、测试与整合
===================================

目标
- 用 Room（或 DataStore）实现 TODO 持久化并与 ViewModel 协作。
- 集成 Hilt（可选）实现依赖注入。
- 编写 Compose UI 测试（composeTestRule）与关键单元测试，完成最终项目整合与打包。

时间建议：8–12 小时

核心概念
- Room：Entity、DAO、Database，使用 Flow/LiveData 返回数据，在 Compose 中用 collectAsState()
- Repository 模式：隔离数据源并为 ViewModel 提供 API。
- Hilt：为 Repository/Database/ViewModel 注入依赖，简化测试替换。
- Compose UI 测试：使用 createComposeRule(), onNodeWithText(), performClick(), assertIsDisplayed()。

最小示例（Room + Flow）
```kotlin
@Entity data class TodoItem(@PrimaryKey val id: String, val text: String)
@Dao interface TodoDao {
  @Query("SELECT * FROM TodoItem") fun getAll(): Flow<List<TodoItem>>
  @Insert suspend fun insert(item: TodoItem)
}

class TodoRepository(private val dao: TodoDao) {
  val all = dao.getAll()
  suspend fun add(t: TodoItem) = dao.insert(t)
}

@Composable
fun TodoScreen(repo: TodoRepository = /* inject */) {
  val items by repo.all.collectAsState(initial = emptyList())
  // render list
}
```

测试示例（Compose UI 测试）
```kotlin
@get:Rule val composeTestRule = createComposeRule()
@Test fun addTodo_showsInList() {
  composeTestRule.setContent { TodoScreen(sampleRepo) }
  composeTestRule.onNodeWithText("Add").performClick()
  composeTestRule.onNodeWithText("新任务").assertIsDisplayed()
}
```

练习
1. 完成 Room 数据层并用 Repository 暴露 Flow。
2. 集成 ViewModel，使用 StateFlow/LiveData 将数据提供到 Compose。
3. 写 3 个 Compose UI 测试（新增、删除、编辑场景）。
4. 使用 Hilt 简化注入并编写简单的集成测试（可选）。

检测点
- 数据持久化在应用重启后仍可见。
- 核心 UI 测试通过。
- 生成可安装 APK 并在设备上验证功能完整性。

资源
- 官方 Room 文档
- Hilt for Android 文档
- Compose testing 文档

---
