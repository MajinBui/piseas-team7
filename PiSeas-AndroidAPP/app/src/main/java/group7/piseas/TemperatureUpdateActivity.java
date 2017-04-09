package group7.piseas;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import piseas.network.FishyClient;

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
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature_update);
        index = getIntent().getIntExtra("id", -1);
        minTempTable = (TextView) findViewById(R.id.minTempValTV);
        maxTempTable = (TextView) findViewById(R.id.maxTempValTV);

        fillTable();
        seekBars();
    }

    public void seekBars(){
        minSeekBar = (SeekBar)findViewById(R.id.minTempSB);
        maxSeekBar = (SeekBar)findViewById(R.id.maxTempSB);
        minTempTV = (TextView)findViewById(R.id.minTempInputTV);
        maxTempTV = (TextView)findViewById(R.id.maxTempInputTV);

        minTemp = minSeekBar.getProgress() + MIN_TEMP;
        maxTemp = maxSeekBar.getMax() + MIN_TEMP;

        minTemp = (int)TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsMinTemp();
        maxTemp = (int)TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsMaxTemp();

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

                TankListActivity.tankList.get(index).getPiSeasXmlHandler().setMaxTemp(maxTemp);
                TankListActivity.tankList.get(index).getPiSeasXmlHandler().setMinTemp(minTemp);

                minTempTable.setText(String.valueOf(minTemp) + "°C");
                maxTempTable.setText(String.valueOf(maxTemp) + "°C");

                dialogInterface.dismiss();
                FishyClient.updateTemperatureRange(TankListActivity.tankList.get(index).getId(), (minTemp), (maxTemp));
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
        FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
        minimumTemp =  String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsMinTemp());
        maximumTemp =  String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsMaxTemp());

        minTempTable.setText(minimumTemp);
        maxTempTable.setText(maximumTemp);
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
