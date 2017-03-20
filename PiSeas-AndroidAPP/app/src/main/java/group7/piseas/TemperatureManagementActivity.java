package group7.piseas;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import group7.piseas.Helpers.TankTimer;
import piseas.network.FishyClient;

public class TemperatureManagementActivity extends AppCompatActivity {
    private String minTemp;
    private String maxTemp;
    private TextView minTempTable;
    private TextView maxTempTable;
    Switch auto;
    int index;
    private TextView curTempTV;
    private long UPDATE_VALUE_DELAY = 10000;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.post(new TankTimer(getApplicationContext(), curTempTV, null));
            handler.postDelayed(runnable, UPDATE_VALUE_DELAY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_management);
        index = getIntent().getIntExtra("id", -1);

        minTempTable = (TextView) findViewById(R.id.minTempValTV);
        maxTempTable = (TextView) findViewById(R.id.maxTempValTV);
        auto = (Switch) findViewById(R.id.enableTempRegSW);
        curTempTV = (TextView)findViewById(R.id.curTempTV);

        handler.postDelayed(runnable, UPDATE_VALUE_DELAY);

        fillTable();
    }

    public void onUpdateTempClick(View view){
        Intent i = new Intent(this, TemperatureUpdateActivity.class);
        i.putExtra("id", index);
        startActivity(i);
    }
    public void onRemoveTemperatureRangeClick(View view){
        if( TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsMinTemp()!= 0){
            AlertDialog aD = new AlertDialog.Builder(this).create();
            aD.setTitle("Remove temperature range?\n" + "Min : " + minTemp + " Max : " + maxTemp);
            aD.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Switch s = (Switch) findViewById(R.id.enableTempRegSW);

                    if(s.isChecked())
                        s.setChecked(false);

                    TankListActivity.tankList.get(index).getPiSeasXmlHandler().setMaxTemp(0);
                    TankListActivity.tankList.get(index).getPiSeasXmlHandler().setMinTemp(0);
                    TankListActivity.tankList.get(index).getPiSeasXmlHandler().setAutoTemp(false);

                    minTempTable.setText("0.0");
                    maxTempTable.setText("0.0");

                    dialogInterface.dismiss();
                    update();
                }
            });
            aD.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            aD.show();
            validateAuto();
        }
        else{
            Toast.makeText( this, "There is no temperature range to remove.", Toast.LENGTH_LONG ).show();
        }
    }
    public void fillTable(){

        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        minTemp =  String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsMinTemp());
        maxTemp =  String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsMaxTemp());

        //auto.setChecked(TankListActivity.tankList.get(index).getPiSeasXmlHandler().);
        //TODO: get auto temperature function missing from parser

        minTempTable.setText(minTemp);
        maxTempTable.setText(maxTemp);
        curTempTV.setText(String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSensorCurrentTemp()));

        validateAuto();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        update();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillTable();
        validateAuto();
        handler.postDelayed(runnable, UPDATE_VALUE_DELAY);

    }

    public void validateAuto(){
        //validation for automation
        if(minTempTable.getText() == "0.0" && maxTempTable.getText() == "0.0" ){
            auto.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Can not enable, set range first",
                            Toast.LENGTH_LONG).show();
                    auto.setChecked(false);
                }
            });
        }
        else {
            auto.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {

                }
            });
            auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton manual, boolean isChecked) {
                    if (isChecked){
                        TankListActivity.tankList.get(index).getPiSeasXmlHandler().setAutoTemp(true);
                    }
                    else {
                        TankListActivity.tankList.get(index).getPiSeasXmlHandler().setAutoTemp(false);
                    }
                }
            });

        }
    }

    public void update(){
        FishyClient.sendMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
    }
}
