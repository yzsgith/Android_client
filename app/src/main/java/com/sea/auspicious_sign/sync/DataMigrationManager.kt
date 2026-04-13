// DataMigrationManager.kt
//TODO:策略：存储一个独立的数据版本号，与 App 版本解耦，用于精细控制数据库、配置、文件的迁移。
object DataMigrationManager {
    private const val DATA_VERSION_KEY = "data_schema_version"
    private const val CURRENT_DATA_VERSION = 1

    suspend fun migrateIfNeeded(context: Context) {
        val dataStore = PreferenceDataStoreFactory.create(
            corruptionHandler = null,
            migrations = listOf(),
            produceFile = { context.filesDir.resolve("data_version.preferences_pb") }
        )
        val current = dataStore.data.map { prefs -> prefs[DATA_VERSION_KEY] ?: 0 }.first()
        if (current < CURRENT_DATA_VERSION) {
            // TODO: 执行从 current 到 CURRENT_DATA_VERSION 的迁移步骤
            dataStore.edit { settings ->
                settings[DATA_VERSION_KEY] = CURRENT_DATA_VERSION
            }
        }
    }
}
