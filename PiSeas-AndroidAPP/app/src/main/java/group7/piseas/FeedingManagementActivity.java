package group7.piseas;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import group7.piseas.Adapters.FeedingAdapter;
import group7.piseas.Objects.FeedSchedule;
import group7.piseas.Objects.Tank;
import piseas.network.FishyClient;

public class FeedingManagementActivity extends AppCompatActivity {
    public static  ArrayList<FeedSchedule> feeds;
    private ListView listView;
    private final String tankID = "Matt";
    public static Switch autoFeed;
    private int index;
    private Tank tank;
    private final int MAX = 2;
    static FeedingAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding_management);
        autoFeed = (Switch) findViewById(R.id.autoLight);
        index = getIntent().getIntExtra("id", -1);
        tank = TankListActivity.tankList.get(index);

        populateList();
        validateAuto();
    }

    public void onManualFeed(View view){
        Date date = new Date();
        int hour = date.getHours();
        int min = date.getMinutes();
        FeedSchedule curSchedule = new FeedSchedule(hour, min);
        int feedsPerDay[] = new int[7];
        int time = hour * 100 + min;
        int timeMin = time - (MAX * 100);
        int timeMax = time + (MAX * 100);

        for(FeedSchedule feed : feeds){
            for(int i=0; i<7; i++) {
                if (feed.getWeek(i))
                    feedsPerDay[i]++;
            }
        }

        boolean day[] = new boolean[7];
        Arrays.fill(day, false);

        int dateDay = date.getDay();
        if(feedsPerDay[dateDay]+1 > 2){
            Toast.makeText(this, "Can only have 2 feeds per day!", Toast.LENGTH_LONG).show();
        }
        boolean feedNow = true;
        for(FeedSchedule feed : feeds){
            if(feed.getWeek(dateDay) && timeMin <= feed.getTimeCompare() && timeMax >= feed.getTimeCompare()){
                Toast.makeText(this, "Must wait 2 hours from last feed time.", Toast.LENGTH_LONG).show();
                feedNow = false;
            }
        }

        if(feedNow && lastFeedCheck(hour, min)){
            Toast.makeText(this, "Fish are fed manually", Toast.LENGTH_LONG).show();
            FishyClient.setManualFeed(tank.getId(),true);
        }
    }
    public boolean lastFeedCheck(int nowHour, int nowMin){
        FishyClient.retrieveSensorData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        int hour = tank.getPiSeasXmlHandler().getSensorFeedHr();
        int min = tank.getPiSeasXmlHandler().getSensorFeedMin();
        Date now = new Date(0, 0, 0, nowHour, nowMin);
        Date last = new Date(0, 0, 0, hour, min);
        long diff = last.getTime() - now.getTime();
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 1000);
        int totalNumber = TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSensorTotalFeeds();
        if ((diffHours + diffMinutes)>120){
            if(totalNumber < 2)
                return true;
        }
        Toast.makeText(this, "Cannot feed manually feed right now, fed too many times.", Toast.LENGTH_LONG).show();
        return false;
    }

    public void addSchedule(View view){
        Intent i = new Intent(this, FeedingEditActivity.class);
        i.putExtra("id", index);
        i.putExtra("autoStatus", autoFeed.isChecked());
        startActivity(i);
    }

    @Override
    protected void onResume() {
        Log.i("ONRESUME", "");
        super.onResume();
        populateList();
        validateAuto();
        adapter.notifyDataSetChanged();
    }

    private void populateList(){
        feeds =  getData();

        listView = (ListView) findViewById(R.id.list_schedule);
        adapter = new FeedingAdapter(this, R.layout.days_list_items, feeds, index);

        listView.setAdapter(adapter);
        validateAuto();
    }

    public ArrayList<FeedSchedule> getData(){
        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        return TankListActivity.tankList.get(index).getPiSeasXmlHandler().getFeedSchedules();

    }

    public void validateAuto(){
        //validation for automation
        autoFeed.setChecked( TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsAutoFeed());
        if(feeds == null ||feeds.isEmpty()){
            autoFeed.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Can not enable, create schedule first",
                            Toast.LENGTH_LONG).show();
                    autoFeed.setChecked(false);
                }
            });
            autoFeed.setChecked(false);
        }
        else{
            autoFeed.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                }
            });
            autoFeed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton manual, boolean isChecked) {
                    if (isChecked){
                        FishyClient.setAutoFeed(TankListActivity.tankList.get(index).getId(), true);
                    }
                    else {
                        FishyClient.setAutoFeed(TankListActivity.tankList.get(index).getId(), false);
                    }
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        populateList();
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
