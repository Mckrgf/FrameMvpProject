# Android `Context` 目录方法区别总结

## 1. 六个方法分别是什么

| 方法 | 存储位置 | 典型用途 | 卸载 App 后 | 是否可能为 `null` |
|---|---|---|---|---|
| `getCacheDir()` | 内部存储（应用私有） | 临时缓存（接口缓存、图片缓存） | 会删除 | 通常不会 |
| `getFilesDir()` | 内部存储（应用私有） | 长期保存的私有文件（配置、业务数据） | 会删除 | 通常不会 |
| `getExternalCacheDir()` | 外部存储应用专属目录（主卷） | 较大缓存文件 | 会删除 | 可能（外部存储不可用时） |
| `getExternalFilesDir("")` | 外部存储应用专属目录（主卷） | 较大且需要保留的业务文件 | 会删除 | 可能 |
| `getExternalCacheDirs()` | 外部存储应用缓存目录（所有卷） | 多存储卷缓存策略（主存储+SD 卡等） | 会删除 | 数组元素可能为 `null` |
| `getExternalMediaDirs()` | 外部存储应用媒体目录（所有卷） | 多卷媒体目录管理（拍照/录音等） | 会删除 | 数组元素可能为 `null` |

---

## 2. 核心区别

- **内部存储 vs 外部存储**
  - `getCacheDir()`、`getFilesDir()`：内部私有目录，稳定性更高。
  - 其余方法：外部存储应用专属目录，空间一般更大，但受挂载状态影响。

- **缓存目录 vs 文件目录**
  - `Cache`：临时数据，系统可能主动清理。
  - `Files`：更偏持久化，适合长期业务文件。

- **单目录 vs 多目录**
  - `getExternalCacheDir()`、`getExternalFilesDir(...)`：返回主卷目录。
  - `getExternalCacheDirs()`、`getExternalMediaDirs()`：返回所有可用存储卷目录数组。

---

## 3. 关于“现在基本不支持 SD 卡”的结论

如果项目明确**不做多存储卷（SD 卡/USB 等）适配**：

- 一般**不需要**使用：
  - `getExternalCacheDirs()`
  - `getExternalMediaDirs()`
- 日常使用单目录 API 足够：
  - 缓存：`getExternalCacheDir()`
  - 文件：`getExternalFilesDir(null)`（或具体类型目录）

仅在以下场景再考虑 `...Dirs()`：

- 需要兼容多存储卷设备
- 需要做更强的目录回退策略
- 未来规划支持外接/可挂载存储

---

## 4. 你当前代码里的建议

在 `CommunitySubFragment#scrollTop()` 中，这几行目录调用没有被使用：

```java
getContext().getCacheDir();
getContext().getFilesDir();
getContext().getExternalCacheDir();
getContext().getExternalFilesDir("");
getContext().getExternalCacheDirs();
getContext().getExternalMediaDirs();
```

如果只是调试遗留代码，建议删除，避免无意义调用和代码噪音。

---

## 5. 选型速记

- 小而重要、私有数据：`getFilesDir()`
- 临时缓存：`getCacheDir()`
- 大文件且 App 内部使用：`getExternalFilesDir(...)`
- 需要多卷兼容时：再用 `getExternalCacheDirs()` / `getExternalMediaDirs()`

