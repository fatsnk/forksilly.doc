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
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.app.ActivityManager
import android.content.Context

import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate

import expo.modules.ReactActivityDelegateWrapper

class MainActivity : ReactActivity() {
  // private val READ_STORAGE_PERMISSION_REQUEST_CODE = 100 // 移除
  private val READ_MEDIA_IMAGES_PERMISSION_REQUEST_CODE = 101 // 保留媒体权限请求码
  private val POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE = 102 // 通知权限请求码

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.AppTheme);
    super.onCreate(null)
    //super.onCreate(savedInstanceState)
    //hideNavigationBar()
    window.decorView.postDelayed({
        hideNavigationBar()
    }, 100)
    requestMediaPermissionIfNeeded() // 调用新的权限检查方法
    requestNotificationPermissionIfNeeded() // 调用通知权限检查方法
  }

  override fun onResume() {
      super.onResume()
      hideNavigationBar()
      // 只有当服务真正作为前台服务运行时才停止，避免在服务启动过程中停止导致 Crash
      if (isForegroundServiceRunning(MyForegroundService::class.java)) {
          val serviceIntent = Intent(this, MyForegroundService::class.java)
          stopService(serviceIntent)
      }
  }

  @Suppress("DEPRECATION")
  private fun isForegroundServiceRunning(serviceClass: Class<*>): Boolean {
      val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
      for (service in manager.getRunningServices(Int.MAX_VALUE)) {
          if (serviceClass.name == service.service.className) {
              return service.foreground
          }
      }
      return false
  }

  override fun onPause() {
      super.onPause()
      try {
          val serviceIntent = Intent(this, MyForegroundService::class.java)
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              startForegroundService(serviceIntent)
          } else {
              startService(serviceIntent)
          }
      } catch (e: Exception) {
          e.printStackTrace()
      }
  }


  private val uiVisibilityListener = View.OnSystemUiVisibilityChangeListener { visibility ->
    if (visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0) {
        window.decorView.postDelayed({
            hideNavigationBar()
        }, 2000)
    }
  }

override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    if (hasFocus) {
        hideNavigationBar()
        window.decorView.setOnSystemUiVisibilityChangeListener(uiVisibilityListener)
    }
}

  private fun hideNavigationBar() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    // Android 11 及以上
    window.setDecorFitsSystemWindows(false)
    window.insetsController?.let {
        it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars()) //隐藏系统UI
        // it.hide(WindowInsets.Type.navigationBars())
        //it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // 改用这个行为：上划直接响应系统手势
        it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_DEFAULT
    }
} else {
    // Android 10 及以下
    @Suppress("DEPRECATION")
    window.decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_FULLSCREEN
        
    )
}
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

  private fun requestNotificationPermissionIfNeeded() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
              requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE)
          }
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
          POST_NOTIFICATIONS_PERMISSION_REQUEST_CODE -> {
              if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  // Notification permission granted
              } else {
                  // Notification permission denied
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
