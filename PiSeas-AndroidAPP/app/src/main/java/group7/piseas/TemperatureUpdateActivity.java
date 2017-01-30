package group7.piseas;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.HashMap;

import group7.piseas.Server.FishyClient;

/**
 * Created by Mike on 11/27/2016.
 */

public class TemperatureUpdateActivity extends AppCompatActivity {
    private SeekBar minSeekBar;
    private SeekBar maxSeekBar;
    private TextView minTempTV;
    private TextView maxTempTV;
    public static final int MIN_TEMP = 20;
    private static int minTemp;
    private static int maxTemp;
    private String minimumTemp;
    private String maximumTemp;
    private static String tankID = "Mike";
    private TextView minTempTable;
    private TextView maxTempTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_update);

        minTempTable = (TextView) findViewById(R.id.minTempValTV);
        maxTempTable = (TextView) findViewById(R.id.maxTempValTV);

        seekBars();
        fillTable();
    }

    public void seekBars(){
        minSeekBar = (SeekBar)findViewById(R.id.minTempSB);
        maxSeekBar = (SeekBar)findViewById(R.id.maxTempSB);
        minTempTV = (TextView)findViewById(R.id.minTempInputTV);
        maxTempTV = (TextView)findViewById(R.id.maxTempInputTV);

        minTemp = minSeekBar.getProgress() + MIN_TEMP;
        maxTemp = maxSeekBar.getMax() + MIN_TEMP;

        maxSeekBar.setProgress(maxTemp - MIN_TEMP);

        minTempTV.setText(minTemp + "°C");
        maxTempTV.setText(maxTemp + "°C");

        minSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                        if((progress + MIN_TEMP) <= maxTemp){
                            minTemp = progress + MIN_TEMP;
                        }
                        else{
                            minTemp = maxTemp;
                            minSeekBar.setProgress(minTemp - MIN_TEMP);
                        }
                        minTempTV.setText(minTemp + "°C");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar){ }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar){ }
                }
        );

        maxSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                        if((progress + MIN_TEMP) >= minTemp){
                            maxTemp = progress + MIN_TEMP;
                        }
                        else{
                            maxTemp = minTemp;
                            maxSeekBar.setProgress(maxTemp - MIN_TEMP);
                        }
                        maxTempTV.setText(maxTemp + "°C");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar){ }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar){ }
                }
        );

    }

    public void onSaveTemperatureRangeClick(View view){
        AlertDialog aD = new AlertDialog.Builder(this).create();
        aD.setTitle("Save this temperature range?\n" + "Min : " + minTemp + "°C Max : " + maxTemp + "°C");
        aD.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                TextView minTempTable = (TextView) findViewById(R.id.minTempValTV);
                TextView maxTempTable = (TextView) findViewById(R.id.maxTempValTV);
                SharedPreferences.Editor sharedPrefEdit = getSharedPreferences("piseas",
                        MODE_PRIVATE).edit();

                HashMap<String, String> dataList = new HashMap<String, String>();
                dataList.put("Min Temp", String.valueOf(minTemp) + "°C");
                dataList.put("Max Temp", String.valueOf(maxTemp) + "°C");
                FishyClient.writeToServerData(tankID, dataList);

                sharedPrefEdit.putString("Min Temp", String.valueOf(minTemp) + "°C");
                sharedPrefEdit.putString("Max Temp", String.valueOf(maxTemp) + "°C");
                sharedPrefEdit.apply();

                minTempTable.setText(String.valueOf(minTemp) + "°C");
                maxTempTable.setText(String.valueOf(maxTemp) + "°C");

                dialogInterface.dismiss();
                finish();
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

    public void fillTable(){
        SharedPreferences sharedPref = getSharedPreferences("piseas", MODE_PRIVATE);

        minimumTemp = sharedPref.getString("Min Temp", "").toString();
        maximumTemp = sharedPref.getString("Max Temp", "").toString();

        minTempTable.setText(minimumTemp);
        maxTempTable.setText(maximumTemp);
    }

}