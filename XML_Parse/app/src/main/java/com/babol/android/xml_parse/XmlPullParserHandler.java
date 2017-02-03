package com.babol.android.xml_parse;

import android.content.Context;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlSerializer;

import java.io.StringReader;
import java.io.StringWriter;
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


    // TEST RETRIEVING FROM 1_MOBILE.XML FILE, RETRIEVING WORKKS ---------------------------------------
    private String parseMobile(InputStream is, String TAG_NAME, int attribute){
        String code = "-";
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            is.reset();

            parser.setInput(is, null);

            int event;

            while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (TAG_NAME.equals(tag)) {
                        event = parser.nextTag();
                        parser.nextTag();
                        tag = parser.getName();
                        //code = parser.getAttributeName(0);
                        if(tag.equals("details"))
                            code = parser.getAttributeValue(attribute);
                    }
                }
            }
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        return code;
    }

    public String getMobileCode(InputStream is){
        // Parse from tag "Tank", attribute 0
        return parseMobile(is, "Tank", 0);
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

    public void write(String tag, String attribute, String value, int id) {

        String filepath = id + "_mobile.xml";

        File newXml = new File(context.getFilesDir() + "/" + filepath);


        // Check to see if file exists, if file does not exist, create new
        if(!newXml.isFile())
            createXml(filepath);

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();


            // CRASHES AFTER HERE - SAXException error, unexpected end of document
            //Document doc = docBuilder.parse(new InputSource(new StringReader("1_mobile.xml")));
            Document doc = docBuilder.parse(context.openFileInput("1_mobile.xml"));


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

    // Create new xml file with default values
    private void createXml(String filename){
        try {
            FileOutputStream myFile = context.openFileOutput(filename, context.MODE_PRIVATE);

            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            // Initial data to fill into new XML file - temporary
            String code = "abc123";
            String password = "helloworld";
            int size = 20;
            String description = "my fishy home";
            String dateSent = "0045-02-03-2017";
            boolean push = true;
            int main = 1;
            String log = "This is the log.";
            boolean feed = false;
            boolean autoFeed = false;
            boolean light = false;
            boolean autoLight = false;
            int minTemp = 16;
            int maxTemp = 22;
            boolean autoTemp = false;
            boolean manualFan = false;
            boolean manualHeater = false;
            boolean drain = false;
            boolean fill = false;
            boolean autoWaterChange = true;
            float pHMin = 1;
            float pHMax = 9;

            xmlSerializer.setOutput(writer);
            // start document
            xmlSerializer.startDocument("UTF-8", true);
            // open tags and insert attributes into each individual
            xmlSerializer.startTag("", "Piseas");

            // Tank
            xmlSerializer.startTag("", "Tank");
            xmlSerializer.startTag("", "details");
                xmlSerializer.attribute("", "code", code);
                xmlSerializer.attribute("", "password", password);
                xmlSerializer.attribute("", "size", String.valueOf(size));
                xmlSerializer.attribute("", "description", description);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Tank");

            // Date
            xmlSerializer.startTag("", "Date");
            xmlSerializer.startTag("", "details");
                xmlSerializer.attribute("", "dateSent", dateSent);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Date");

            // Settings
            xmlSerializer.startTag("", "Settings");
            xmlSerializer.startTag("", "details");
                xmlSerializer.attribute("", "push", String.valueOf(push));
                xmlSerializer.attribute("", "main", String.valueOf(main));
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Settings");

            // Feed
            xmlSerializer.startTag("", "Feed");
            xmlSerializer.startTag("", "details");
                xmlSerializer.attribute("", "feed", String.valueOf(feed));
                xmlSerializer.attribute("", "autoFeed", String.valueOf(autoFeed));
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Feed");

            // FeedSchedule
            xmlSerializer.startTag("", "FeedSchedule");
            xmlSerializer.endTag("", "FeedSchedule");

            // Light
            xmlSerializer.startTag("", "Light");
            xmlSerializer.startTag("", "details");
                xmlSerializer.attribute("", "light", String.valueOf(light));
                xmlSerializer.attribute("", "autoLight", String.valueOf(autoLight));
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Light");

            // LightSchedule
            xmlSerializer.startTag("", "LightSchedule");
            xmlSerializer.endTag("", "LightSchedule");

            // Temperature
            xmlSerializer.startTag("", "Temperature");
            xmlSerializer.startTag("", "details");
                xmlSerializer.attribute("", "min", String.valueOf(minTemp));
                xmlSerializer.attribute("", "max", String.valueOf(maxTemp));
                xmlSerializer.attribute("", "autoTemp", String.valueOf(autoTemp));
                xmlSerializer.attribute("", "manualFan", String.valueOf(manualFan));
                xmlSerializer.attribute("", "manualHeater", String.valueOf(manualHeater));
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Temperature");

            // Pump
            xmlSerializer.startTag("", "Pump");
            xmlSerializer.startTag("", "details");
                xmlSerializer.attribute("", "drain", String.valueOf(drain));
                xmlSerializer.attribute("", "fill", String.valueOf(fill));
                xmlSerializer.attribute("", "autoWaterChange", String.valueOf(autoWaterChange));
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Pump");

            // Sensor
            xmlSerializer.startTag("", "Sensor");
            xmlSerializer.startTag("", "details");
                xmlSerializer.attribute("", "pHmin", String.valueOf(pHMin));
                xmlSerializer.attribute("", "pHmax", String.valueOf(pHMax));
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Sensor");

            xmlSerializer.endTag("", "Piseas");

            xmlSerializer.endDocument();

            writer.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }

        int z = 2;
    }

    public void setCode(String value, int id){
        write("Tank", "code", value, id);
    }

    public void setpassword(String value, int id){
        write("Tank", "password", value, id);
    }

    public void setSize(String value, int id){
        write("Tank", "size", value, id);
    }

    public void setDescription(String value, int id){
        write("Tank", "description", value, id);
    }

    public void setDateSent(String value, int id){
        write("Date", "dateSent", value, id);
    }

    public void setPush(String value, int id){
        write("Settings", "push", value, id);
    }

    public void setMain(String value, int id){
        write("Settings", "main", value, id);
    }

    public void setFeed(String value, int id){
        write("Feed", "feed", value, id);
    }

    public void setAutoFeed(String value, int id){
        write("Feed", "autoFeed", value, id);
    }

//    FeedSchedule
//    LightSchedule

    public void setLight(String value, int id){
        write("Light", "light", value, id);
    }

    public void setAutoLight(String value, int id){
        write("Light", "autoLight", value, id);
    }

    public void setTempMin(String value, int id){
        write("Temperature", "min", value, id);
    }

    public void setTempMax(String value, int id){
        write("Temperature", "max", value, id);
    }

    public void setAutoTemp(String value, int id){
        write("Temperature", "autoTemp", value, id);
    }

    public void setManualFan(String value, int id){
        write("Temperature", "manualFan", value, id);
    }

    public void setManualHeater(String value, int id){
        write("Temperature", "manualHeater", value, id);
    }

    public void setDrain(String value, int id){
        write("Pump", "drain", value, id);
    }

    public void setFill(String value, int id){
        write("Pump", "fill", value, id);
    }

    public void setAutoWaterChange(String value, int id){
        write("Pump", "autoWaterChange", value, id);
    }

    public void setPHMin(String value, int id){
        write("Sensor", "pHmin", value, id);
    }

    public void setSensor(String value, int id){
        write("Sensor", "pHmax", value, id);
    }



}