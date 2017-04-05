package group7.piseas.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.format.DateUtils;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Van on 2017-04-04.
 *
 * This receiver is strictly used just to activate the service on phone boot up.
 *
 */

public class PiseasReceiver extends WakefulBroadcastReceiver {

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("PiseasReceiver", "PiseasReceiver activated");
        SharedPreferences settings = context.getSharedPreferences("piseas", MODE_PRIVATE);
        boolean isChecked = settings.getBoolean("allowNotifications", false);
        if (isChecked) {
            Intent startServiceIntent = new Intent(context, PiseasService.class);

            PendingIntent pi = PendingIntent.getService(context, 0, startServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            long firstMillis = System.currentTimeMillis(); // alarm is set right away

            AlarmManager FPAlarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            FPAlarm.setRepeating(AlarmManager.RTC, firstMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
            //FPAlarm.setRepeating(AlarmManager.RTC, firstMillis, 60000, pi);
            Log.i("PiseasReceiver", "PiseasReceiver did start service");
        } else {
            Log.i("PiseasReceiver", "PiseasReceiver did not start service");
        }
    }


}
