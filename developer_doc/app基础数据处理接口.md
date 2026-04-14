# app 模块基础数据处理接口

> 本文档列出 `:app` 模块中与 Android 框架相关的数据处理接口（Room DAO、网络接口、传感器接口等）。  
> 对应的文件链接指向 `app` 模块源码路径。

## 一、Room 数据库访问接口（DAO）

| 接口 | 作用 | 关键方法 | 文件链接 |
| --- | --- | --- | --- |
| `RawDataDao` | 原始数据表操作 | `insert`, `getById`, `getUnsynced` 等 | [app/src/main/java/.../data/local/dao/RawDataDao.kt](../app/src/main/java/com/sea/auspicious_sign/data/local/dao/RawDataDao.kt) |
| `TransformedDataDao` | 转化数据表操作 | `insert`, `getByRawId` 等 | [app/src/main/java/.../data/local/dao/TransformedDataDao.kt](../app/src/main/java/com/sea/auspicious_sign/data/local/dao/TransformedDataDao.kt) |
| `ConsumableDataDao` | 消费数据表操作 | `insert`, `getUnsynced` 等 | [app/src/main/java/.../data/local/dao/ConsumableDataDao.kt](../app/src/main/java/com/sea/auspicious_sign/data/local/dao/ConsumableDataDao.kt) |
| `SyncQueueDao` | 同步队列表操作 | `insert`, `updateStatus`, `getPending` 等 | [app/src/main/java/.../data/local/dao/SyncQueueDao.kt](../app/src/main/java/com/sea/auspicious_sign/data/local/dao/SyncQueueDao.kt) |

## 二、网络接口（Retrofit）

| 接口 | 作用 | 关键方法 | 文件链接 |
| --- | --- | --- | --- |
| `NetlifyApi` | 定义与 Astro 后端通信的 API | `uploadData`, `requestProcess`, `getResult`, `syncMappings` 等 | [app/src/main/java/.../data/remote/NetlifyApi.kt](../app/src/main/java/com/sea/auspicious_sign/data/remote/NetlifyApi.kt) |

## 三、传感器采集接口

| 类/接口 | 作用 | 关键方法 | 文件链接 |
| --- | --- | --- | --- |
| `HeartRateCollector` | 心率传感器监听器（含模拟数据 fallback） | `start()`, `stop()`, 回调接口 | [app/src/main/java/.../sensor/collector/HeartRateCollector.kt](../app/src/main/java/com/sea/auspicious_sign/sensor/collector/HeartRateCollector.kt) |
| `AccelerometerCollector` | 加速度计传感器监听器 | `start()`, `stop()`, 回调接口 | [app/src/main/java/.../sensor/collector/AccelerometerCollector.kt](../app/src/main/java/com/sea/auspicious_sign/sensor/collector/AccelerometerCollector.kt) |

## 四、工作管理器接口

| 类 | 作用 | 关键方法 | 文件链接 |
| --- | --- | --- | --- |
| `SensorUploadWorker` | 定期上传传感器数据的 Worker | `doWork()` | [app/src/main/java/.../sensor/upload/SensorUploadWorker.kt](../app/src/main/java/com/sea/auspicious_sign/sensor/upload/SensorUploadWorker.kt) |
| `SyncWorker` | 定期同步本地结果到 Netlify DB 的 Worker | `doWork()` | [app/src/main/java/.../sync/SyncWorker.kt](../app/src/main/java/com/sea/auspicious_sign/sync/SyncWorker.kt) |

## 五、仓库与协调接口

| 类/接口 | 作用 | 关键方法 | 文件链接 |
| --- | --- | --- | --- |
| `SensorDataRepository` | 传感器数据本地存储仓库（封装 Room） | `insertRawData()`, `getUnsyncedData()` 等 | [app/src/main/java/.../sensor/SensorDataRepository.kt](../app/src/main/java/com/sea/auspicious_sign/sensor/SensorDataRepository.kt) |
| `DataProcessRepository` | 协调采集、处理、存储全流程 | `processRawData(raw: RawData)` | [app/src/main/java/.../domain/repository/DataProcessRepository.kt](../app/src/main/java/com/sea/auspicious_sign/domain/repository/DataProcessRepository.kt) |
| `SyncRepository` | 同步逻辑封装（读取队列、调用 API、更新状态） | `enqueueSync()`, `processPendingSync()` | [app/src/main/java/.../sync/SyncRepository.kt](../app/src/main/java/com/sea/auspicious_sign/sync/SyncRepository.kt) |

---

**生成时间**：2026-04-14  
**维护者**：项目团队