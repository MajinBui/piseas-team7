
package group7.piseas;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import group7.piseas.Objects.Tank;
import piseas.network.FishyClient;
//import group7.piseas.Server.FishyClient;

public class AddTankActivity extends AppCompatActivity {
    int REQUEST_CODE = 7;
    String[] strings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tank);
        final EditText edit1 = (EditText) findViewById(R.id.codeEditer);
        final EditText edit2 = (EditText) findViewById(R.id.passwordEditer);
        Button addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strings = new String[2];
                strings[0] = edit1.getText().toString();
                strings[1] = edit2.getText().toString();
                if (validate(strings)){
                    Intent intent = (new Intent(getBaseContext(),TankManagementActivity.class));
                    intent.putExtra("id", -1);
                    intent.putExtra("code", strings[0]);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("AddTank", "on Activity Result");
        if (REQUEST_CODE == requestCode){
            Log.i("AddTank", "request code match");
            if(resultCode == RESULT_OK){
                Log.i("AddTank", "result OK");
                int fishType = data.getIntExtra("type", 0);
                int tankSize  = data.getIntExtra("size", 0);
                String name = data.getStringExtra("name");
                String desc = data.getStringExtra("desc");
                Tank tank = new Tank(getApplicationContext(), strings[0],
                        strings[1],
                        name, fishType, tankSize, desc);
                TankListActivity.tankList.add(tank);
                Log.i("AddTank", "NEW TANK ADDED" + TankListActivity.tankList.size());
                tank.sendTankDetailsToServer();
            }
        }
        finish();
    }

    private boolean validate(String[] strings){
        Log.i("AddTank", "Validate: " + strings[0].toString() + strings[1].toString());
        // id can be letters and numbers
        // password can apparently be strings
        //if (onlyNums(strings[0])) { // check for digits and blanks
            //if(onlyNums(strings[1])){
                for (Tank tank: TankListActivity.tankList){ //check for repeat
                    if (strings[0].equals(tank.getId())){
                        Toast.makeText(getBaseContext(),
                                "The tank code you provided is already added. Please try again!",
                                Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
                if (FishyClient.checkMobileXmlPassword(strings[0], strings[1]))
                        return true;

                Toast.makeText(getBaseContext(),
                        "The tank code and password you provided does not match existing records. Please try again!",
                        Toast.LENGTH_LONG).show();
                return false;
            /*}
            else
                Toast.makeText(getBaseContext(),
                        "Must enter password, can only contain digits. Please try again!",
                    Toast.LENGTH_LONG).show();
            return false;*/
        //}
        /*Toast.makeText(getBaseContext(),
                "Must enter tank code, can only contain digits. Please try again!",
                Toast.LENGTH_LONG).show();
        return false;*/
    }

    private boolean onlyNums(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
