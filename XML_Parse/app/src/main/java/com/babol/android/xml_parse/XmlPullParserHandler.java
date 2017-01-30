package com.babol.android.xml_parse;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlSerializer;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class XmlPullParserHandler {

    private Context context;

    public XmlPullParserHandler(Context context){
        this.context=context;
    }

    // Retrieve

    private String parse(InputStream is, String TAG_NAME, int attribute){
        String code = "-";
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();


            XmlSerializer serial = factory.newSerializer();

            is.reset();

            parser.setInput(is, null);

           int event;

            while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (TAG_NAME.equals(tag)) {
                        //code = parser.getAttributeName(0);
                        code = parser.getAttributeValue(attribute);
                    }
                }
            }
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        return code;
    }

    public String getCode(InputStream is){
        // Parse from tag "Tank", attribute 0
        return parse(is, "Tank", 0);
    }

    public String getPassword(InputStream is){
        // Parse from tag "Tank", attribute 1
        return parse(is, "Tank", 1);
    }

    public String getDateSent(InputStream is){
        // Parse from tag "Date", attribute 0
        return parse(is, "Date", 0);
    }

    public String getTotalFeeds(InputStream is){
        // Parse from tag "Feed", attribute 0
        return parse(is, "Feed", 0);
    }

    public String getFeedTime(InputStream is){
        // Parse from tag "Feed", attribute 1
        return parse(is, "Feed", 1);
    }

    public String getCurrentTemp(InputStream is){
        // Parse from tag "Temperature", attribute 0
        return parse(is, "Temperature", 0);
    }

    public String getPH(InputStream is){
        // Parse from tag "Sensor", attribute 0
        return parse(is, "Sensor", 0);
    }

    public String getConductivity(InputStream is){
        // Parse from tag "Sensor", attribute 1
        return parse(is, "Sensor", 1);
    }

    // Update

    public void write(String tag, String attribute, String value) {

        try {
            String filepath = "test.xml";

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(filepath);

            Node PiSeas = doc.getFirstChild();

            Node xmlTag = doc.getElementsByTagName(tag).item(0);

            NodeList list = xmlTag.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {

                Node detail = list.item(i);

                // get the salary element, and update the value
                if (attribute.equals(detail.getNodeName())) {
                    detail.setTextContent(value);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setCode(String value){
        write("Tank", "code", value);
    }

    public void setpassword(String value){
        write("Tank", "password", value);
    }

    public void setSize(String value){
        write("Tank", "size", value);
    }

    public void setDescription(String value){
        write("Tank", "description", value);
    }

    public void setDateSent(String value){
        write("Date", "dateSent", value);
    }

    public void setPush(String value){
        write("Settings", "push", value);
    }

    public void setMain(String value){
        write("Settings", "main", value);
    }

    public void setFeed(String value){
        write("Feed", "feed", value);
    }

    public void setAutoFeed(String value){
        write("Feed", "autoFeed", value);
    }

//    FeedSchedule
//    LightSchedule

    public void setLight(String value){
        write("Light", "light", value);
    }

    public void setAutoLight(String value){
        write("Light", "autoLight", value);
    }

    public void setTempMin(String value){
        write("Temperature", "min", value);
    }

    public void setTempMax(String value){
        write("Temperature", "max", value);
    }

    public void setAutoTemp(String value){
        write("Temperature", "autoTemp", value);
    }

    public void setManualFan(String value){
        write("Temperature", "manualFan", value);
    }

    public void setManualHeater(String value){
        write("Temperature", "manualHeater", value);
    }

    public void setDrain(String value){
        write("Pump", "drain", value);
    }

    public void setFill(String value){
        write("Pump", "fill", value);
    }

    public void setAutoWaterChange(String value){
        write("Pump", "autoWaterChange", value);
    }

    public void setPHMin(String value){
        write("Sensor", "pHmin", value);
    }

    public void setSensor(String value){
        write("Sensor", "pHmax", value);
    }
}