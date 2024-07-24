package it.application.team_tracker.model.remoteDataSource

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import it.application.team_tracker.MainActivity

//TODO da testare
class DownloadService(val name: String, val download: () -> Unit): Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            "DownloadServiceChannel",
            "Download Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(serviceChannel)
    }

    private fun getPendingIntent(): PendingIntent {
        val notificationIntent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notification: Notification = NotificationCompat.Builder(this, "DownloadServiceChannel")
            .setContentTitle("Team Tracker")
            .setContentText("Downloading $name")
            //.setSmallIcon(R.drawable.ic_download) // TODO Replace with your app's notification icon
            .setContentIntent(getPendingIntent())
            .build()
        startForeground(1, notification)
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Thread {
            download()
        }
        return START_NOT_STICKY
    }
}