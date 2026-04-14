import android.app.Application
import androidx.work.WorkManager
import com.sea.auspicious_sign.BuildConfig

// AuspiciousSignApplication.kt
// TODO: 策略：在应用启动时检查版本号，若升级则取消旧版本的任务并重新安排。
class AuspiciousSignApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val prefs = getSharedPreferences("app_version", MODE_PRIVATE)
        val oldVersion = prefs.getInt("data_version", 0)
        val currentVersion = BuildConfig.VERSION_CODE

        if (oldVersion < currentVersion) {
            // TODO: 根据版本号差异执行清理逻辑
            if (oldVersion < 2) {
                WorkManager.getInstance(this).cancelAllWorkByTag("sensor_upload")
                // 重新安排新版本任务
                scheduleUploadWork()
            }
            prefs.edit().putInt("data_version", currentVersion).apply()
        }
    }

    private fun scheduleUploadWork() {
        // TODO: 使用 WorkManager 安排新版本的上传任务
    }
}
