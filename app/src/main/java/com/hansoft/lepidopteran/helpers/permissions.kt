package com.hansoft.lepidopteran.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object GeoPermission {

    @SuppressLint("InlinedApi")
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    /** Check to see if we have the necessary permissions for this app. */
    fun hasGeoPermissions(activity: Activity): Boolean {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    /** Check to see if we have the necessary permissions for this app and ask for them if we don't. */
    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 0)
    }

    /** Check to see if we need to show the rationale for this permission. */
    fun shouldShowRequestPermissionRationale(activity: Activity): Boolean {
        for (permission in PERMISSIONS) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true
            }
        }
        return false
    }

    /** Launch Application Settings to grant permission. */
    fun launchPermissionSettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }
}
