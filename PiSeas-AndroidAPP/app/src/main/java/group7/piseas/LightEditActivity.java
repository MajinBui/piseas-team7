package group7.piseas;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import group7.piseas.Objects.LightSchedule;
import piseas.network.FishyClient;

/**
 * Created by mmbab on 11/30/2016.
 *
 */

public class LightEditActivity extends AppCompatActivity {

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
    private int index;
    private boolean autoReg;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_edit_schedule);

        title = (TextView) findViewById(R.id.title);
        title.setText("Feeding Settings: " + TankListActivity.tankList.get(index).getName());

        schedule = new ArrayList<LightSchedule>();

        index = getIntent().getIntExtra("index", -1);
        autoReg = getIntent().getBooleanExtra("auto", false);

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
        schedule.clear();
        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        schedule = TankListActivity.tankList.get(index).getPiSeasXmlHandler().getLightSchedules();
    }

    public void feedSave(View view){
        int on = hourOn * 100 + minOn,
                off = hourOff * 100 + minOff;
        boolean fail = false;
        if(on == off){
            Toast.makeText(this, "On and off times must not be the same", Toast.LENGTH_LONG).show();
            return;
        }

        for(LightSchedule l : schedule){
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
            return;
        }

        lightSchedule = new LightSchedule(hourOn, hourOff, minOn, minOff);
        schedule.add(lightSchedule);

        int[] hrOn = new int[schedule.size()];
        int[] minOn = new int[schedule.size()];
        int[] hrOff = new int[schedule.size()];
        int[] minOff = new int[schedule.size()];

        for(int van = 0; van<schedule.size();van++){
            hrOn[van] = schedule.get(van).getOnHour();
            minOn[van] = schedule.get(van).getOnMin();
            hrOff[van] = schedule.get(van).getOffHour();
            minOff[van] = schedule.get(van).getOffMin();
        }

        // int[] hrOn, int[]minOn, int[] hrOff, int[] minOff, bool auto, bool manual
        FishyClient.setLighting(TankListActivity.tankList.get(index).getId(), hrOn, minOn, hrOff, minOff,
                autoReg, LightManagementActivity.manual.isChecked());


        //TankListActivity.tankList.get(index).getPiSeasXmlHandler().setLight(schedule, false, false);

//        FishyClient.sendMobileXmlData(TankListActivity.tankList.get(index).getId(),
//                getFilesDir().getAbsolutePath().toString());

        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }
}

