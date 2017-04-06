package group7.piseas.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import group7.piseas.Helpers.NotificationHelper;
import group7.piseas.Helpers.Utilities;
import group7.piseas.LightManagementActivity;
import group7.piseas.Objects.PiseasLog.Logs;
import group7.piseas.Objects.PiseasLog.PiseasLog;
import group7.piseas.Objects.Tank;
import group7.piseas.TankListActivity;
import group7.piseas.TemperatureManagementActivity;
import group7.piseas.WaterAnalysisManagementActivity;
import group7.piseas.WaterLevelManagementActivity;

/**
 * Created by Van on 2017-04-04.
 * Piseas service used to periodically check for new action log items and make notifications
 * for each entry.
 */

public class PiseasService extends IntentService{

    public PiseasService() {
        super("PiseasService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("PiseasService", "Service running");
        makeNotifications();
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void makeNotifications() {
        List<Tank> tankList = TankListActivity.tankList;
        if (tankList == null) {
            tankList = new ArrayList<Tank>();
        }
        load(tankList);
        TankListActivity.tankList = tankList;
        if (!tankList.isEmpty()) {
            Log.i("PiseasService", "tanklist not empty");
            SharedPreferences sharedPref = getSharedPreferences("piseas", MODE_PRIVATE);
            for (Tank tank : tankList) {
                Log.i("PiseasService", "tank: " + tank.getId());
                tank.retrieveActionLogFromServer();
                Logs logs = tank.getPiSeasXmlHandler().getLogs();
                String prefKey = "logdate"+tank.getId();

                long dateLastLogCheck = sharedPref.getLong(prefKey, new Date(Long.MIN_VALUE).getTime());
                long newLastLogCheck = dateLastLogCheck;
                for (PiseasLog log : logs.getPiseasLogs()) {
                    Log.i("PiseasService", "getting log");
                    Log.i("PiseasService", Utilities.dateToString(log.getDate()) + " vs " + Utilities.dateToString(new Date(dateLastLogCheck)));
                    // Stop immediately when notifications has already been read
                    if (log.getDate().compareTo(new Date(dateLastLogCheck)) <= 0 )
                        break;
                    // Build notification
                    if (log.getType().equals("NOT")) {
                        Class cls = TankListActivity.class;
                        String title = "Piseas";
                        String message = log.getDescription();
                        int id = 0;  // notification id, used to layer how notifications are sent
                        if (log.getDescription().toLowerCase().contains("temp")) {
                            cls = TemperatureManagementActivity.class;
                            id = 11;
                        } else if (log.getDescription().toLowerCase().contains("ph") || log.getDescription().toLowerCase().contains("conductivity")) {
                            cls = WaterAnalysisManagementActivity.class;
                            id = 12;
                        } else if (log.getDescription().toLowerCase().contains("water")) {
                            cls = WaterLevelManagementActivity.class;
                            id = 13;
                        } else if (log.getDescription().toLowerCase().contains("light")) {
                            cls = LightManagementActivity.class;
                            id = 14;
                        }
                        NotificationHelper.createNotification(this, cls, title, message, id, tankList.indexOf(tank));
                    }
                    // reset the current last date checked for notifications
                    if (log.getDate().compareTo(new Date(newLastLogCheck)) > 0)
                        newLastLogCheck = log.getDate().getTime();
                }
                // save changes
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong(prefKey, newLastLogCheck);
                editor.commit();
            }
        }
    }

    private void load(List<Tank> tankList){
        Log.i("PiseasService", "LOAD");
        SharedPreferences sharedPref = getSharedPreferences("piseas", MODE_PRIVATE);
        // clear old shared preferences if old version
        try {
            String tankCode = sharedPref.getString("code"+0, "0");
        } catch (ClassCastException e) {
            SharedPreferences prefs = getSharedPreferences("piseas", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
        }
        int size = sharedPref.getInt("listSize", 0);
        Log.i("PiseasService", "LOAD size " + size);

        int pw = 0;
        String name = "";
        String desc = "";
        int type = 0;
        int tankSize = 0;

        tankList.clear();
        for (int i=0;i<size; i++){
            String tankCode = sharedPref.getString("code"+i, "0");
            Log.i("PiseasService", "LOAD Tank Code" + tankCode);
            try {
                Log.i("PiseasService", "LOAD ADD TANK " + tankCode + ", "+ pw + ", "+ name + ", "+ type + ", "+ size+ ", "+desc);
                tankList.add(new Tank(getApplicationContext(), tankCode));
            } catch (NullPointerException e) {
                //Toast.makeText(this, "No internet connection available!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
