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
import java.util.HashMap;
import java.util.List;

import group7.piseas.Adapters.FeedingAdapter;
import group7.piseas.Objects.FeedSchedule;
import piseas.network.FishyClient;

public class FeedingManagementActivity extends AppCompatActivity {
    private List<FeedSchedule> feeds;
    private ListView listView;
    private final String tankID = "Matt";
    Switch autoFeed;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding_management);
        autoFeed = (Switch) findViewById(R.id.autoLight);
        index = getIntent().getIntExtra("id", -1);

        populateList();
        validateAuto();
    }

    public void onManualFeed(View view){  //TODO: add manual validation
        Toast.makeText(this, "Fish are fed manually", Toast.LENGTH_LONG).show();
    }

    public void addSchedule(View view){
        Intent i = new Intent(this, FeedingEditActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        Log.i("ONRESUME", "");
        super.onResume();
        populateList();
        validateAuto();
    }

    private void populateList(){
        feeds =  getData();

        listView = (ListView) findViewById(R.id.list_schedule);
        //TODO: add delete option with context menu

        FeedingAdapter adapter = new FeedingAdapter(this, R.layout.days_list_items, feeds);

        listView.setAdapter(adapter);
        validateAuto();
    }

    public ArrayList<FeedSchedule> getData(){
       /* String[] days = new String[]{"Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"};
        String divider = "<br/>";*/
        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        return TankListActivity.tankList.get(index).getPiSeasXmlHandler().getFeedSchedules();
        /*
        HashMap<String, String> retrieveList = FishyClient.retrieveServerData(tankID);

        for(int i=0; i<7; i++){
            if(!retrieveList.get(days[i]).equals("-")){
                String[] separate = retrieveList.get(days[i]).split(divider);

                for (String aSeparate : separate) {
                    boolean found = false;
                    for (FeedSchedule feed : feeds) {
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
                        feeds.add(dayFeed);
                    }
                }
            }
        }*/
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
                        //TODO : add single change function or update entire settings xml
                    }
                    else {

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
