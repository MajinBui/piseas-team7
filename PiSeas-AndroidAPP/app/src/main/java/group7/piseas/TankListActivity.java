package group7.piseas;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import group7.piseas.Adapters.TankAdapter;
import group7.piseas.Objects.Tank;
import group7.piseas.services.PiseasReceiver;
import group7.piseas.services.PiseasService;

public class TankListActivity extends AppCompatActivity {

    public static List<Tank> tankList;
    private String Version = "1.0.2";
    ListView listView;
    static TankAdapter adapter;
    ImageButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("tanklist", "onCreate");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tank_list);
        tankList = new ArrayList<Tank>();//get from shared preferences

        listView = (ListView) findViewById(R.id.list);
        adapter = new TankAdapter(this, R.layout.tank_info, tankList);
        addButton = (ImageButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TankList", "add clicked");
                startActivity(new Intent(getBaseContext(), AddTankActivity.class));
            }
        });
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TankMainPageActivity.class);
                intent.putExtra("id", i);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
        adapter.notifyDataSetChanged();
    }

    public static void update(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        SharedPreferences settings = getSharedPreferences("piseas", MODE_PRIVATE);
        boolean isChecked = settings.getBoolean("allowNotifications", false);
        MenuItem item = menu.findItem(R.id.allowNotifications);
        item.setChecked(isChecked);
        if (isChecked) {
            scheduleNotifications();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.allowNotifications:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    Log.i("LISTACTIVITY", "allow notifications");
                    scheduleNotifications();
                }
                else {
                    Log.i("LISTACTIVITY", "block notifications");
                    cancelNotifications();
                }
                SharedPreferences settings = getSharedPreferences("piseas", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("allowNotifications", item.isChecked());
                editor.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        Log.i("TANKLIST", "onStop");
        SharedPreferences.Editor editor = getSharedPreferences("piseas", Context.MODE_PRIVATE).edit();
        //editor.clear();
        editor.putInt("listSize", tankList.size());
        Log.i("TANKLIST", "size: " + tankList.size());
        for (int i = 0; i<tankList.size();i++){
            editor.putString("code"+i, tankList.get(i).getId());
            Log.i("TANKLIST", "code: " + "code"+i + " - " + tankList.get(i).getId());
        }
        editor.commit();
        super.onStop();
    }

    private void load(){

        Log.i("TANKLIST", "LOAD");
        SharedPreferences sharedPref = getSharedPreferences("piseas", MODE_PRIVATE);
        // clear old shared preferences if old version
        try {
            String tankCode = sharedPref.getString("code"+0, "0");
        } catch (ClassCastException e) {
            SharedPreferences prefs = getSharedPreferences("piseas", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
        }
        if (!sharedPref.getString("version", "").equals(Version)) {
            SharedPreferences prefs = getSharedPreferences("piseas", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.putString("version", Version);
            editor.commit();
        }
        int size = sharedPref.getInt("listSize", 0);
        Log.i("TANKLIST", "LOAD size " + size);

        int pw = 0;
        String name = "";
        String desc = "";
        int type = 0;
        int tankSize = 0;

        if (tankList.size() > 0) {
            SharedPreferences.Editor editor = getSharedPreferences("piseas", Context.MODE_PRIVATE).edit();
            //editor.clear();
            editor.putInt("listSize", tankList.size());
            Log.i("TANKLIST", "size: " + tankList.size());
            for (int i = 0; i<tankList.size();i++){
                editor.putString("code"+i, tankList.get(i).getId());
                Log.i("TANKLIST", "code: " + "code"+i + " - " + tankList.get(i).getId());
            }
            editor.commit();
        }

        tankList.clear();
        for (int i=0;i<size; i++){
            String tankCode = sharedPref.getString("code"+i, "0");
            Log.i("TANKLIST", "LOAD Tank Code" + tankCode);
            try {
                Log.i("TANKLIST", "LOAD ADD TANK " + tankCode + ", "+ pw + ", "+ name + ", "+ type + ", "+ size+ ", "+desc);
                tankList.add(new Tank(getApplicationContext(), tankCode));
            } catch (NullPointerException e) {
                Toast.makeText(this, "No internet connection available!", Toast.LENGTH_LONG).show();
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void scheduleNotifications() {
        // Construct an intent that will execute the AlarmReceiver
        Intent startServiceIntent = new Intent(getApplicationContext(), PiseasService.class);
        // Create a PendingIntent to be triggered when the alarm goes off

        PendingIntent pi = PendingIntent.getService(this, 0, startServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
//        alarm.setInexactRepeating(AlarmManager.RTC, firstMillis, 60000, pi);
        alarm.setInexactRepeating(AlarmManager.RTC, firstMillis, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
    }

    public void cancelNotifications() {
        Intent startServiceIntent = new Intent(getApplicationContext(), PiseasService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, startServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pi);
    }
}
