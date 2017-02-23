package group7.piseas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import group7.piseas.Helpers.XmlPullParserHandler;

public class WaterLevelManagementActivity extends AppCompatActivity {
    private Switch drain;
    private Switch fill;
    private Switch auto;
    private XmlPullParserHandler parser = new XmlPullParserHandler(this, "1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_level_management);
        int index = getIntent().getIntExtra("id", -1);
        TextView tv = (TextView) findViewById(R.id.tankName);
        tv.setText("Tank: " + TankListActivity.tankList.get(index).getName());

        drain = (Switch) findViewById(R.id.enableDrain);
        fill = (Switch) findViewById(R.id.enableFill);
        auto = (Switch) findViewById(R.id.enableAuto);

        drain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton drain, boolean isChecked) {
                if (!isChecked)
                    fill.setClickable(true);
                else{
                    fill.setChecked(false);
                    auto.setChecked(false);
                }
            }
        });

        fill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton fill, boolean isChecked) {
                if (!isChecked)
                    drain.setClickable(true);
                else{
                    drain.setChecked(false);
                    auto.setChecked(false);
                }
            }
        });

        auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton auto, boolean isChecked) {
                if (!isChecked){
                    drain.setClickable(true);
                    fill.setClickable(true);
                }
                else{
                    fill.setChecked(false);
                    drain.setChecked(false);
                }
            }
        });
    }

    private void loadXmlData() {

        fill.setChecked(parser.getSettingsFill());
        auto.setChecked(parser.getSettingsAutoWaterChange());
        drain.setChecked(parser.getSettingsDrain());
        Log.d("activity; load", "fill" + " : " + parser.getSettingsFill());
    }

    private void saveXmlData() {
        parser.setFill(fill.isChecked());
        parser.setAutoWaterChange(auto.isChecked());
        parser.setDrain(drain.isChecked());

        Log.d("activity; save", "fill" + " : " + fill.isChecked());
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveXmlData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadXmlData();
    }

}
