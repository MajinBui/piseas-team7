package group7.piseas;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import group7.piseas.Objects.FeedSchedule;
import group7.piseas.Server.FishyClient;

/**
 * Created by mmbab on 12/1/2016.
 *
 */

public class FeedingEditActivity extends AppCompatActivity {
    private ArrayList<FeedSchedule> schedule;
    private FeedSchedule curSchedule;
    private FishyClient client;
    private final String tankID = "Matt";
    private final int MAX = 2;
    private final String divider = "<br/>";

    private NumberPicker pickerHr, pickerMin;
    private TextView textMon, textTues, textWed, textThurs, textFri, textSat, textSun;
    private boolean day[];
    private String[] days;
    private int hour, min;
    private String lightVal;
    private String light = "Lights";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding_edit_schedule);

        textMon = (TextView) findViewById(R.id.textMon);
        textTues = (TextView) findViewById(R.id.textTue);
        textWed = (TextView) findViewById(R.id.textWed);
        textThurs = (TextView) findViewById(R.id.textThur);
        textFri = (TextView) findViewById(R.id.textFri);
        textSat = (TextView) findViewById(R.id.textSat);
        textSun = (TextView) findViewById(R.id.textSun);

        schedule = new ArrayList<FeedSchedule>();

        day = new boolean[7];
        Arrays.fill(day, false);

        days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        pickerHr = (NumberPicker) findViewById(R.id.picker_hour);
        pickerMin = (NumberPicker) findViewById(R.id.picker_minute);

        pickerHr.setMinValue(0);
        pickerHr.setMaxValue(23);
        pickerMin.setMinValue(0);
        pickerMin.setMaxValue(59);

        pickerHr.setWrapSelectorWheel(true);
        pickerMin.setWrapSelectorWheel(true);

        pickerHr.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                hour = newVal;
            }
        });
        pickerMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                min = newVal;
            }
    });

        getData();
    }

    public void dayClick(View view) {
        switch(view.getId()){
            case R.id.textSun:
                day[0] = changeBackground(day[0], textSun);
                break;
            case R.id.textMon:
                day[1] = changeBackground(day[1], textMon);
                break;
            case R.id.textTue:
                day[2] = changeBackground(day[2], textTues);
                break;
            case R.id.textWed:
                day[3] = changeBackground(day[3], textWed);
                break;
            case R.id.textThur:
                day[4] = changeBackground(day[4], textThurs);
                break;
            case R.id.textFri:
                day[5] = changeBackground(day[5], textFri);
                break;
            default:
                day[6] = changeBackground(day[6], textSat);
        }
    }

    private boolean changeBackground(boolean today, TextView txt){
        today = !today;
        if(today)
            txt.setBackground(getResources().getDrawable(R.drawable.backfull));
        else
            txt.setBackground(getResources().getDrawable(R.drawable.backempty));
        return today;
    }

    public void getData(){
        HashMap<String, String> retrieveList = FishyClient.retrieveServerData(tankID);

            for(int i=0; i<7; i++){
                if(!retrieveList.get(days[i]).equals("-")){
                    String[] separate = retrieveList.get(days[i]).split(divider);

                    for (String aSeparate : separate) {
                        boolean found = false;
                        for (FeedSchedule feed : schedule) {
                            if (feed.getTime().equals(aSeparate)) {
                                feed.setWeek(i);
                                found = true;
                            }
                        }
                        if(!found){
                            String[] timeSplit = aSeparate.split(":");
                            int hr = Integer.parseInt(timeSplit[0]);
                            int min = Integer.parseInt(timeSplit[1]);
                            FeedSchedule dayFeed = new FeedSchedule(hr, min);
                            dayFeed.setWeek(i);
                            schedule.add(dayFeed);
                        }
                    }
                }
            }
        lightVal = retrieveList.get(light);
    }

    private boolean checkMax(){
        curSchedule = new FeedSchedule(hour, min);
        int hourMin = (hour-MAX)%24;
        int hourMax = (hour+MAX)%24;
        int feedsPerDay[] = new int[7];

        for(FeedSchedule feed : schedule){
            for(int i=0; i<7; i++) {
                if (feed.getWeek(i))
                    feedsPerDay[i]++;
            }
        }

        for(int i=0; i<7; i++){
            if(day[i]){
                if(feedsPerDay[i]+1 > 2){
                    Toast.makeText(this, "Can only have 2 feeds per day!", Toast.LENGTH_LONG).show();
                    return false;
                }
                curSchedule.setWeek(i);
                for(FeedSchedule feed : schedule){
                    if(hourMax > hourMin) {
                        // check if hour is 2hr within another feed, current time is near midnight
                        if (feed.getWeek(i) && (feed.getHour() >= hourMin && feed.getHour() <= hourMax)) {
                            Toast.makeText(this, "Feeding times must be at least 2 hours apart!", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    // check if hour is 2hr within another feed
                    }
                    else {
                        if (feed.getWeek(i) && feed.getHour() >= hourMin || feed.getHour() <= hourMax) {
                            Toast.makeText(this, "Feeding times must be at least 2 hours apart!", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }
                }
            }
        }
        schedule.add(curSchedule);
        return true;
    }

    public void feedSave(View view){
        HashMap<String, String> dataList = new HashMap<String, String>();
        min = pickerMin.getValue();
        hour = pickerHr.getValue();

        if(checkMax()){
            String padding = "00";

            dataList.put("tankId", tankID);
            for(int i = 0; i < 7; i++){
                String temp = "";
                for(FeedSchedule feed : schedule){
                    if(feed.getWeek(i))
                        temp += feed.getTime() + divider;
                }
                if(temp.equals(""))
                    temp = "-";
                dataList.put(days[i], temp);
            }

            dataList.put(light, lightVal);

            FishyClient.writeToServerData(tankID, dataList);

            finish();
        }
    }

    public void clear(View view){
        HashMap<String, String> cleaningLady = new HashMap<String, String>();
        cleaningLady.put("tankId", tankID);
        for(int i = 0; i < 7; i++){
            cleaningLady.put(days[i], "-");
        }
        //cleaningLady.put(light, "-");
        cleaningLady.put(light, lightVal);
        FishyClient.writeToServerData(tankID, cleaningLady);
        Toast.makeText(this, "Cleaning lady is done!", Toast.LENGTH_LONG).show();
    }
}
