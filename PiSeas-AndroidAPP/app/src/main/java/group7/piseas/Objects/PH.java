package group7.piseas.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

import group7.piseas.Helpers.NotificationHelper;
import group7.piseas.TemperatureManagementActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Van on 2016-12-06.
 */

public class PH {
    private Context context;
    private long pH;
    private TextView textView;
    private PH(){}
    public PH(Context context, TextView view) {
        this.textView = view;
        this.context = context;
    }
    public void run(HashMap<String, String> dataList) {
        String in = dataList.get("ph Level");
        if (isRunnable())
            textView.setText(in);
        try {
            pH = Long.parseLong(in.replaceAll("\\D+", ""));
            long pHMax = Long.parseLong(dataList.get("Max pH").replaceAll("\\D+", ""));
            long pHMin = Long.parseLong(dataList.get("Min pH").replaceAll("\\D+", ""));
            SharedPreferences sharedPref = context.getSharedPreferences("piseas", MODE_PRIVATE);

            if (pH > pHMax && pH > pHMin ) {
                new NotificationHelper(context, TemperatureManagementActivity.class, "PiSeas", "pH levels dangerous");
            }
        } catch (NumberFormatException e) {
            Log.d("", e.getMessage());
        }
    }
    public String toString() {
        return pH + "";
    }
    public boolean isRunnable() {
        return textView != null;
    }
}
