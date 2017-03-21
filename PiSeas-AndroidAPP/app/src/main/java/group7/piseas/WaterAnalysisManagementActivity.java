package group7.piseas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import piseas.network.FishyClient;
// TODO: fix spinner
public class WaterAnalysisManagementActivity extends AppCompatActivity {
    NumberPicker lowPH;
    NumberPicker highPH;
    NumberPicker lowCon;
    NumberPicker highCon;
    Switch autoPH;
    Switch autoCon;
    TextView pHValue;
    TextView conValue;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_analysis_management);
        TextView tv = (TextView) findViewById(R.id.tankName);
        tv.setText("Tank: " + TankListActivity.tankList.get(index).getName());
        index = getIntent().getIntExtra("id", -1);

        lowPH = (NumberPicker) findViewById(R.id.pHMinInput);
        highPH = (NumberPicker) findViewById(R.id.pHMaxInput);
        lowCon = (NumberPicker) findViewById(R.id.CMinInput);
        highCon = (NumberPicker) findViewById(R.id.CMaxInput);
        autoCon = (Switch) findViewById(R.id.enableCCheck);
        autoPH = (Switch) findViewById(R.id.enablePH);
        pHValue =  (TextView) findViewById(R.id.pHDisplay);
        conValue =  (TextView) findViewById(R.id.conDisplay);

        lowPH.setMinValue(0);
        lowPH.setMaxValue(14);
        lowPH.setWrapSelectorWheel(false);
        lowPH.setValue(0);

        highPH.setMinValue(0);
        highPH.setMaxValue(14);
        highPH.setWrapSelectorWheel(false);
        highPH.setValue(0);

        lowCon.setMinValue(0);
        lowCon.setMaxValue(14);
        lowCon.setWrapSelectorWheel(false);
        lowCon.setValue(0);

        highCon.setMinValue(0);
        highCon.setMaxValue(14);
        highCon.setWrapSelectorWheel(false);
        highCon.setValue(0);

        populatePage();
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
        else
            autoCon.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                }
            });

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
        else
            autoPH.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                }
            });
    }

    public void save(View view) {
        if (lowCon.getValue() >= highCon.getValue() || lowPH.getValue() >= highPH.getValue()) {
            Toast.makeText(getBaseContext(), "Can not save, please double check values",
                    Toast.LENGTH_LONG).show();
        } else {//add server call
            validateAuto();
            TankListActivity.tankList.get(index).getPiSeasXmlHandler().setAutoPH(autoPH.isChecked());
            TankListActivity.tankList.get(index).getPiSeasXmlHandler().setAutoConductivity(autoCon.isChecked());
            TankListActivity.tankList.get(index).getPiSeasXmlHandler().setPHMax(highPH.getValue());
            TankListActivity.tankList.get(index).getPiSeasXmlHandler().setPHMin(lowPH.getValue());
            TankListActivity.tankList.get(index).getPiSeasXmlHandler().setCMax(highCon.getValue());
            TankListActivity.tankList.get(index).getPiSeasXmlHandler().setCMin(lowCon.getValue());
            FishyClient.sendMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
            finish();
        }
    }

    public void populatePage(){
        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        lowPH.setValue((int)TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsPHMin());
        highPH.setValue((int)TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsPHMax());
        lowCon.setValue((int)TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsCMin());
        highCon.setValue((int)TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsCMax());
        autoPH.setChecked(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsPHAuto());
        autoCon.setChecked(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsCAuto());
        //TODO: change this so it looks nicer, not append but setText later
        pHValue.append(String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSensorPH()));
        conValue.append(String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSensorConductivity()));
    }
}
