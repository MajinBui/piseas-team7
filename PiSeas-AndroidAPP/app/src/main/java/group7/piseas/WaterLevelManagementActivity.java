package group7.piseas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class WaterLevelManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_level_management);
        int index = getIntent().getIntExtra("id", -1);
        TextView tv = (TextView) findViewById(R.id.tankName);
        tv.setText("Tank: " + TankListActivity.tankList.get(index).getName());
    }
}
