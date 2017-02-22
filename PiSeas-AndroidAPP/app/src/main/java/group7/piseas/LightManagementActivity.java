package group7.piseas;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;


public class LightManagementActivity extends AppCompatActivity {
    private final String tankID = "Matt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_managment);
    }

    public void addSchedule(View view) {
        Intent i = new Intent(this, LightEditActivity.class);
        startActivity(i);
    }


    public void getData() {
//        HashMap<String, String> retrieveList = FishyClient.retrieveServerData(tankID);
//
//        if (!retrieveList.get(light).equals("-")) {
//            String[] separate = retrieveList.get(light).split(divider);
//            for (String aSeparate : separate) {
//                int hOn, mOn, hOff, mOff;
//                String temp = aSeparate.replace(" - ", ":");
//                String[] times = temp.split(":");
//                hOn = Integer.parseInt(times[0]);
//                mOn = Integer.parseInt(times[1]);
//                hOff = Integer.parseInt(times[2]);
//                mOff = Integer.parseInt(times[3]);
//                LightSchedule dayLight = new LightSchedule(hOn, hOff, mOn, mOff);
//                schedule.add(dayLight);
//            }
//        }


    }
}
