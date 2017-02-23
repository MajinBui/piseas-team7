package group7.piseas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList;
import group7.piseas.Adapters.LogAdapter;
import group7.piseas.Objects.LogItem;

/**
 * Created by Salli on 2017-02-20.
 */

public class LogActivity extends AppCompatActivity {
    ListView listView;
    static LogAdapter adapter;
    List<LogItem> logList;
    int tankID = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        logList = new ArrayList<LogItem>();

        listView = (ListView) findViewById(R.id.list);
        adapter = new LogAdapter(this, R.layout.log_info, logList);

        listView.setAdapter(adapter);
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
        logList.add(new LogItem("7:00pm", "Tank Light Turned On"));
        logList.add(new LogItem("10:00pm", "Tank Light Turned Off"));
        logList.add(new LogItem("8:00am", "Feed fish"));
        if (tankID != -1){
            //make call to parser to create list of log objects to display
        }
        adapter.notifyDataSetChanged();
    }
}
