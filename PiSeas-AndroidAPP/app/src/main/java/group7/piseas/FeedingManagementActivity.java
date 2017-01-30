package group7.piseas;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group7.piseas.Adapters.FeedingAdapter;
import group7.piseas.Objects.FeedSchedule;
import group7.piseas.Server.FishyClient;

public class FeedingManagementActivity extends AppCompatActivity {
    private List<FeedSchedule> feeds;
    private ListView listView;
    private final String tankID = "Matt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding_management);
        populateList();
    }

    public void onManualFeed(View view){
        Toast.makeText(this, "Fish are fed manually", Toast.LENGTH_LONG).show();
    }

    public void addSchedule(View view){
        Intent i = new Intent(this, FeedingEditActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    private void populateList(){
        feeds = new ArrayList<FeedSchedule>();

        getData();

        listView = (ListView) findViewById(R.id.list_schedule);

        FeedingAdapter adapter = new FeedingAdapter(this, R.layout.days_list_items, feeds);

        listView.setAdapter(adapter);

    }

    public void getData(){
        String[] days = new String[]{"Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"};
        String divider = "<br/>";

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
        }
    }
}