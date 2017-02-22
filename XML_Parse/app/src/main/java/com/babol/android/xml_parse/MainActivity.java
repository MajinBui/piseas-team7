package com.babol.android.xml_parse;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Objects.FeedSchedule;
import Objects.LightSchedule;
import Objects.LogEnum;
import Objects.Logs;

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
        TextView tv_date_sent = (TextView) findViewById(R.id.tv_date_sent);
        TextView tv_total_feeds = (TextView) findViewById(R.id.tv_total_feeds);
        TextView tv_feed_time = (TextView) findViewById(R.id.tv_feed_time);
        TextView tv_current_temp = (TextView) findViewById(R.id.tv_current_temp);
        TextView tv_ph = (TextView) findViewById(R.id.tv_ph);
        TextView tv_conductivity = (TextView) findViewById(R.id.tv_conductivity);
        TextView tv_codeMobile = (TextView) findViewById(R.id.tv_mobilecode);


        // Parser test
        XmlPullParserHandler parser = new XmlPullParserHandler(this, tank_id);

        tv_code.setText(parser.getSensorCode());
        tv_password.setText(parser.getSensorPassword());
        //tv_date_sent.setText(parser.getDateSent(is));
        piDate ds = parser.getSensorDate();

        int totalFeeds = parser.getSensorTotalFeeds();
        int feedTime = parser.getSensorFeedTime();
        int currTemp = parser.getSensorCurrentTemp();
        float phLevel = parser.getSensorPH();
        float cond =  parser.getSensorConductivity();

        tv_codeMobile.setText(parser.getSettingsCode());

        String piPass = parser.getSettingsPassword();
        float piSize = parser.getSettingsSize();
        String PiDesc = parser.getSettingsDescription();
        piDate piDate = parser.getSettingsDate();
        boolean piFeed = parser.getSettingsFeed();
        boolean piAutoFeed = parser.getSettingsAutoFeed();
        boolean getPiLight = parser.getSettingsLight();
        boolean piAutoLight = parser.getSettingsAutoLight();
        float piMin = parser.getSettingsMin();
        float piMax = parser.getSettingsMax();
        boolean piAutoTemp = parser.getSettingsAutoTemp();
        boolean piManualFan = parser.getSettingsManualFan();
        boolean piManH = parser.getSettingsManualHeater();
        boolean piDrain = parser.getSettingsDrain();
        boolean piFill = parser.getSettingsFill();
        boolean piAutoChange = parser.getSettingsAutoWaterChange();
        float piPHMin = parser.getSettingsPHMin();
        float piPHMax = parser.getSettingsPHMax();
        float cMin = parser.getSettingsCMin();
        float cHMax = parser.getSettingsCMax();

        ArrayList<FeedSchedule> feeds = parser.getFeedSchedules();
        ArrayList<LightSchedule> lights = parser.getLightSchedules();

    }

    public void onPiseas(View view) throws IOException{
        TextView et_code = (TextView) findViewById(R.id.et_code);
        TextView et_password = (TextView) findViewById(R.id.et_password);
        TextView et_size = (TextView) findViewById(R.id.et_size);


        // Parser test
        XmlPullParserHandler parser = new XmlPullParserHandler(this, tank_id);
        parser.setCode(et_code.getText().toString());
    }

    public void onLogs(View view) throws IOException{

        XmlPullParserHandler parser = new XmlPullParserHandler(this, tank_id);

        Logs newLog = parser.getLogs();
        LogEnum code = LogEnum.x001;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}