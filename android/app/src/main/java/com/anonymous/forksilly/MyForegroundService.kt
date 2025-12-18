package com.anonymous.forksilly

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.content.pm.ServiceInfo

class MyForegroundService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT else PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("应用正在后台运行")
            .setContentText("点击返回应用")
            .setSmallIcon(R.mipmap.ic_launcher) // 使用现有图标
            .setContentIntent(pendingIntent)
            .setOngoing(true) // 不可滑动清除
            .setPriority(NotificationCompat.PRIORITY_LOW) // 低优先级，减少打扰
            .build()

        // Android 14 (API 34) 需要指定类型，这里配合 Manifest 里的 dataSync
        // 实际上从 Android 10 (API 29) 开始 startForeground 就支持传入 type，
        // 但强制要求是在 Android 14。为了兼容性，我们检查 SDK 版本。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
             // 这里的 type 需要和 Manifest 中声明的一致
             // ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC 需要 API 29+ (其实是 API 34 强制，但常量在更早版本可能不可用或者不同，这里直接用常量)
             // 注意：FOREGROUND_SERVICE_TYPE_DATA_SYNC 是 API 29 引入的吗？
             // 查阅文档：FOREGROUND_SERVICE_TYPE_DATA_SYNC Added in API level 29.
             // 所以可以直接使用。
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            startForeground(1, notification)
        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "后台保活服务",
                NotificationManager.IMPORTANCE_LOW // 低重要性，不发出声音
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
    }
}