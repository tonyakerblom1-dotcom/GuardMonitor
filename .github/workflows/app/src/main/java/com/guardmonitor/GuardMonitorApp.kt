package com.guardmonitor

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import java.io.File

class GuardMonitorApp : Application() {
    
    companion object {
        lateinit var instance: GuardMonitorApp
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannels()
        initLoggingSystem()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val criticalChannel = NotificationChannel(
                "CRITICAL_ALERTS",
                "Security Alerts",
                NotificationManager.IMPORTANCE_MAX
            ).apply {
                description = "IMSI-Catcher Detection Alerts"
                enableVibration(true)
            }
            
            getSystemService(NotificationManager::class.java)?.createNotificationChannel(criticalChannel)
        }
    }

    private fun initLoggingSystem() {
        val logDir = File(filesDir, "logs")
        if (!logDir.exists()) logDir.mkdirs()
        File(logDir, "monitor").mkdirs()
        File(logDir, "alerts").mkdirs()
    }
}
