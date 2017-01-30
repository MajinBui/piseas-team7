package group7.piseas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import group7.piseas.Adapters.TankAdapter;
import group7.piseas.Objects.Tank;
import group7.piseas.Server.FishyClient;

public class TankListActivity extends AppCompatActivity {

    public static List<Tank> tankList;
    ListView listView;
    static TankAdapter adapter;
    ImageButton addButton;
    int REQUEST_CODE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("tanklist", "onCreate");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tank_list);
        tankList = new ArrayList<Tank>();//get from shared preferences

        listView = (ListView) findViewById(R.id.list);
        adapter = new TankAdapter(this, R.layout.tank_info, tankList);
        addButton = (ImageButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("TankList", "add clicked");
                startActivity(new Intent(getBaseContext(), AddTankActivity.class));
            }
        });

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getBaseContext(), TankMainPageActivity.class);
                intent.putExtra("id", i);
                startActivity(intent);
            }
        });
        load();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public static void update(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.serverSetup:
                Log.i("LISTACTIVITY", "SERVER SET UP");
                HashMap<String, String> dataList = new HashMap<String, String>();
                dataList.put("tankId", "801");
                dataList.put("pw", "108");
                dataList.put("name", "");
                dataList.put("type", 0+"");
                dataList.put("size", 0+"");
                dataList.put("desc", "");
                FishyClient.writeToServerData("801", dataList);

                dataList.put("tankId", "802");
                dataList.put("pw", "208");
                dataList.put("name", "TESTER WITH DATA");
                dataList.put("type", 1+"");
                dataList.put("size", 2+"");
                dataList.put("desc", "Just keep swimming");
                FishyClient.writeToServerData("802", dataList);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        Log.i("TANKLIST", "onStop");
        SharedPreferences.Editor editor = getSharedPreferences("piseas", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.putInt("listSize", tankList.size());
        for (int i = 0; i<tankList.size();i++){
            editor.putInt("code"+i, tankList.get(i).getId());
        }
        editor.commit();
        super.onStop();
    }

    private void load(){
        Log.i("TANKLIST", "LOAD");
        SharedPreferences sharedPref = getSharedPreferences("piseas", MODE_PRIVATE);
        int size = sharedPref.getInt("listSize", 0);
        Log.i("TANKLIST", "LOAD size " + size);

        int pw = 0;
        String name = "";
        String desc = "";
        int type = 0;
        int tankSize = 0;

        for (int i=0;i<size; i++){
            int tankCode = sharedPref.getInt("code"+i, 0);
            Log.i("TANKLIST", "LOAD Tank Code" + tankCode);
            HashMap<String, String> dataList = FishyClient.retrieveServerData(tankCode+"");
            if (!dataList.get("pw").isEmpty())
                pw = Integer.parseInt(dataList.get("pw"));
            if (!dataList.get("name").isEmpty())
                name = dataList.get("name");
            if (!dataList.get("desc").isEmpty())
                desc = dataList.get("desc");
            if (!dataList.get("type").isEmpty())
                type = Integer.parseInt(dataList.get("type"));
            if (!dataList.get("size").isEmpty())
                tankSize = Integer.parseInt(dataList.get("size"));
            Log.i("TANKLIST", "LOAD ADD TANK " + tankCode + ", "+ pw + ", "+ name + ", "+ type + ", "+ size+ ", "+desc);
            tankList.add(new Tank(tankCode, pw ,name, type, tankSize, desc));
        }
        adapter.notifyDataSetChanged();
    }
}