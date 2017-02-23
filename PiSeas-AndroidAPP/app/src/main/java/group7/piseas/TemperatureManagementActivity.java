package group7.piseas;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import group7.piseas.Helpers.TankTimer;
import group7.piseas.Server.FishyClient;

public class TemperatureManagementActivity extends AppCompatActivity {
    private String minTemp;
    private String maxTemp;
    private TextView minTempTable;
    private TextView maxTempTable;
    Switch auto;

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

        minTempTable = (TextView) findViewById(R.id.minTempValTV);
        maxTempTable = (TextView) findViewById(R.id.maxTempValTV);
        auto = (Switch) findViewById(R.id.enableTempRegSW);

        curTempTV = (TextView)findViewById(R.id.curTempTV);
        handler.postDelayed(runnable, UPDATE_VALUE_DELAY);
        fillTable();
    }

    public void onUpdateTempClick(View view){
        Intent i = new Intent(this, TemperatureUpdateActivity.class);
        startActivity(i);
    }
    public void onRemoveTemperatureRangeClick(View view){
        SharedPreferences sharedPref = getSharedPreferences("piseas", MODE_PRIVATE);
        String min = sharedPref.getString("Min Temp", "").toString();

        if(!min.equals("")){
            AlertDialog aD = new AlertDialog.Builder(this).create();
            aD.setTitle("Save this temperature range?\n" + "Min : " + minTemp + " Max : " + maxTemp);
            aD.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Switch s = (Switch) findViewById(R.id.enableTempRegSW);

                    if(s.isChecked())
                        s.setChecked(false);

                    String tankID = "Mike";
                    SharedPreferences.Editor sharedPrefEdit = getSharedPreferences("piseas",
                            MODE_PRIVATE).edit();

                    HashMap<String, String> dataList = new HashMap<String, String>();
                    dataList.put("Min Temp", "");
                    dataList.put("Max Temp", "");
                    FishyClient.writeToServerData(tankID, dataList);

                    minTempTable.setText("");
                    maxTempTable.setText("");

                    sharedPrefEdit.putString("Min Temp", "");
                    sharedPrefEdit.putString("Max Temp", "");
                    sharedPrefEdit.apply();

                    dialogInterface.dismiss();
                }
            });
            aD.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            aD.show();
        }
        else{
            Toast.makeText( this, "There is no temperature range to remove.", Toast.LENGTH_LONG ).show();
        }
    }
    public void fillTable(){
        SharedPreferences sharedPref = getSharedPreferences("piseas", MODE_PRIVATE);

        minTemp = sharedPref.getString("Min Temp", "").toString();
        maxTemp = sharedPref.getString("Max Temp", "").toString();

        minTempTable.setText(minTemp);
        maxTempTable.setText(maxTemp);

        validateAuto();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
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
        if(minTempTable.getText() == "" || maxTempTable.getText() == "" ){
            auto.setOnClickListener(new CompoundButton.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "Can not enable, set range first",
                            Toast.LENGTH_LONG).show();
                    auto.setChecked(false);
                }
            });
        }
        else auto.setOnClickListener(new CompoundButton.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
    }
}
