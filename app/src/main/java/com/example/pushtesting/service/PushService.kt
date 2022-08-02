package com.example.pushtesting.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import com.example.pushtesting.DialogActivity
import com.example.pushtesting.MainActivity
import com.example.pushtesting.R
import com.example.pushtesting.SoftTokenActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "From: ${message.from}")

        sendNotification(message.notification?.body.orEmpty())
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String) {
        val confirmationIntent = Intent(this, DialogActivity::class.java)
        val tokenIntent = Intent(this, SoftTokenActivity::class.java)
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingConfirmationIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this, 0 /* Request code */, confirmationIntent,
                PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0 /* Request code */, confirmationIntent,
                PendingIntent.FLAG_ONE_SHOT)
        }

        val pendingTokenIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this, 0 /* Request code */, tokenIntent,
                PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0 /* Request code */, tokenIntent,
                PendingIntent.FLAG_ONE_SHOT)
        }

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Push from guardian")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText(messageBody)
            .setDeleteIntent(pendingConfirmationIntent)
            .setContentIntent(pendingTokenIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Guardian",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    companion object {
        private const val TAG = "PushService"
    }

}