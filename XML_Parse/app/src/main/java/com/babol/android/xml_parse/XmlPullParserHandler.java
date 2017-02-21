package com.babol.android.xml_parse;

import android.content.Context;
import android.util.Xml;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Array;
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import Objects.FeedSchedule;
import Objects.LightSchedule;


public class XmlPullParserHandler {

    private Context context;
    private InputStream is;
    private String id;              // tank code, used for identifying incoming xml files and properly saving

    public XmlPullParserHandler(Context context, String id){
        this.context=context;
        this.id = id;
    }

    // Retrieve data

    // Parsing of the tank_<id>_mobile.xml file, file is save in Assets
    private String parseMobile(String TAG_NAME, int attribute){
        String code = "-";
        try{
            is = context.getAssets().open("tank_" + id + "_mobile.xml");
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
                        is.close();
                        return code;
                    }
                }
            }
            is.close();
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        return code;
    }

    public String getCode(){
        // Parse from tag "Tank", attribute 0
        return parseMobile("Tank", 0);
    }

    public String getPassword(){
        // Parse from tag "Tank", attribute 1
        return parseMobile("Tank", 1);
    }

    public CurrDate getDateSent(){
        int ds[] = new int[6];
        for(int i = 0; i<6 ;i++)
            ds[i] = Integer.parseInt(parseMobile("Date", i));

        return new CurrDate(ds[0], ds[1], ds[2], ds[3], ds[4], ds[5]);
    }

    public int getTotalFeeds(){
        // Parse from tag "Feed", attribute 0
        return Integer.parseInt(parseMobile("Feed", 0));
    }

    public int getFeedTime(){
        // Parse from tag "Feed", attribute 1
        return Integer.parseInt(parseMobile("Feed", 1));
    }

    public int getCurrentTemp(){
        // Parse from tag "Temperature", attribute 0
        return Integer.parseInt(parseMobile("Temperature", 0));
    }

    public float getPH(){
        // Parse from tag "Sensor", attribute 0
        return Float.parseFloat(parseMobile("Sensor", 0));
    }

    public float getConductivity(){
        // Parse from tag "Sensor", attribute 1
        return Float.parseFloat(parseMobile("Sensor", 1));
    }

    // Parsing of the tank_<id>_pi.xml file, file saved in internal storage
    private String parsePi(String TAG_NAME, int attribute){
        String code = "-";
        try{
            File file = new File(context.getFilesDir() + "/tank_" + id + "_pi.xml");
            FileInputStream fis = new FileInputStream(file);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(fis, null);

            int event;

            while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String tag = parser.getName();

                    if (TAG_NAME.equals(tag)) {
                        parser.nextTag();
                        tag = parser.getName();
                        if(tag.equals("details")){
                            code = parser.getAttributeValue(attribute);
                            fis.close();
                            return code;
                        }
                    }
                }
            }
            fis.close();
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        return code;
    }

    public String getPiCode(){
        // Parse from tag "Tank", attribute 0
        return parsePi("Tank", 0);
    }

    public String getPiPassword(){
        // Parse from tag "Tank", attribute 1
        return parsePi("Tank", 1);
    }

    public float getPiSize(){
        // Parse from tag "Tank", attribute 2
        return Float.parseFloat(parsePi("Tank", 2));
    }

    public String getPiDescription(){
        // Parse from tag "Tank", attribute 3
        return parsePi("Tank", 3);
    }

    public CurrDate getPiDateSent(){
        int ds[] = new int[6];
        for(int i = 0; i<6 ;i++)
            ds[i] = Integer.parseInt(parseMobile("Date", i));

        return new CurrDate(ds[0], ds[1], ds[2], ds[3], ds[4], ds[5]);
    }

    public boolean getPiFeed(){
        // Parse from tag "Feed", attribute 0
        return Boolean.parseBoolean(parsePi("Feed", 0));
    }

    public boolean getPiAutoFeed(){
        // Parse from tag "Feed", attribute 1
        return Boolean.parseBoolean(parsePi("Feed", 1));
    }

    public boolean getPiLight(){
        // Parse from tag "Light", attribute 0
        return Boolean.parseBoolean(parsePi("Light", 0));
    }

    public boolean getPiAutoLight(){
        // Parse from tag "Light", attribute 1
        return Boolean.parseBoolean(parsePi("Light", 1));
    }

    public float getPiMin(){
        // Parse from tag "Temperature", attribute 0
        return Float.parseFloat(parsePi("Temperature", 0));
    }

    public float getPiMax(){
        // Parse from tag "Temperature", attribute 1
        return Float.parseFloat(parsePi("Temperature", 1));
    }

    public boolean getPiAutoTemp(){
        // Parse from tag "Temperature", attribute 2
        return Boolean.parseBoolean(parsePi("Temperature", 2));
    }

    public boolean getPiManualFan(){
        // Parse from tag "Temperature", attribute 3
        return Boolean.parseBoolean(parsePi("Temperature", 3));
    }

    public boolean getPiManualHeater(){
        // Parse from tag "Temperature", attribute 4
        return Boolean.parseBoolean(parsePi("Temperature", 4));
    }

    public boolean getPiDrain(){
        // Parse from tag "Pump", attribute 0
        return Boolean.parseBoolean(parsePi("Pump", 0));
    }

    public boolean getPiFill(){
        // Parse from tag "Pump", attribute 1
        return Boolean.parseBoolean(parsePi("Pump", 1));
    }

    public boolean getPiAutoWaterChange(){
        // Parse from tag "Pump", attribute 2
        return Boolean.parseBoolean(parsePi("Pump", 2));
    }

    public float getPiPHMin(){
        // Parse from tag "Sensor", attribute 0
        return Float.parseFloat(parsePi("Sensor", 0));
    }

    public float getPiPHMax(){
        // Parse from tag "Sensor", attribute 1
        return Float.parseFloat(parsePi("Sensor", 1));
}

    public ArrayList<FeedSchedule> getFeedSchedules(){
        ArrayList<FeedSchedule> feeds = new ArrayList<>();
        try{
            File file = new File(context.getFilesDir() + "/tank_" + id + "_pi.xml");
            FileInputStream fis = new FileInputStream(file);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(fis, null);

            while ((parser.next()) != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();
                if (tag.equals("FeedSchedule")) {
                    parser.next();
                    while(parser.getName().equals("details")) {
                        // grab the first 2 attributes, hour/min, create the FeedSchedule object. All days are automatically
                        // set to false, parse through the rest of the attributes to see which is set to true. For the bool
                        // to go true, the string needs to be 'true', cannot be 1.
                        FeedSchedule feed = new FeedSchedule(Integer.parseInt(parser.getAttributeValue(0)),
                                Integer.parseInt(parser.getAttributeValue(1)));
                        for (int i = 2; i < 9; i++) {
                            boolean day = Boolean.parseBoolean(parser.getAttributeValue(i));
                            if (day)
                                feed.setWeek(i);
                        }
                        feeds.add(feed);
                        // next will take the same detail tag twice, I think because it needs a closing
                        // tag to go with the opening one. next() twice to get to the actual next tag
                        parser.next();
                        parser.next();
                    }
                    fis.close();
                    return feeds;
                }
            }
            fis.close();
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        return feeds;
    }

    public ArrayList<LightSchedule> getLightSchedules(){
        ArrayList<LightSchedule> lights = new ArrayList<>();
        try{
            File file = new File(context.getFilesDir() + "/tank_" + id + "_pi.xml");
            FileInputStream fis = new FileInputStream(file);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(fis, null);

            while ((parser.next()) != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();
                if (tag.equals("LightSchedule")) {
                    parser.next();
                    while(parser.getName().equals("details")) {
                        LightSchedule light = new LightSchedule(
                                Integer.parseInt(parser.getAttributeValue(0)),      // onHr
                                Integer.parseInt(parser.getAttributeValue(2)),      // offHr
                                Integer.parseInt(parser.getAttributeValue(1)),      // onMn
                                Integer.parseInt(parser.getAttributeValue(3))       // offMn
                        );
                        lights.add(light);
                        // next will take the same detail tag twice, I think because it needs a closing
                        // tag to go with the opening one. next() twice to get to the actual next tag
                        parser.next();
                        parser.next();
                    }
                    fis.close();
                    return lights;
                }
            }
            fis.close();
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        return lights;
    }



    // Update data
    private void write(String tag, String attribute, String value) {

        String filepath = "tank_" + id + "_pi.xml";
        File newXml = new File(context.getFilesDir() + "/" + filepath);

        // Check to see if file exists, if file does not exist, create new
        if(!newXml.isFile())
            createXml(filepath);

        try {
        // Modify XML using DOM
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(context.openFileInput(filepath));

        // Search for tag and insert into a node object
        NodeList nodes = doc.getElementsByTagName(tag);
        Node detail = nodes.item(0);

        // Get all child tags of our parent
            NodeList details = detail.getChildNodes();
            for(int i = 0; i < details.getLength(); i++){
                Node item = details.item(i);

                // Get all the attributes of the details tag
                NamedNodeMap detailNodes = item.getAttributes();
                for(int z = 0; z < detailNodes.getLength(); z++){
                    Attr attr = (Attr) detailNodes.item(z);
                    String attrV = attr.getValue(); // ------------testing
                    if(attr.getNodeName().equals(attribute))
                        attr.setNodeValue(value);
                    String attrV2 = attr.getValue(); // ------------testing

                    int zzz = 4;    // -----------------------------testing
                    zzz++;          // -----------------------------testing
                }
            }

            // Save all changes
            TransformerFactory tranceFactory = TransformerFactory.newInstance();
            Transformer trance = tranceFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(context.openFileOutput(filepath, context.MODE_PRIVATE));
            trance.transform(source, result);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public void setCode(String value){
        write("Tank", "code", value);
    }

    public void setpassword(String value){
        write("Tank", "password", value);
    }

    public void setSize(float value){
        write("Tank", "size", String.valueOf(value));
    }

    public void setDescription(String value){
        write("Tank", "description", value);
    }

    public void setDateSent(String value){
        write("Date", "dateSent", value);
    }

    public void setPush(boolean value){ write("Settings", "push", String.valueOf(value)); }

    public void setMain(int value){
        write("Settings", "main", String.valueOf(value));
    }

    public void setFeed(boolean value, int id){
        write("Feed", "feed", String.valueOf(value));
    }

    public void setAutoFeed(boolean value){
        write("Feed", "autoFeed", String.valueOf(value));
    }

//    FeedSchedule
//    LightSchedule

    public void setLight(boolean value){
        write("Light", "light", String.valueOf(value));
    }

    public void setAutoLight(boolean value){
        write("Light", "autoLight", String.valueOf(value));
    }

    public void setTempMin(int value){
        write("Temperature", "min", String.valueOf(value));
    }

    public void setTempMax(int value){
        write("Temperature", "max", String.valueOf(value));
    }

    public void setAutoTemp(boolean value){
        write("Temperature", "autoTemp", String.valueOf(value));
    }

    public void setManualFan(boolean value){
        write("Temperature", "manualFan", String.valueOf(value));
    }

    public void setManualHeater(boolean value){
        write("Temperature", "manualHeater", String.valueOf(value));
    }

    public void setDrain(boolean value){
        write("Pump", "drain", String.valueOf(value));
    }

    public void setFill(boolean value){
        write("Pump", "fill", String.valueOf(value));
    }

    public void setAutoWaterChange(boolean value){
        write("Pump", "autoWaterChange", String.valueOf(value));
    }

    public void setPHMin(float value){
        write("Sensor", "pHmin", String.valueOf(value));
    }

    public void setPHMax(float value){
        write("Sensor", "pHmax", String.valueOf(value));
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
            String size = "20";
            String description = "my fishy home";
            String dateSent = "0045-02-03-2017";
            String feed = "false";
            String autoFeed = "false";
            String light = "false";
            String autoLight = "false";
            String minTemp = "16";
            String maxTemp = "22";
            String autoTemp = "false";
            String manualFan = "false";
            String manualHeater = "false";
            String drain = "false";
            String fill = "false";
            String autoWaterChange = "true";
            String pHMin = "1";
            String pHMax = "9";

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
            xmlSerializer.attribute("", "size", size);
            xmlSerializer.attribute("", "description", description);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Tank");

            // Date
            xmlSerializer.startTag("", "Date");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "dateSent", dateSent);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Date");

            // Feed
            xmlSerializer.startTag("", "Feed");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "feed", feed);
            xmlSerializer.attribute("", "autoFeed", autoFeed);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Feed");

            // FeedSchedule
            xmlSerializer.startTag("", "FeedSchedule");
            xmlSerializer.attribute("", "schedules", "3");

            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "hr", "13");
            xmlSerializer.attribute("", "min", "50");
            xmlSerializer.attribute("", "Mon", "true");
            xmlSerializer.attribute("", "Tue", "false");
            xmlSerializer.attribute("", "Wed", "true");
            xmlSerializer.attribute("", "Thu", "true");
            xmlSerializer.attribute("", "Fri", "true");
            xmlSerializer.attribute("", "Sat", "false");
            xmlSerializer.attribute("", "Sun", "false");
            xmlSerializer.endTag("", "details");

            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "hr", "07");
            xmlSerializer.attribute("", "min", "30");
            xmlSerializer.attribute("", "Mon", "false");
            xmlSerializer.attribute("", "Tue", "true");
            xmlSerializer.attribute("", "Wed", "false");
            xmlSerializer.attribute("", "Thu", "true");
            xmlSerializer.attribute("", "Fri", "false");
            xmlSerializer.attribute("", "Sat", "false");
            xmlSerializer.attribute("", "Sun", "false");
            xmlSerializer.endTag("", "details");

            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "hr", "21");
            xmlSerializer.attribute("", "min", "45");
            xmlSerializer.attribute("", "Mon", "false");
            xmlSerializer.attribute("", "Tue", "false");
            xmlSerializer.attribute("", "Wed", "true");
            xmlSerializer.attribute("", "Thu", "true");
            xmlSerializer.attribute("", "Fri", "true");
            xmlSerializer.attribute("", "Sat", "true");
            xmlSerializer.attribute("", "Sun", "false");
            xmlSerializer.endTag("", "details");

            xmlSerializer.endTag("", "FeedSchedule");

            // Light
            xmlSerializer.startTag("", "Light");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "light", light);
            xmlSerializer.attribute("", "autoLight", autoLight);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Light");

            // LightSchedule
            xmlSerializer.startTag("", "LightSchedule");
            xmlSerializer.attribute("", "schedules", "2");

            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "onHr", "09");
            xmlSerializer.attribute("", "onMin", "00");
            xmlSerializer.attribute("", "offHr", "12");
            xmlSerializer.attribute("", "offMin", "30");
            xmlSerializer.endTag("", "details");

            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "onHr", "13");
            xmlSerializer.attribute("", "onMin", "00");
            xmlSerializer.attribute("", "offHr", "18");
            xmlSerializer.attribute("", "offMin", "30");
            xmlSerializer.endTag("", "details");

            xmlSerializer.endTag("", "LightSchedule");

            // Temperature
            xmlSerializer.startTag("", "Temperature");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "min", minTemp);
            xmlSerializer.attribute("", "max", maxTemp);
            xmlSerializer.attribute("", "autoTemp", autoTemp);
            xmlSerializer.attribute("", "manualFan", manualFan);
            xmlSerializer.attribute("", "manualHeater", manualHeater);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Temperature");

            // Pump
            xmlSerializer.startTag("", "Pump");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "drain", drain);
            xmlSerializer.attribute("", "fill", fill);
            xmlSerializer.attribute("", "autoWaterChange", autoWaterChange);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Pump");

            // Sensor
            xmlSerializer.startTag("", "Sensor");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "pHmin", pHMin);
            xmlSerializer.attribute("", "pHmax", pHMax);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Sensor");

            xmlSerializer.endTag("", "Piseas");

            xmlSerializer.endDocument();

            writer.toString();
            myFile.write(writer.toString().getBytes());

            // always remember to close -_-
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}