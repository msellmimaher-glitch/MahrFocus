package com.maher.focus

import android.app.Application
import com.maher.focus.notifications.NotificationScheduler

class MaherFocusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        NotificationScheduler.createNotificationChannel(this)
        NotificationScheduler.scheduleAllEnabledReminders(this)
    }
}
