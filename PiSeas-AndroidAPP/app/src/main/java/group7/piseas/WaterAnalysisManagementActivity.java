package group7.piseas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import cn.carbswang.android.numberpickerview.library.NumberPickerView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import group7.piseas.Objects.PH;
import group7.piseas.Objects.Tank;
import group7.piseas.Objects.WaterConductivity;
import piseas.network.FishyClient;

public class WaterAnalysisManagementActivity extends AppCompatActivity {
    NumberPickerView lowPH;
    NumberPickerView highPH;
    NumberPickerView lowCon;
    NumberPickerView highCon;
    Switch autoPH;
    Switch autoCon;
    TextView pHValue;
    TextView conValue;
    TextView title;

    private Tank tank;
    private PH pH;
    private WaterConductivity wc;

    int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_analysis_management);
        TextView tv = (TextView) findViewById(R.id.title);
        tv.setText("Tank: " + TankListActivity.tankList.get(index).getName());
        index = getIntent().getIntExtra("id", -1);

        lowPH = (NumberPickerView) findViewById(R.id.pHMinInput);
        highPH = (NumberPickerView) findViewById(R.id.pHMaxInput);
        lowCon = (NumberPickerView) findViewById(R.id.CMinInput);
        highCon = (NumberPickerView) findViewById(R.id.CMaxInput);
        autoCon = (Switch) findViewById(R.id.enableCCheck);
        autoPH = (Switch) findViewById(R.id.enablePH);
        pHValue =  (TextView) findViewById(R.id.pHDisplay);
        conValue =  (TextView) findViewById(R.id.conDisplay);

        title = (TextView) findViewById(R.id.title);
        title.setText("Water Analysis: " + TankListActivity.tankList.get(index).getName());

        // generate ph values into string list
        String[] pHValues = new String[15];
        for (int i = 0; i < 15; i++) {
            pHValues[i] = Integer.toString(i);
        }

        // generate water conductivity values into string list
        String[] wcValues = new String[21];
        for (int i = 0; i < 21; i++) {
            wcValues[i] = Integer.toString(i * 100);
        }
        lowPH.setDisplayedValues(pHValues);
        lowPH.setMinValue(0);
        lowPH.setMaxValue(14);
        lowPH.setWrapSelectorWheel(false);
        lowPH.setValue(0);

        highPH.setDisplayedValues(pHValues);
        highPH.setMinValue(0);
        highPH.setMaxValue(14);
        highPH.setWrapSelectorWheel(false);
        highPH.setValue(0);

        lowCon.setDisplayedValues(wcValues);
        lowCon.setMinValue(0);
        lowCon.setMaxValue(20);
        lowCon.setWrapSelectorWheel(false);
        lowCon.setValue(0);

        highCon.setDisplayedValues(wcValues);
        highCon.setMinValue(0);
        highCon.setMaxValue(20);
        highCon.setWrapSelectorWheel(false);
        highCon.setValue(0);

        tank = TankListActivity.tankList.get(getIntent().getIntExtra("id", -1));
        pH = tank.getpH();
        wc = tank.getWc();

        autoCon.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        autoCon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton manual, boolean isChecked) {
                TankListActivity.tankList.get(index).getPiSeasXmlHandler().setAutoConductivity(isChecked);
                tank.getWc().setAuto(isChecked);
                FishyClient.setAutoWC(TankListActivity.tankList.get(index).getId(), isChecked);
            }
        });

        autoPH.setOnClickListener(new CompoundButton.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        autoPH.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton manual, boolean isChecked) {
                TankListActivity.tankList.get(index).getPiSeasXmlHandler().setAutoPH(isChecked);
                FishyClient.setAutoPh(TankListActivity.tankList.get(index).getId(), isChecked);
                tank.getpH().setAuto(isChecked);
            }
        });
    }

    public void validateAuto(){
        //validation for automation
        if(lowCon.getValue() > highCon.getValue()){
            autoCon.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Can not enable, configure settings first",
                            Toast.LENGTH_LONG).show();
                    autoCon.setChecked(false);
                }
            });
        }

        if(lowPH.getValue() > highPH.getValue()){
            autoPH.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Can not enable, configure settings first",
                            Toast.LENGTH_LONG).show();
                    autoPH.setChecked(false);
                }
            });
        }

    }

    public void save(View view) {
        if (lowCon.getValue() >= highCon.getValue() || lowPH.getValue() >= highPH.getValue()) {
            Toast.makeText(getBaseContext(), "Can not save, please double check values",
                    Toast.LENGTH_LONG).show();
        } else {
            validateAuto();
            pH.setAuto(autoPH.isChecked());
            pH.setpHMax(highPH.getValue());
            pH.setpHMin(lowPH.getValue());

            wc.setAuto(autoCon.isChecked());
            wc.setConMax(highCon.getValue());
            wc.setConMin(lowCon.getValue());

            tank.updateWaterAnalysis();
            finish();
        }
    }

    public void populatePage(){
        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        lowPH.setValue((int)pH.getpHMin());
        highPH.setValue((int) pH.getpHMax());
        autoPH.setChecked(pH.isAuto());
        lowCon.setValue((int)wc.getConMin());
        highCon.setValue((int)wc.getConMax());
        autoCon.setChecked(wc.isAuto());
        pHValue.setText(String.valueOf(pH.getValue()));
        conValue.setText(String.valueOf(wc.getValue()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        populatePage();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
