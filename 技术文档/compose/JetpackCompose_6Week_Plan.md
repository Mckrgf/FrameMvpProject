Jetpack Compose 6 周学习计划与知识点介绍
====================================

版本：面向有 Android 开发经验但无 Compose 经验的工程师。
总时长建议：6 周（每周 8–12 小时）。目标：能独立用 Compose 做出带导航、持久化、ViewModel、主题及基础动画的 TODO 应用并编写若干 UI 测试。

一、总体说明
----------------
- 前提：熟悉 Kotlin、Android 基础（Activity/Fragment 概念、RecyclerView、ViewModel/LiveData/Coroutines 基本使用）。
- 架构建议：Compose + MVVM（ViewModel + repository）+ Room/Datastore。可选 DI：Hilt。
- 验收标准：能完成一个具备增删改查的 TODO APP，含导航、多屏、主题、动画与持久化；包含 3–5 个 UI 测试。

二、学习节奏（日常建议）
- 文档/视频：30–45 分钟
- 跟着 Codelab/示例实现：45–90 分钟
- 自己编码或复写练习功能：60–90 分钟
- 提交一次小 commit + 写 5–10 行学习笔记

三、每周计划（细化）
----------------------
周 1：环境与基础 Composable（目标：掌握基本构建块）
- 时间分配（总 8–12h）
  - 环境配置与模板：1h
  - 学习 @Composable、Preview、Modifier、布局基础（Row/Column/Box）：3–4h
  - 常用 UI：Text、Button、Image、Icon、Card：2h
  - 练习：实现静态用户卡片与 Header：2–3h
- 知识点说明
  - @Composable 注解与函数式 UI 思想
  - Preview 用于快速预览
  - Modifier 链式修饰（padding、fillMaxWidth、clickable、background）
  - 布局原理：主轴/交叉轴、权重（weight）
- 练习输出
  - 一个静态用户卡片屏幕（含头像、名字、按钮）

周 2：状态管理与单向数据流（目标：理解 Compose 的状态模型）
- 时间分配
  - State、remember、mutableStateOf：2–3h
  - State hoisting 与单向数据流：2h
  - ViewModel 与 Compose 集成、SavedStateHandle：2h
  - 练习：计数器、受控输入框表单：2–3h
- 知识点说明
  - 可组合函数中的可变状态与重组（recomposition）机制
  - remember 的作用与生命周期
  - State hoisting：将状态提升到父级或 ViewModel
  - Compose 与 LiveData/Flow/StateFlow 的桥接
- 练习输出
  - 一个带表单验证的页面（文本输入、错误提示、提交）

周 3：复杂布局与列表（Lazy 列表）（目标：能实现复杂列表与性能优化）
- 时间分配
  - LazyColumn/LazyRow、items、key：2–3h
  - 懒加载与占位（placeholder）、空状态、分组列表：2h
  - ConstraintLayout for Compose、weight、自定义布局基础：2h
  - 练习：实现可点击列表与详细页跳转：2–3h
- 知识点说明
  - Lazy 系列组件与 item scope
  - 列表复用、避免不必要重组（使用 keys、记忆化）
  - 滚动状态（rememberLazyListState）与滚动到特定位置
- 练习输出
  - 带加载、空状态、点击跳转的 TODO 列表（假数据）

周 4：导航、交互模式与输入（目标：掌握多屏导航与交互组件）
- 时间分配
  - Navigation for Compose（NavHost、NavController、safe-args 思维）：3h
  - Dialog、Snackbar、BottomSheet（ModalBottomSheet、Scaffold）：2h
  - TextField/Keyboard/Focus 管理与验证：2h
  - 练习：实现列表 → 新建/编辑页的完整流程：2–3h
- 知识点说明
  - 单Activity 多 NavHost 的常见模式（通常单 NavHost）
  - 参数传递与深度链接（字符串、序列化对象）
  - Scaffold：topBar、bottomBar、floatingActionButton 的使用模式
- 练习输出
  - 一个能新建/编辑 TODO、并返回更新列表的导航流程

周 5：主题、Material3、动画（目标：实现美观且响应式 UI）
- 时间分配
  - Material3 主题系统、颜色、Typography、暗色主题：3h
  - 自定义主题、主题扩展与动态色彩：2h
  - 动画基础（animate* API、AnimatedVisibility、animateContentSize、updateTransition）：3–4h
  - 练习：主题切换 + 添加/删除动画：1–2h
- 知识点说明
  - Material3 与 Compose Material 的差异点
  - 动画的原理：重组 vs 动画帧、状态驱动动画
  - 过渡动画（enter/exit）与性能注意点
- 练习输出
  - 支持暗色/浅色主题切换的 TODO 列表；新增/删除带动画

周 6：持久化、DI、测试与整合项目（目标：完成并验收最终项目）
- 时间分配
  - Room 数据库或 DataStore 集成（DAO、Repository）：3–4h
  - ViewModel 与 Repository 管理、Flow/StateFlow 数据流：2h
  - Hilt（或手动 DI）基础集成：1–2h
  - UI 测试（composeTestRule）、单元测试、打包：2–3h
  - 最终整合与 README：2–3h
- 知识点说明
  - Room 与 Compose 的数据流（Flow + collectAsState）
  - 保存屏幕状态（SavedStateHandle）与进程死亡恢复
  - Compose UI 测试：查找组件、执行点击、断言文本显示
- 练习输出
  - 完整的 TODO APP（增删改查、持久化、导航、主题、动画），含 3 个 UI 测试

四、交付物与检查点
- 每周提交分支或 commit：
  - week1-basics, week2-state, week3-list, week4-nav, week5-theme, week6-final
- 每周 README（短）：做了什么、学到的主要点、待改进项
- 最终 README：功能说明、架构、如何运行、测试命令与截图

五、示例每天任务清单（可复制）
- Day A（入门日）
  - 30m 阅读官方 Basics Codelab
  - 60–90m 跟着 Codelab 编写示例
  - 60m 改写/扩展示例并 commit
- Day B（练习日）
  - 30m 回顾笔记与问题列表
  - 90m 实现一个小 feature（比如带状态的输入表单）
  - 30m 写测试或记录边界情况

六、推荐资源（中文/英文）
- 官方文档：https://developer.android.com/jetpack/compose
- 官方 Codelabs：Jetpack Compose Pathway
- 谷歌 I/O/Compose 相关视频和 Android 官方示例
- 社区教程：androidbycode、Medium 上的 Compose 系列文章

七、扩展建议（进阶）
- Paging 3 + Compose 集成
- ConstraintLayout 复杂布局与自定义 Layout
- 性能剖析（Layout Inspector、Compose Compiler 插桟提示）
- 自定义可组合组件库封装

八、如何使用本计划
- 把文档放 repo docs 或 root，按每周分支实现并在每周末做回顾。
- 需我把每周内容拆成单独 Markdown 文件并初始化一个 starter repo 模板吗？

---
生成时间: 2026-09-07
