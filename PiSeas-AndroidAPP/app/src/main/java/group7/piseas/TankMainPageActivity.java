package group7.piseas;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import group7.piseas.Helpers.TankTimer;

//com.sun.org.apache.xerces.internal.dom.DeferredDocumentImpl
public class TankMainPageActivity extends AppCompatActivity {
    static int index;

    TextView temperatureTextView;
    TextView pHTextView;
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
        TextView tv = (TextView) findViewById(R.id.tankName);
        try {
            tv.setText("Tank: " + TankListActivity.tankList.get(index).getName());
        } catch (IndexOutOfBoundsException e) {
            tv.setText("Tank: " + "error");
        }
        //Creation of notifications
        temperatureTextView = (TextView) findViewById(R.id.temperatureValueTextView);
        pHTextView = (TextView) findViewById(R.id.pHValueTextView);
        handler.postDelayed(runnable, UPDATE_VALUE_DELAY);
    }

    public void onFeedingClick(View view){
        Intent i = new Intent(this, FeedingManagementActivity.class);
        startActivity(i);
    }
    public void onLightingClick(View view){
        Intent i = new Intent(this, LightManagementActivity.class);
        startActivity(i);}

    public void onWaterAnalysisClick(View view){
        Intent i = new Intent(this, WaterAnalysisManagementActivity.class);
        i.putExtra("id", index);
        startActivity(i);
    }
    public void onWaterFlowClick(View view){
        Intent i = new Intent(this, WaterLevelManagementActivity.class);
        i.putExtra("id", index);
        startActivity(i);
    }
    public void onTemperatureClick(View view){
        Intent i = new Intent(this, TemperatureManagementActivity.class);
        startActivity(i);
    }
    public void onTankInfoClick(View view){
        Intent i = new Intent(this, TankManagementActivity.class);
        i.putExtra("id", index);
        startActivity(i);
    }
    public void logButtonClick(View view){
        Intent i = new Intent(this, LogActivity.class);
        i.putExtra("id", index);
        startActivity(i);
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
        handler.postDelayed(runnable, UPDATE_VALUE_DELAY);
    }
}
