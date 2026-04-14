# app 模块基础数据结构

> 本文档列出 `:app` 模块中与 Android 框架相关的基础数据结构，主要包括 Room 数据库实体类。  
> 对应的文件链接指向 `app` 模块源码路径。

## 一、Room 实体类（数据库表）

| 数据结构 | 作用 | 文件链接 |
| --- | --- | --- |
| `RawDataEntity` | 存储原始数据（ID、类型、时间戳、JSON/Blob键） | [app/src/main/java/.../data/local/entity/RawDataEntity.kt](../app/src/main/java/com/sea/auspicious_sign/data/local/entity/RawDataEntity.kt) |
| `TransformedDataEntity` | 存储转化数据（关联原始数据ID、结果JSON） | [app/src/main/java/.../data/local/entity/TransformedDataEntity.kt](../app/src/main/java/com/sea/auspicious_sign/data/local/entity/TransformedDataEntity.kt) |
| `ConsumableDataEntity` | 存储消费数据（关联转化数据ID、结果JSON、同步状态） | [app/src/main/java/.../data/local/entity/ConsumableDataEntity.kt](../app/src/main/java/com/sea/auspicious_sign/data/local/entity/ConsumableDataEntity.kt) |
| `SyncQueueEntity` | 同步队列表（记录待同步的消费数据ID、重试次数、状态） | [app/src/main/java/.../data/local/entity/SyncQueueEntity.kt](../app/src/main/java/com/sea/auspicious_sign/data/local/entity/SyncQueueEntity.kt) |

## 二、其他 Android 相关数据结构

（本模块中其他 Android 特定类，如 WorkManager 相关数据、SharedPreferences 键值等，不属于严格意义上的“基础数据结构”，故未列入。如有需要可后续扩展。）

---

**生成时间**：2026-04-14  
**维护者**：项目团队