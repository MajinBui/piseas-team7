package com.babol.android.xml_parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends Activity {

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

        try{
            XmlPullParserHandler parser = new XmlPullParserHandler(this);
            InputStream is = getAssets().open("Data.xml");

            tv_code.setText(parser.getCode(is));
            tv_password.setText(parser.getPassword(is));
            tv_date_sent.setText(parser.getDateSent(is));
            tv_total_feeds.setText(parser.getTotalFeeds(is));
            tv_feed_time.setText(parser.getFeedTime(is));
            tv_total_feeds.setText(parser.getTotalFeeds(is));
            tv_current_temp.setText(parser.getCurrentTemp(is));
            tv_ph.setText(parser.getPH(is));
            tv_conductivity.setText(parser.getConductivity(is));


            is.close();
        } catch (IOException e) {e.printStackTrace();}


    }

    public void onPiseas(View view) throws IOException{
        TextView et_code = (TextView) findViewById(R.id.et_code);
        TextView et_password = (TextView) findViewById(R.id.et_password);
        TextView et_size = (TextView) findViewById(R.id.et_size);
        TextView et_description = (TextView) findViewById(R.id.et_description);

        XmlPullParserHandler parser = new XmlPullParserHandler(this);

        parser.setCode("TEST");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }




}