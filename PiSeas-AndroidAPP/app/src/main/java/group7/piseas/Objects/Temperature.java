package group7.piseas.Objects;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

import group7.piseas.Helpers.NotificationHelper;
import group7.piseas.TemperatureManagementActivity;

/**
 * Created by Van on 2016-12-06.
 */

public class Temperature {
    private Context context;
    private long temperature;
    private TextView textView;
    private Temperature() {
        textView = null;
    }
    public Temperature(Context context, TextView view) {
        this.textView = view;
        this.context = context;
    }
    public void run(HashMap<String, String> dataList) {
        String in = dataList.get("temperature");
        if (isRunnable())
            textView.setText(in);
        try {
            temperature = Long.parseLong(in.replaceAll("\\D+", ""));
            long tempMax = Long.parseLong(dataList.get("Max Temp").replaceAll("\\D+", ""));
            long tempMin = Long.parseLong(dataList.get("Min Temp").replaceAll("\\D+", ""));

            if (temperature > tempMax && temperature > tempMin ) {
                //NotificationHelper.createNotification(context, TemperatureManagementActivity.class, "PiSeas", "Temperature levels dangerous", 11, 0);
            }
        } catch (NumberFormatException e) {
            Log.d("", e.getMessage());
        }
    }
    public String toString() {
        return temperature + "°C";
    }
    public boolean isRunnable() {
        return textView != null;
    }
}
