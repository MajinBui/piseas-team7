package group7.piseas;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import group7.piseas.Helpers.TankTimer;
import group7.piseas.Objects.Tank;
import piseas.network.FishyClient;

public class TankMainPageActivity extends AppCompatActivity {
    static int index;
    TextView temperatureTextView;
    TextView pHTextView;
    TextView conductivityTextView;
    TextView tv;
    Tank tank;
    private long UPDATE_VALUE_DELAY = 10000;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.post(new TankTimer(getApplicationContext(), temperatureTextView, pHTextView));
            handler.postDelayed(runnable, UPDATE_VALUE_DELAY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tank_main_page);
        index = getIntent().getIntExtra("id", -1);

         tv = (TextView) findViewById(R.id.title);

        try {
            tv.setText("Tank: " + TankListActivity.tankList.get(index).getName());
        } catch (IndexOutOfBoundsException e) {
            tv.setText("Tank: " + "error");
        }
        //Creation of notifications
        temperatureTextView = (TextView) findViewById(R.id.temperatureValueTextView);
        pHTextView = (TextView) findViewById(R.id.pHValueTextView);
        conductivityTextView =(TextView) findViewById(R.id.value3);
        //handler.postDelayed(runnable, UPDATE_VALUE_DELAY);
    }

    public void onFeedingClick(View view){
        Intent i = new Intent(this, FeedingManagementActivity.class);
        i.putExtra("id", index);
        startActivity(i);
    }
    public void onLightingClick(View view) {
        Intent i = new Intent(this, LightManagementActivity.class);
        i.putExtra("id", index);
        startActivity(i);
    }

    public void onWaterAnalysisClick(View view){
        Intent i = new Intent(this, WaterAnalysisManagementActivity.class);
        i.putExtra("id", index);
        // Get new settings
        tank.retrieveMobileSettingsFromServer();
        startActivity(i);
    }
    public void onWaterFlowClick(View view){
        Intent i = new Intent(this, WaterLevelManagementActivity.class);
        i.putExtra("id", index);
        // Get new settings
        tank.retrieveMobileSettingsFromServer();
        startActivity(i);
    }
    public void onTemperatureClick(View view){
        Intent i = new Intent(this, TemperatureManagementActivity.class);
        i.putExtra("id", index);
        startActivity(i);
    }
    public void onTankInfoClick(View view){
        Intent i = new Intent(this, TankManagementActivity.class);
        Log.d("TankMain", "open management");
        i.putExtra("id", index);
        startActivity(i);
    }
    public void logButtonClick(View view){
        Intent i = new Intent(this, LogActivity.class);
        // Get new log
        tank.retrieveActionLogFromServer();
        // do not need to reload object for this one
        i.putExtra("id", index);
        startActivity(i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //handler.removeCallbacks(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //handler.removeCallbacks(runnable);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updatePage();
        tank = TankListActivity.tankList.get(index);
        //handler.postDelayed(runnable, UPDATE_VALUE_DELAY);
    }

    protected void updatePage(){
        if (index != -1){
            FishyClient.retrieveSensorData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
            temperatureTextView.setText(String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSensorCurrentTemp()) +
                "\u00b0C");
            pHTextView.setText(String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSensorPH()));
            conductivityTextView.setText(String.valueOf(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSensorConductivity()));
            tv.setText("Tank: " + TankListActivity.tankList.get(index).getName());
        }
        else
        Toast.makeText(getBaseContext(),
                "Error while updating page",
                Toast.LENGTH_LONG).show();
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
