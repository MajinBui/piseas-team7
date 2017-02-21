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


        XmlPullParserHandler parser = new XmlPullParserHandler(this, tank_id);

        tv_code.setText(parser.getCode());
        tv_password.setText(parser.getPassword());
        //tv_date_sent.setText(parser.getDateSent(is));
        CurrDate ds = parser.getDateSent();

        int totalFeeds = parser.getTotalFeeds();
        int feedTime = parser.getFeedTime();
        int currTemp = parser.getCurrentTemp();
        float phLevel = parser.getPH();
        float cond =  parser.getConductivity();

        TextView tv_codeMobile = (TextView) findViewById(R.id.tv_mobilecode);
        tv_codeMobile.setText(parser.getPiCode());

        String piPass = parser.getPiPassword();
        float piSize = parser.getPiSize();
        String PiDesc = parser.getPiDescription();
        CurrDate piDate = parser.getPiDateSent();
        boolean piFeed = parser.getPiFeed();
        boolean piAutoFeed = parser.getPiAutoFeed();
        boolean getPiLight = parser.getPiLight();
        boolean piAutoLight = parser.getPiAutoLight();
        float piMin = parser.getPiMin();
        float piMax = parser.getPiMax();
        boolean piAutoTemp = parser.getPiAutoTemp();
        boolean piManualFan = parser.getPiManualFan();
        boolean piManH = parser.getPiManualHeater();
        boolean piDrain = parser.getPiDrain();
        boolean piFill = parser.getPiFill();
        boolean piAutoChange = parser.getPiAutoWaterChange();
        float piPHMin = parser.getPiPHMin();
        float piPHMax = parser.getPiPHMax();

        ArrayList<FeedSchedule> feeds = parser.getFeedSchedules();
        ArrayList<LightSchedule> lights = parser.getLightSchedules();

    }

    public void onPiseas(View view) throws IOException{
        TextView et_code = (TextView) findViewById(R.id.et_code);
        TextView et_password = (TextView) findViewById(R.id.et_password);
        TextView et_size = (TextView) findViewById(R.id.et_size);
        XmlPullParserHandler parser = new XmlPullParserHandler(this, tank_id);

        parser.setCode(et_code.getText().toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}