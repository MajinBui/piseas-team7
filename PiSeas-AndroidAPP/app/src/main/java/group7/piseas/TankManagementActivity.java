package group7.piseas;

import group7.piseas.Adapters.SpinnerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import group7.piseas.Objects.Tank;
import piseas.network.FishyClient;


public class TankManagementActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    int index;
    int tankCode;
    int fishType;
    int tankSize;
    String name;
    String desc;

    EditText nameEdit;
    Spinner spinner;
    Spinner spinner2;
    EditText descEdit;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_tank_management);

        index = getIntent().getIntExtra("id", -100);
        tankCode = getIntent().getIntExtra("code", -100);

        nameEdit = (EditText) findViewById(R.id.nameEditer);
        descEdit = (EditText) findViewById(R.id.descEditor);
        title = (TextView) findViewById(R.id.title);
        if(index>=0)
            if (TankListActivity.tankList.get(index).getName().equals("")) {
                title.setText("Tank Info");
            } else {
                title.setText("Tank Info : " + TankListActivity.tankList.get(index).getName());
            }
        else
            title.setText("Tank Info");

        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = nameEdit.getText().toString();
                desc = descEdit.getText().toString();
                Log.i("TankManagement", "-----------------------------"+name);
                if (index == -1){//new tank
                    if (name.isEmpty() || !uniqueName(name)){
                        Toast.makeText(getBaseContext(),
                                (index == -1)? "You must provide a unique tank name to add new tank.":
                                        "You must provide a unique tank name to update tank.",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Log.i("Tank Management", "SENDING BACK DATA FOR NEW TANK");
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("name", name);
                        returnIntent.putExtra("type", fishType);
                        returnIntent.putExtra("size", tankSize);
                        returnIntent.putExtra("desc", desc);
                        Log.i("Tank Management", String.format("$1"));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
                else {
                    if (name.isEmpty()){
                        Toast.makeText(getBaseContext(),
                                (index == -1)? "You must provide a unique tank name to add new tank.":
                                        "You must provide a unique tank name to update tank.",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        update(index);
                        finish();
                    }
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.fishEditer);

        List<CharSequence> fishSizes = Arrays.asList((CharSequence[]) getResources().getStringArray(R.array.fish_size));
        SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, fishSizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        spinner2 = (Spinner) findViewById(R.id.sizeEditer);

        List<CharSequence> tankSizes = Arrays.asList((CharSequence[]) getResources().getStringArray(R.array.tank_size));
        SpinnerAdapter adapter2 = new SpinnerAdapter(this, android.R.layout.simple_spinner_item, tankSizes);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);

        if(index >=0){
            FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), getFilesDir().getAbsolutePath().toString());
            nameEdit.setText(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsName());
            descEdit.setText(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsDescription());
            spinner.setSelection(TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsType()? 1: 0);
            spinner2.setSelection((int)TankListActivity.tankList.get(index).getPiSeasXmlHandler().getSettingsSize());
        }
    }

    private boolean uniqueName(String str){
        Log.i("Tank Management", "Unique Name");
        for (Tank tank:TankListActivity.tankList){
            if (str.equals(tank.getName()))
                return false;
        }
        return true;
    }

    private void update(int i){
        Log.i("Tank Management", "Update");
        TankListActivity.tankList.get(i).setName(name);
        TankListActivity.tankList.get(i).setType(fishType);
        TankListActivity.tankList.get(i).setSize(tankSize);
        TankListActivity.tankList.get(i).setDesc(desc);
        TankListActivity.tankList.get(i).sendTankDetailsToServer();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("TankMangement", "onItemSelected, spinner");
        Spinner spin = (Spinner)adapterView;
        Spinner spin2 = (Spinner)adapterView;
        if(spin.getId() == R.id.fishEditer)
        {
            fishType = i;
        }
        if(spin2.getId() == R.id.sizeEditer)
        {
            tankSize = i;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
