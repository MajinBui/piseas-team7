package com.babol.android.xml_parse;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import Objects.FeedSchedule;
import Objects.LightSchedule;
import Log.Logs;

public class MainActivity extends Activity {

    private String tank_id = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onData(View view) throws IOException{
        TextView tv_code = (TextView) findViewById(R.id.tv_id);
        TextView tv_password = (TextView) findViewById(R.id.tv_password);
        TextView tv_total_feeds = (TextView) findViewById(R.id.tv_total_feeds);
        TextView tv_feed_time = (TextView) findViewById(R.id.tv_feed_time);
        TextView tv_current_temp = (TextView) findViewById(R.id.tv_current_temp);
        TextView tv_conductivity = (TextView) findViewById(R.id.tv_conductivity);
        TextView tv_codeMobile = (TextView) findViewById(R.id.tv_mobilecode);

        // Parser test
        XmlPullParserHandler parser = new XmlPullParserHandler(this, tank_id);

        tv_code.setText(parser.getSensorID());
        tv_password.setText(parser.getSensorPassword());
        //tv_date_sent.setText(parser.getDateSent(is));
        piDate ds = parser.getSensorDate();

        int totalFeeds = parser.getSensorTotalFeeds();
        int feedHr = parser.getSensorFeedHr();
        int feedMin = parser.getSensorFeedMin();
        int currTemp = parser.getSensorCurrentTemp();
        float phLevel = parser.getSensorPH();
        float cond =  parser.getSensorConductivity();

        tv_codeMobile.setText(parser.getSettingsID());

        String piPass = parser.getSettingsPassword();
        float piSize = parser.getSettingsSize();
        String piDesc = parser.getSettingsDescription();
        Boolean piType = parser.getSettingsType();
        piDate piDate = parser.getSettingsDate();
        boolean piFeed = parser.getSettingsFeed();
        boolean piAutoFeed = parser.getSettingsAutoFeed();
        boolean getPiLight = parser.getSettingsLight();
        boolean piAutoLight = parser.getSettingsAutoLight();
        float piMin = parser.getSettingsMinTemp();
        float piMax = parser.getSettingsMaxTemp();
        boolean piDrain = parser.getSettingsDrain();
        boolean piFill = parser.getSettingsFill();
        boolean piAutoChange = parser.getSettingsAutoWaterChange();
        float piPHMin = parser.getSettingsPHMin();
        float piPHMax = parser.getSettingsPHMax();
        float cMin = parser.getSettingsCMin();
        float cHMax = parser.getSettingsCMax();

        ArrayList<FeedSchedule> feeds = parser.getFeedSchedules();
        ArrayList<LightSchedule> lights = parser.getLightSchedules();


        String idid = parser.getSettingsID();
        TextView tv_test = (TextView) findViewById(R.id.tv_ph);
        tv_test.setText(idid);



        String ddd = ds.toString();
        String dis = piDate.getFullDate();
        String timez = piDate.toString();
        int asd = 8;
        asd++;

    }

    public void onPiseas(View view) throws IOException{
        TextView et_code = (TextView) findViewById(R.id.et_code);
        TextView et_password = (TextView) findViewById(R.id.et_password);
        TextView et_size = (TextView) findViewById(R.id.et_size);


        // Parser test
        XmlPullParserHandler parser = new XmlPullParserHandler(this, tank_id);
        parser.setID(et_code.getText().toString());

        piDate piz = new piDate("2017-02-20T19:19:19+0500");
        parser.setDateSent(piz.getFullDate());

        FeedSchedule f1 = new FeedSchedule(05, 45);
        FeedSchedule f2 = new FeedSchedule(14, 15);
        FeedSchedule f3 = new FeedSchedule(18, 30);
        ArrayList<FeedSchedule> ffs = new ArrayList<FeedSchedule>();
        ffs.add(f1);
        ffs.add(f2);
        ffs.add(f3);
        parser.setFeed(ffs, true, true);

    }

    public void onLogs(View view) throws IOException{

        XmlPullParserHandler parser = new XmlPullParserHandler(this, tank_id);


        // A Logs object is created, with a list of Log objects inside
        Logs newLog = parser.getLogs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}