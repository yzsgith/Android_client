# domain 模块基础数据结构

> 本文档列出 `:domain` 模块中的所有基础数据结构（纯 Kotlin/JVM 逻辑，不依赖 Android SDK）。  
> 对应的文件链接指向 `domain` 模块源码路径。

## 一、数据阶段标记接口

| 数据结构 | 作用 | 文件链接 |
| --- | --- | --- |
| `RawData` | 标记原始数据（来自传感器或文件） | [domain/model/DataStages.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/model/DataStages.kt) |
| `TransformedData` | 标记转化后的中间数据 | 同上 |
| `ConsumableData` | 标记最终可使用的数据（如告警、统计结果） | 同上 |

## 二、传感器数据模型

| 数据结构 | 作用 | 文件链接 |
| --- | --- | --- |
| `RawHeartRate` | 心率原始数据（bpm、置信度、时间戳） | [domain/model/SensorData.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/model/SensorData.kt) |
| `RawAccelerometer` | 加速度计原始数据（x、y、z、时间戳） | 同上 |
| `SmoothedHeartRate` | 平滑后的心率数据（属于 TransformedData） | 同上 |
| `HeartRateAlert` | 心率告警数据（属于 ConsumableData） | 同上 |
| `StepCount` | 步数统计（属于 ConsumableData） | 同上 |
| `AlertLevel` | 告警等级枚举（NORMAL, WARNING, CRITICAL） | 同上 |

## 三、处理流水线相关数据结构

| 数据结构 | 作用 | 文件链接 |
| --- | --- | --- |
| `DataProcessor<IN, OUT>` | 单步数据转换处理器接口 | [domain/processor/DataProcessor.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/DataProcessor.kt) |
| `StreamingProcessor<IN, OUT>` | 流式数据处理器接口 | [domain/processor/StreamingProcessor.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/StreamingProcessor.kt) |
| `ProcessorChain` | 组合多个 DataProcessor 形成链式调用 | [domain/processor/ProcessorChain.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/ProcessorChain.kt) |
| `ProcessorRegistry` | 处理器注册中心（单例） | [domain/processor/ProcessorRegistry.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/ProcessorRegistry.kt) |
| `HeartRateSmoother` | 心率平滑处理器（示例实现） | [domain/processor/impl/HeartRateSmoother.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/impl/HeartRateSmoother.kt) |
| `HeartRateAnalyzer` | 心率分析处理器（示例实现） | [domain/processor/impl/HeartRateAnalyzer.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/processor/impl/HeartRateAnalyzer.kt) |

## 四、策略与同步状态

| 数据结构 | 作用 | 文件链接 |
| --- | --- | --- |
| `StorageTarget` | 存储目标枚举（LOCAL_ONLY, REMOTE_ONLY, BOTH） | [domain/strategy/ProcessingStrategy.kt](../domain/src/main/java/com/sea/auspicious_sign/domain/strategy/ProcessingStrategy.kt) |
| `ProcessingStrategy` | 处理策略接口（决策是否本地处理、存储目标） | 同上 |
| `SyncStatus` | 同步状态枚举（PENDING, SUCCESS, FAILED） | [sync/SyncStatus.kt](../domain/src/main/java/com/sea/auspicious_sign/sync/SyncStatus.kt) |

## 五、网络模型与配置

| 数据结构 | 作用 | 文件链接 |
| --- | --- | --- |
| `UploadResponse` | 上传文件到 Netlify Blobs 的响应（blobKey, jobId） | [data/remote/model/NetlifyModels.kt](../domain/src/main/java/com/sea/auspicious_sign/data/remote/model/NetlifyModels.kt) |
| `ProcessRequest` | 请求云端处理的请求体（blobKey, dataType） | 同上 |
| `ResultResponse` | 获取处理结果的响应（status, result） | 同上 |
| `AppConfig` | 全局配置数据类（服务器地址、上传间隔等） | [utils/AppConfig.kt](../domain/src/main/java/com/sea/auspicious_sign/utils/AppConfig.kt) |

---

**生成时间**：2026-04-21  
**维护者**：项目团队