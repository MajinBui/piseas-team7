package group7.piseas;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import group7.piseas.Objects.LightSchedule;
import group7.piseas.Server.FishyClient;

/**
 * Created by mmbab on 11/30/2016.
 *
 */

public class LightEditActivity extends Activity{

    NumberPicker pickerHrOn;
    NumberPicker pickerMinOn;
    NumberPicker pickerHrOff;
    NumberPicker pickerMinOff;

    private ArrayList<LightSchedule> schedule;
    private LightSchedule lightSchedule;
    private final String[] days = new String[]{"Sunday", "Monday", "Tuesday",
            "Wednesday", "Thursday", "Friday", "Saturday"};
    private String feed[];
    private int hourOn, minOn, hourOff, minOff;
    private String light = "Lights";
    private final String tankID = "Matt";
    private final String divider = "<br/>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_edit_schedule);

        schedule = new ArrayList<LightSchedule>();

        pickerHrOn = (NumberPicker) findViewById(R.id.picker_hour_on);
        pickerMinOn = (NumberPicker) findViewById(R.id.picker_minute_on);
        pickerHrOff = (NumberPicker) findViewById(R.id.picker_hour_off);
        pickerMinOff = (NumberPicker) findViewById(R.id.picker_minute_off);

        pickerHrOn.setMinValue(0);
        pickerHrOn.setMaxValue(23);
        pickerMinOn.setMinValue(0);
        pickerMinOn.setMaxValue(59);
        pickerHrOff.setMinValue(0);
        pickerHrOff.setMaxValue(23);
        pickerMinOff.setMinValue(0);
        pickerMinOff.setMaxValue(59);

        pickerHrOn.setWrapSelectorWheel(true);
        pickerMinOn.setWrapSelectorWheel(true);
        pickerHrOff.setWrapSelectorWheel(true);
        pickerMinOff.setWrapSelectorWheel(true);

        pickerHrOff.setValue(1);
        hourOff = 1;

        pickerHrOn.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                hourOn = newVal;
            }
        });
        pickerMinOn.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                minOn = newVal;
            }
        });

        pickerHrOff.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                hourOff = newVal;
            }
        });
        pickerMinOff.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                minOff = newVal;
            }
        });

        getData();
    }

    public void getData(){
        HashMap<String, String> retrieveList = FishyClient.retrieveServerData(tankID);

        if(!retrieveList.get(light).equals("-")) {
            String[] separate = retrieveList.get(light).split(divider);
            for (String aSeparate : separate) {
                int hOn, mOn, hOff, mOff;
                String temp = aSeparate.replace(" - ", ":");
                String[] times = temp.split(":");
                hOn = Integer.parseInt(times[0]);
                mOn = Integer.parseInt(times[1]);
                hOff = Integer.parseInt(times[2]);
                mOff = Integer.parseInt(times[3]);
                LightSchedule dayLight = new LightSchedule(hOn, hOff, mOn, mOff);
                schedule.add(dayLight);
            }
        }

        feed = new String[7];
        // Get all data from feeding schedule
        for(int i=0; i<7; i++)
            feed[i] = retrieveList.get(days[i]);
    }

    private boolean checkData(){
//        lightSchedule = new LightSchedule(hourOn, hourOff, minOn, minOff);
//        schedule.add(lightSchedule);
//
//        if(hourOn < hourOff || (hourOn == hourOff && minOn > minOff)){
//            Toast.makeText(this, "Off time must be after on time", Toast.LENGTH_LONG).show();
        int max = 2400, on = hourOn * 100 + minOn,
        off = hourOff * 100 + minOff;
        boolean fail = false;
        if(on == off){
            Toast.makeText(this, "On and off times must not be the same", Toast.LENGTH_LONG).show();
            return false;
        }

        for(LightSchedule l : schedule){
//            if((hourOn >= l.getOnHour() && hourOn <= l.getOffHour()) ||
//                    (hourOff >= l.getOnHour() && hourOff <= l.getOffHour()) ||
//                    (hourOn <= l.getOnHour() && hourOff >= l.getOffHour())
//                    ){
//                Toast.makeText(this, "Schedule times must not overlap", Toast.LENGTH_LONG).show();
//                return false;
            if(on < off){
                if(l.getTimeOnCompare() < l.getTimeOffCompare()){
                    if(off < l.getTimeOnCompare() || on > l.getTimeOffCompare())
                        ;
                    else
                    fail = true;
                    }
                else{
                    if(on < l.getTimeOffCompare() || off > l.getTimeOnCompare())
                        fail = true;
                    }
                }
            else{
                if((on <= l.getTimeOffCompare() || on <= l.getTimeOnCompare() ||
                        off >= l.getTimeOffCompare() || off >= l.getTimeOnCompare())
                        && l.getTimeOffCompare() > l.getTimeOnCompare())
                    fail = true;
            }
        }

        if (fail){
            Toast.makeText(this, "Schedule times must not overlap", Toast.LENGTH_LONG).show();
            return false;
            }

                lightSchedule = new LightSchedule(hourOn, hourOff, minOn, minOff);
        schedule.add(lightSchedule);
        return true;
    }

    public void feedSave(View view){
        HashMap<String, String> dataList = new HashMap<String, String>();
        if(checkData()){
            dataList.put("tankId", tankID);
            // Insert whatever was in the feeding schedule back into the hash map for display
            for(int i=0; i<7; i++)
                dataList.put(days[i], feed[i]);

            String temp = "";
            for(LightSchedule l : schedule)
                temp += l.getTime() + divider;

            dataList.put(light, temp);

            FishyClient.writeToServerData(tankID, dataList);

            finish();
        }
    }

    public void clear(View view){
        HashMap<String, String> cleaningLady = new HashMap<String, String>();
        cleaningLady.put("tankId", tankID);

        cleaningLady.put(light, "-");

        FishyClient.writeToServerData(tankID, cleaningLady);
        Toast.makeText(this, "Cleaning is done!", Toast.LENGTH_LONG).show();
    }
}

//http://vanchaubui.com/fish_tanks/Matt.html