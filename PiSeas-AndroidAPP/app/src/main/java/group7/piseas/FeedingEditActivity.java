package group7.piseas;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import group7.piseas.Objects.FeedSchedule;
import piseas.network.FishyClient;

/**
 * Created by mmbab on 12/1/2016.
 *
 */

public class FeedingEditActivity extends AppCompatActivity {
    private ArrayList<FeedSchedule> schedule;
    private FeedSchedule curSchedule;
    //private FishyClient client;
    private final String tankID = "Matt";
    private final int MAX = 2;
    private final String divider = "<br/>";

    private NumberPicker pickerHr, pickerMin;
    private TextView textMon, textTues, textWed, textThurs, textFri, textSat, textSun, title;
    private boolean day[];
    private String[] days;
    private int hour, min;
    private String lightVal;
    private String light = "Lights";
    private int index;
    private boolean autoStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding_edit_schedule);

        index = getIntent().getIntExtra("id", -1);
        autoStatus = getIntent().getBooleanExtra("auto", false);
        textMon = (TextView) findViewById(R.id.textMon);
        textTues = (TextView) findViewById(R.id.textTue);
        textWed = (TextView) findViewById(R.id.textWed);
        textThurs = (TextView) findViewById(R.id.textThur);
        textFri = (TextView) findViewById(R.id.textFri);
        textSat = (TextView) findViewById(R.id.textSat);
        textSun = (TextView) findViewById(R.id.textSun);
        title = (TextView) findViewById(R.id.title);
        title.setText("Temperature Settings: " + TankListActivity.tankList.get(index).getName());

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

        schedule = getData();
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

    public ArrayList<FeedSchedule> getData(){
        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        return TankListActivity.tankList.get(index).getPiSeasXmlHandler().getFeedSchedules();
    }

    private boolean checkMax(){
        curSchedule = new FeedSchedule(hour, min);
        int hourMin = (hour-MAX)%24;
        int hourMax = (hour+MAX)%24;
        int feedsPerDay[] = new int[7];
        int time = hour * 100 + min;
        int timeMin = time - (MAX * 100);
        int timeMax = time + (MAX * 100);

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
                curSchedule.setWeek(i, true);
                for(FeedSchedule feed : schedule){
                    if(feed.getWeek(i) && timeMin <= feed.getTimeCompare() && timeMax >= feed.getTimeCompare()){
                        Toast.makeText(this, "Feeding times must be at least 2 hours apart!", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }
        }
        boolean run = false;
        for (int daysSelected = 0; daysSelected<7; daysSelected++){
            if(curSchedule.getWeek(daysSelected)){
                run = true;
                break;
            }
        }
        if (run)
            schedule.add(curSchedule);
        else {
            Toast.makeText(this, "Must select at least one day!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void feedSave(View view){
        min = pickerMin.getValue();
        hour = pickerHr.getValue();

        if(checkMax()){
            TankListActivity.tankList.get(index).getPiSeasXmlHandler().setFeed(schedule,false,autoStatus);
            boolean[][] weekArray = new boolean[schedule.size()][7];
            int[] hour = new int[schedule.size()];
            int[] min = new int[schedule.size()];
            for (int i =0; i< schedule.size(); i++){
                FeedSchedule temp = schedule.get(i);
                for (int j = 0; j< 7; j++)
                    weekArray[i][j] = temp.getWeek(j);
                hour[i] = temp.getHour();
                min[i] = temp.getMin();
            }
            FishyClient.setFeeding(TankListActivity.tankList.get(index).getId(), weekArray,hour, min, autoStatus, false);
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
        //FishyClient.writeToServerData(tankID, cleaningLady);
        Toast.makeText(this, "Cleaning lady is done!", Toast.LENGTH_LONG).show();
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
