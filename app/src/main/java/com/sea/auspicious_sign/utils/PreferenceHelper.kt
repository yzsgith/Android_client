// PreferenceHelper.kt
// TODO: 策略：读取配置时提供默认值；若旧版本键名变更，在升级时迁移。
object PreferenceHelper {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_AUTO_UPLOAD = "auto_upload"
    private const val KEY_OLD_AUTO_UPLOAD = "old_auto_upload"  // 旧版本键名

    fun getAutoUpload(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        // TODO: 处理键名迁移，检查旧键是否存在
        if (prefs.contains(KEY_OLD_AUTO_UPLOAD)) {
            val oldValue = prefs.getBoolean(KEY_OLD_AUTO_UPLOAD, true)
            prefs.edit().putBoolean(KEY_AUTO_UPLOAD, oldValue).remove(KEY_OLD_AUTO_UPLOAD).apply()
        }
        return prefs.getBoolean(KEY_AUTO_UPLOAD, true)
    }
}
