package com.teamup.amazingoptsmssend.Serves

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.teamup.amazingoptsmssend.R
import com.teamup.amazingoptsmssend.api.MySharedPreferences
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FloatingWidgetService : Service() {
    private var mWindowManager: WindowManager? = null
    private var mFloatingWidgetView: View? = null
    private var initialY: Int = 0
    private val handler = Handler()
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
        startBackgroundTask()
     startForegroundService()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Service destroyed")

        mFloatingWidgetView?.let {
            mWindowManager?.removeView(it)
        }
    }

    private fun startBackgroundTask() {

        handler.postDelayed(smsCheckRunnable, 20000)
    }

    private val smsCheckRunnable = object : Runnable {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            initialY += 1
            val mySharedPreferences = MySharedPreferences(applicationContext)

                mySharedPreferences.getList("key").forEach {
                    SandDate(applicationContext,it)
                }

            handler.postDelayed(this, 20000)
        }
    }


    private fun startForegroundService() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("ForegroundService", "Foreground Service Channel")
            } else {
                ""
            }

        val notification = buildNotification("The application is working $initialY")
        startForeground(FOREGROUND_NOTIFICATION_ID, notification)
        handler.postDelayed(updateNotificationTextRunnable, 1000)
    }

    private val updateNotificationTextRunnable = object : Runnable {
        override fun run() {
            val notificationText = "The application is working $initialY"
            val notification = buildNotification(notificationText)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(FOREGROUND_NOTIFICATION_ID, notification)
            handler.postDelayed(this, 1000)
        }
    }
    private fun buildNotification(notificationText: String): Notification {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("ForegroundService",R.string.app_name.toString())
            } else {
                ""
            }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle( R.string.app_name.toString())
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.notification)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    companion object {
        private const val TAG = "FloatingWidgetService"
        private const val FOREGROUND_NOTIFICATION_ID = 1
    }
}

