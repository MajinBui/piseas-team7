package group7.piseas;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group7.piseas.Adapters.LightAdapter;
import group7.piseas.Objects.LightSchedule;
//import group7.piseas.Server.FishyClient;
import piseas.network.FishyClient;



public class LightManagementActivity extends AppCompatActivity {
    private final String tankID = "Matt";
    private Switch auto;
    private Switch manual;
    private List<LightSchedule> lights;
    private ListView listView;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_managment); //TODO: change the layout!!!!!!
        index = getIntent().getIntExtra("id", -1);

        auto = (Switch) findViewById(R.id.autoLight);
        manual = (Switch) findViewById(R.id.manLight);

        populateList();
    }

    public void addSchedule(View view) {
        Intent i = new Intent(this, LightEditActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateList();
    }

    private void populateList(){
        lights =  getData();

        listView = (ListView) findViewById(R.id.list_schedule_light);

        LightAdapter adapter = new LightAdapter(this, R.layout.lights_item_list, lights);
        listView.setAdapter(adapter);
        validateAuto();
    }

    private ArrayList<LightSchedule> getData() {
        /*HashMap<String, String> retrieveList = FishyClient.retrieveServerData(tankID);
        String divider = "<br/>";
        String light = "Lights";

        if (!retrieveList.get(light).equals("-")) {
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
                lights.add(dayLight);
                }
            }*/

        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        return TankListActivity.tankList.get(index).getPiSeasXmlHandler().getLightSchedules();
    }
    public void validateAuto(){
        //validation for automation
        auto.setChecked( TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsAutoLight());
        manual.setChecked( TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsManualLight());
        if(lights == null ||lights.isEmpty()){
            auto.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Can not enable, create schedule first",
                            Toast.LENGTH_LONG).show();
                    auto.setChecked(false);
                    //TODO save settings on xml
                }
            });
            auto.setChecked(false);
        }
        else { //TODO fix auto turning on
            auto.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Log.i("LIGHT MANAGEMENT"," on click auto switch");
                }
            });
           auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton manual, boolean isChecked) {
                    if (isChecked)
                        manual.setChecked(false);
                    Log.i("LIGHT MANAGEMENT","man switch off");
                }
            });
        }

        manual.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton manual, boolean isChecked) {
                if (isChecked){
                    auto.setChecked(false);
                    FishyClient.updateManualCommands(TankListActivity.tankList.get(index).getId(),
                            false, true, false, false);
                }
                else {
                    FishyClient.updateManualCommands(TankListActivity.tankList.get(index).getId(),
                            false, false, false, false);
                }
                Log.i("LIGHT MANAGEMENT","man change");
            }
        });

    }
}
