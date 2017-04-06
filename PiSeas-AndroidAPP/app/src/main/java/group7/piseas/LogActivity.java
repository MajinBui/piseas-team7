package group7.piseas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;
import group7.piseas.Adapters.LogAdapter;
import group7.piseas.Helpers.Utilities;
import group7.piseas.Objects.PiseasLog.Logs;
import group7.piseas.Objects.PiseasLog.PiseasLog;
import group7.piseas.Objects.LogItem;
import group7.piseas.Objects.Tank;

/**
 * Created by Salli on 2017-02-20.
 */

public class LogActivity extends AppCompatActivity {
    ListView listView;
    static LogAdapter adapter;
    List<LogItem> logList;

    Tank tank;
    Logs logs;
    TextView title;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        logList = new ArrayList<LogItem>();
        title = (TextView) findViewById(R.id.tankName);
        listView = (ListView) findViewById(R.id.list);
        adapter = new LogAdapter(this, R.layout.log_info, logList);

        listView.setAdapter(adapter);
        index = getIntent().getIntExtra("id", -1);
        tank = TankListActivity.tankList.get(getIntent().getIntExtra("id", -1));
        load();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    private void load(){
        logs = tank.getLogs();
        for (PiseasLog log : logs.getPiseasLogs()) {
            logList.add(new LogItem(Utilities.simpleDateToString(log.getDate()), log.getDescription()));
        }
        adapter.notifyDataSetChanged();
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
