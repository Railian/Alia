package ua.railian.alia.alerts

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ua.railian.alia.core.Alia

fun notification(
    channel: AliaNotificationChannel,
    id: Int = 1,
    builder: NotificationCompat.Builder.() -> Unit
) = AlertNotification(channel, id) { context ->
    NotificationCompat.Builder(context, if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) channel.baker(context).id else "").apply(builder).build()
}

class AlertNotification(
    val channel: AliaNotificationChannel,
    val id: Int = 1,
    val baker: (Context) -> Notification
) : AliaAlert<Notification>

infix fun AliaContextAlerts.make(notification: AlertNotification): Notification {
    createOrUpdate(notification.channel)
    return notification.baker.invoke(context)
}

infix fun AliaContextAlerts.show(notification: AlertNotification): Notification =
    make(notification).apply {
        NotificationManagerCompat.from(context).notify(notification.id, this)
    }

fun notificationChannel(
    id: String,
    builder: NotificationChannel.() -> Unit = {}
): AliaNotificationChannel =
    AliaNotificationChannel {
        NotificationChannel(
            id,
            id,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply(builder)
    }

class AliaNotificationChannel(val baker: (Context) -> NotificationChannel)

@RequiresApi(Build.VERSION_CODES.O)
object NotificationChannels {

    val DEBUG = notificationChannel("Debug") {
        name = "Debug channel"
        importance = NotificationManager.IMPORTANCE_HIGH
    }

    val SYSTEM = notificationChannel("System") {
        name = "System channel"
        importance = NotificationManager.IMPORTANCE_LOW
    }
}

infix fun AliaContextAlerts.make(channel: AliaNotificationChannel): NotificationChannel? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { channel.baker.invoke(context)} else null

infix fun AliaContextAlerts.createOrUpdate(channel: AliaNotificationChannel): NotificationChannel? =
    make(channel)?.apply { NotificationManagerCompat.from(context).createNotificationChannel(this) }

@SuppressLint("NewApi")
infix fun AliaContextAlerts.delete(channel: AliaNotificationChannel): NotificationChannel? =
    make(channel)?.apply { NotificationManagerCompat.from(context).deleteNotificationChannel(this.id) }

fun notificationUsage(context: Context, activity: AppCompatActivity) {

    Alia.ALERTS[context] show notification(id = 2, channel = NotificationChannels.DEBUG) {
        setContentTitle("Hello, world!")
        setContentText("Are you ready for Alia notifications?")
    }

    Alia.ALERTS[context] createOrUpdate NotificationChannels.SYSTEM
    Alia.ALERTS[context] delete NotificationChannels.SYSTEM
    Alia.ALERTS[context] delete notificationChannel("System")
}