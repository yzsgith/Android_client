package com.sea.auspicious_sign.utils

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionHelper {
    const val REQUEST_BODY_SENSORS = 1001

    fun hasBodySensorsPermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.BODY_SENSORS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    fun requestBodySensorsPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.BODY_SENSORS),
            REQUEST_BODY_SENSORS
        )
    }
}