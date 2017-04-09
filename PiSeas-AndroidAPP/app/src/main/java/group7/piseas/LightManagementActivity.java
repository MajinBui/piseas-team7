package group7.piseas;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group7.piseas.Adapters.LightAdapter;
import group7.piseas.Objects.LightSchedule;
//import group7.piseas.Server.FishyClient;
import group7.piseas.Objects.Tank;
import piseas.network.FishyClient;



public class LightManagementActivity extends AppCompatActivity {
    public static Switch autoLight;
    public static Switch manual;
    public static ArrayList<LightSchedule> lights;
    private ListView listView;
    private int index;
    static LightAdapter adapter;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_managment);
        index = getIntent().getIntExtra("id", -1);
        title = (TextView) findViewById(R.id.title);
        title.setText("Feeding Settings: " + TankListActivity.tankList.get(index).getName());

        autoLight = (Switch) findViewById(R.id.autoLight);
        manual = (Switch) findViewById(R.id.manLight);

        autoLight.setChecked(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsAutoLight());
        manual.setChecked(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsManualLight());

        populateList();
    }

    public void addSchedule(View view) {
        Intent i = new Intent(this, LightEditActivity.class);
        i.putExtra("index", index);
        i.putExtra("auto", autoLight.isChecked());

        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
        adapter.notifyDataSetChanged();
    }

    public static void update(){
        adapter.notifyDataSetChanged();
    }

    private void populateList(){
        lights =  getData();

        listView = (ListView) findViewById(R.id.list_schedule_light);

        adapter = new LightAdapter(this, R.layout.lights_item_list, lights, index);
        listView.setAdapter(adapter);
        validateAuto();
    }

    private ArrayList<LightSchedule> getData() {
        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        return TankListActivity.tankList.get(index).getPiSeasXmlHandler().getLightSchedules();
    }

    public void validateAuto(){
        //validation for automation
        autoLight.setChecked( TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsAutoLight());
        manual.setChecked( TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsManualLight());
        if(lights == null || lights.isEmpty()){
            autoLight.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Can not enable, create schedule first", Toast.LENGTH_LONG).show();
                    autoLight.setChecked(false);
                }
            });
            autoLight.setChecked(false);
        }
        else {
            autoLight.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //autoLight.setChecked(true);
                    Log.i("LIGHT MANAGEMENT"," on click auto switch");
                }
            });
            autoLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton manual, boolean isChecked) {
                    if (isChecked) {
                        sendData();
                    }
                    else
                        sendData();
                }
            });
        }

        manual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton manual, boolean isChecked) {
                if (isChecked){
                    autoLight.setChecked(false);
                    FishyClient.updateManualCommands(TankListActivity.tankList.get(index).getId(),
                            false, true, false, false);
                }
                else {
                    FishyClient.updateManualCommands(TankListActivity.tankList.get(index).getId(),
                            false, false, false, false);
                }
            }
        });

    }

    private void sendData(){
        int size = LightManagementActivity.lights.size();
        int[] hrOn = new int[size];
        int[] minOn = new int[size];
        int[] hrOff = new int[size];
        int[] minOff = new int[size];

        for(int van = 0; van<LightManagementActivity.lights.size();van++){
            hrOn[van] = LightManagementActivity.lights.get(van).getOnHour();
            minOn[van] = LightManagementActivity.lights.get(van).getOnMin();
            hrOff[van] = LightManagementActivity.lights.get(van).getOffHour();
            minOff[van] = LightManagementActivity.lights.get(van).getOffHour();
        }

        // int[] hrOn, int[]minOn, int[] hrOff, int[] minOff, bool auto, bool manual
        FishyClient.setLighting(TankListActivity.tankList.get(index).getId(), hrOn, minOn, hrOff, minOff,
                autoLight.isChecked(), manual.isChecked());

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
