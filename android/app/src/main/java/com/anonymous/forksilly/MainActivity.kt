package com.anonymous.forksilly

import android.os.Build
import android.os.Bundle
import android.app.Activity // 需要为 setting()
import android.content.Intent // 需要为 setting()
import android.net.Uri // 需要为 setting()
import android.os.Environment // 需要为 setting() 和 forceShow...
import android.provider.Settings // 需要为 setting()
import android.Manifest // 需要为 forceShow...
import android.content.pm.PackageManager // 需要为 forceShow...
import android.util.Log // 日志

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate

import expo.modules.ReactActivityDelegateWrapper

class MainActivity : ReactActivity() {
  // private val READ_STORAGE_PERMISSION_REQUEST_CODE = 100 // 移除
  private val READ_MEDIA_IMAGES_PERMISSION_REQUEST_CODE = 101 // 保留媒体权限请求码

  override fun onCreate(savedInstanceState: Bundle?) {
    // Log.d("PermissionDebug", "MainActivity onCreate: TOP OF METHOD") // 移除日志
    setTheme(R.style.AppTheme);
    // Log.d("PermissionDebug", "MainActivity onCreate: setTheme called") // 移除日志
    super.onCreate(null)
    // Log.d("PermissionDebug", "MainActivity onCreate: super.onCreate called") // 移除日志
    requestMediaPermissionIfNeeded() // 调用新的权限检查方法
    // Log.d("PermissionDebug", "MainActivity onCreate: requestMediaPermissionIfNeeded called") // 移除日志
  }

  private fun requestMediaPermissionIfNeeded() {
    // Log.d("PermissionDebug", "requestMediaPermissionIfNeeded called. API Level: " + Build.VERSION.SDK_INT) // 移除日志
    // 仅当设备是 Android 13 (API 33) 及以上版本时，才请求 READ_MEDIA_IMAGES
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Log.d("PermissionDebug", "Device is Android 13 or newer.") // 移除日志
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            // Log.d("PermissionDebug", "READ_MEDIA_IMAGES permission: NOT GRANTED. Requesting...") // 移除日志
            requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), READ_MEDIA_IMAGES_PERMISSION_REQUEST_CODE)
        } else {
            // Log.d("PermissionDebug", "READ_MEDIA_IMAGES permission: ALREADY GRANTED.") // 移除日志
        }
    } else {
        // Log.d("PermissionDebug", "Device is older than Android 13. READ_MEDIA_IMAGES not applicable via this logic.") // 移除日志
        // 对于旧版本，如果需要访问存储，Expo 通常会处理 READ_EXTERNAL_STORAGE（如果已在 Manifest 中声明）
        // 或者应用需要通过其他方式（如 expo-image-picker）请求。
        // 此处不再主动请求 READ_EXTERNAL_STORAGE。
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
      super.onRequestPermissionsResult(requestCode, permissions, grantResults)
      // Log.d("PermissionDebug", "onRequestPermissionsResult called. RequestCode: " + requestCode) // 移除日志
      when (requestCode) {
          READ_MEDIA_IMAGES_PERMISSION_REQUEST_CODE -> {
              if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  // Log.d("PermissionDebug", "READ_MEDIA_IMAGES permission GRANTED by user.") // 移除日志
              } else {
                  // Log.d("PermissionDebug", "READ_MEDIA_IMAGES permission DENIED by user.") // 移除日志
              }
          }
          // READ_STORAGE_PERMISSION_REQUEST_CODE case removed
      }
  }

  // setting() 方法已移除

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun getMainComponentName(): String = "main"

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate {
    return ReactActivityDelegateWrapper(
          this,
          BuildConfig.IS_NEW_ARCHITECTURE_ENABLED,
          object : DefaultReactActivityDelegate(
              this,
              mainComponentName,
              fabricEnabled
          ){})
  }

  /**
    * Align the back button behavior with Android S
    * where moving root activities to background instead of finishing activities.
    * @see <a href="https://developer.android.com/reference/android/app/Activity#onBackPressed()">onBackPressed</a>
    */
  override fun invokeDefaultOnBackPressed() {
      if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
          if (!moveTaskToBack(false)) {
              // For non-root activities, use the default implementation to finish them.
              super.invokeDefaultOnBackPressed()
          }
          return
      }

      // Use the default back button implementation on Android S
      // because it's doing more than [Activity.moveTaskToBack] in fact.
      super.invokeDefaultOnBackPressed()
  }
}
