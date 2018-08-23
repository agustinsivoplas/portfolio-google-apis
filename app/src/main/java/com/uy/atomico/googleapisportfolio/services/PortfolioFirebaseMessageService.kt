package com.uy.atomico.googleapisportfolio.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.uy.atomico.googleapisportfolio.R

/**
 * Created by agustin.sivoplas@gmail.com on 8/12/18.
 * Atomico Labs
 */

class PortfolioFirebaseMessageService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "portfolio_channel_id"
        const val CHANNEL_NAME = "Portfolio Channel"
        const val CHANNEL_DESC = "Google Apis Portfolio Channel"
        const val KEY_TITLE = "title"
        const val KEY_MESSAGE = "message"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_foreground )
                    .setColorized(true)
                    .setContentTitle(remoteMessage.data[KEY_TITLE])
                    .setContentText(remoteMessage.data[KEY_MESSAGE])
                    .setColor(ContextCompat.getColor(this, R.color.primaryNotificationColor))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            createNotificationChannel()
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.notify(999, mBuilder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.description = CHANNEL_DESC
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}