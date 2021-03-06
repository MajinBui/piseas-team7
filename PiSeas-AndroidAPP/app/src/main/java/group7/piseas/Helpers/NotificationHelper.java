package group7.piseas.Helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import group7.piseas.R;

/**
 * Created by Van on 2016-12-06.
 */

public class NotificationHelper {
    /**
     * Helper function used to create notifications.
     * @param context the application context
     * @param cls the activity to open when the notification is opened
     * @param title the title of the notification
     * @param message the message of the notification
     * @param id the id of the notification
     * @param index the index of the tank that created this notification
     */
    public static void createNotification(Context context, Class cls, String title, String message, int id, int index) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBuilder.setSmallIcon(R.drawable.ic_stat_onesignal_default);
            mBuilder.setColor(0xff123456);
        } else {
            mBuilder.setSmallIcon(R.drawable.icon);
        }
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(context, cls);
        resultIntent.putExtra("id", index);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(cls);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        for (int i = 0; i < stackBuilder.getIntentCount(); i++) {
            stackBuilder.editIntentAt(i).putExtra("id", index);
        }
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(id, mBuilder.build());
    }
}
