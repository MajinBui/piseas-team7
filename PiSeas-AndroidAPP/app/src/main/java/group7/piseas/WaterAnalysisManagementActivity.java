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

public class WaterAnalysisManagementActivity extends AppCompatActivity {
    NumberPicker lowPH;
    NumberPicker highPH;
    NumberPicker lowCon;
    NumberPicker highCon;
    Switch autoPH;
    Switch autoCon;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_analysis_management);
        int index = getIntent().getIntExtra("id", -1);
        TextView tv = (TextView) findViewById(R.id.tankName);
        tv.setText("Tank: " + TankListActivity.tankList.get(index).getName());
        index = getIntent().getIntExtra("id", -1);

        lowPH = (NumberPicker) findViewById(R.id.pHMinInput);
        highPH = (NumberPicker) findViewById(R.id.pHMaxInput);
        lowCon = (NumberPicker) findViewById(R.id.CMinInput);
        highCon = (NumberPicker) findViewById(R.id.CMaxInput);
        autoCon = (Switch) findViewById(R.id.enableCCheck);
        autoPH = (Switch) findViewById(R.id.enablePH);

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
            //TODO : write to server
            finish();
        }
    }

    public void populatePage(){
        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());

    }
}
