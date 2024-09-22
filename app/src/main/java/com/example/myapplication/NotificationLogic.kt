package com.example.myapplication

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.sql.Time
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class PinNotificationManager: Service() {

    private val context: Context = this;

    companion object {
        const val CHANNEL_ID: String = "CHANNEL_ID";
        const val CHANNEL_NAME: String = "CHANNEL_NAME";
        const val CHANNEL_DESCRIPTION: String = "CHANNEL_DESCRIPTION";
        const val INTERVAL_ONE_MINUTE = 60000L;
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
        channel.description = CHANNEL_DESCRIPTION;
        channel.enableLights(false);

        val notificationManager = context.getSystemService(NotificationManager::class.java);
        notificationManager.createNotificationChannel(channel);
    }

    override fun onStartCommand(p0: Intent?, p1: Int, p2: Int): Int {
        createNotificationChannel();
        val intent = Intent(context, NotificationBroadcastReceiver::class.java).setAction("com.example.myapplication.NOTIFICATION_UPDATE");
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        val alarmManager = context.getSystemService(AlarmManager::class.java);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000L, INTERVAL_ONE_MINUTE, pendingIntent);

        val notification = Notification.Builder(context, CHANNEL_ID)
            .setContentTitle("Notification Service Initiated.")
            .setContentText("Will update course data in 1 min")
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .build();
        startForeground(1, notification);

        return START_STICKY;
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }

    override fun onDestroy() {
        stopSelf();
    }

}

class NotificationBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val currentTime = LocalDateTime.now();
        val nextCourse = TimeCalculationUtil.getNextCourse();

        val title: String; val content: String;
        if (nextCourse == null) {
            title = "No more classes today";
            content = "Hooray!";
        }
        else {
            val courseTime = LocalDateTime.of(currentTime.year, currentTime.month, currentTime.dayOfMonth, nextCourse.startHour, nextCourse.startMinute);
            val mins = ChronoUnit.MINUTES.between(currentTime, courseTime);
            title = "Next: ${nextCourse.name} in ${mins / 60} hr ${mins % 60} min";
            content = "at ${nextCourse.location}, get ready!";
        }
        val notification = createNotification(context, title, content);
        val notificationManager = context.getSystemService(NotificationManager::class.java);
        notificationManager.notify(1, notification);
    }

    private fun createNotification(context: Context, title: String, content: String): Notification {
        val notificationBuilder = Notification.Builder(context, PinNotificationManager.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(false)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.ic_launcher_background);
        val notification = notificationBuilder.build();

        val intent = Intent();
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        notification.contentIntent = pendingIntent;

        return notification;
    }
}