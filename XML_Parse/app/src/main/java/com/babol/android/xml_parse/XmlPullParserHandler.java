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
import Objects.LogEnum;
import Objects.Logs;


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
    private String parseSensor(String TAG_NAME, int attribute){
        String code = "-";
        try{
            is = context.getAssets().open(id + "_sensor_data.xml");
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

    public String getSensorCode(){
        // Parse from tag "Tank", attribute 0
        return parseSensor("Tank", 0);
    }

    public String getSensorPassword(){
        // Parse from tag "Tank", attribute 1
        return parseSensor("Tank", 1);
    }

    public piDate getSensorDate(){
        // Parse from tag Date, attribute 0
        return new piDate(parseSensor("Date", 0));
    }

    public int getSensorTotalFeeds(){
        // Parse from tag "Feed", attribute 0
        return Integer.parseInt(parseSensor("Feed", 0));
    }

    public int getSensorFeedTime(){
        // Parse from tag "Feed", attribute 1
        return Integer.parseInt(parseSensor("Feed", 1));
    }

    public int getSensorCurrentTemp(){
        // Parse from tag "Temperature", attribute 0
        return Integer.parseInt(parseSensor("Temperature", 0));
    }

    public float getSensorPH(){
        // Parse from tag "Sensor", attribute 0
        return Float.parseFloat(parseSensor("Sensor", 0));
    }

    public float getSensorConductivity(){
        // Parse from tag "Sensor", attribute 1
        return Float.parseFloat(parseSensor("Sensor", 1));
    }

    // Parsing of the tank_<id>_pi.xml file, file saved in internal storage
    private String parseSettings(String TAG_NAME, int attribute){
        String code = "-";
        try{
            File file = new File(context.getFilesDir() + "/" + id + "_mobile_settings.xml");
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
                        if(TAG_NAME.equals("Feed") || TAG_NAME.equals("Light")) {
                            code = parser.getAttributeValue(attribute);
                            fis.close();
                            return code;
                        }
                        // Go to details tag
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

    public String getSettingsCode(){
        // Parse from tag "Tank", attribute 0
        return parseSettings("Tank", 0);
    }

    public String getSettingsPassword(){
        // Parse from tag "Tank", attribute 1
        return parseSettings("Tank", 1);
    }

    public float getSettingsSize(){
        // Parse from tag "Tank", attribute 2
        return Float.parseFloat(parseSettings("Tank", 2));
    }

    public String getSettingsDescription(){
        // Parse from tag "Tank", attribute 3
        return parseSettings("Tank", 3);
    }

    public piDate getSettingsDate(){
        // Parse from tag "Date", attribute 0
        return new piDate(parseSensor("Date", 0));
    }

    public boolean getSettingsFeed(){
        // Parse from tag "Feed", attribute 1
        return Boolean.parseBoolean(parseSettings("Feed", 1));
    }

    public boolean getSettingsAutoFeed(){
        // Parse from tag "Feed", attribute 2
        return Boolean.parseBoolean(parseSettings("Feed", 2));
    }

    public boolean getSettingsLight(){
        // Parse from tag "Light", attribute 1
        return Boolean.parseBoolean(parseSettings("Light", 1));
    }

    public boolean getSettingsAutoLight(){
        // Parse from tag "Light", attribute 2
        return Boolean.parseBoolean(parseSettings("Light", 2));
    }

    public float getSettingsMin(){
        // Parse from tag "Temperature", attribute 0
        return Float.parseFloat(parseSettings("Temperature", 0));
    }

    public float getSettingsMax(){
        // Parse from tag "Temperature", attribute 1
        return Float.parseFloat(parseSettings("Temperature", 1));
    }

    public boolean getSettingsAutoTemp(){
        // Parse from tag "Temperature", attribute 2
        return Boolean.parseBoolean(parseSettings("Temperature", 2));
    }

    public boolean getSettingsManualFan(){
        // Parse from tag "Temperature", attribute 3
        return Boolean.parseBoolean(parseSettings("Temperature", 3));
    }

    public boolean getSettingsManualHeater(){
        // Parse from tag "Temperature", attribute 4
        return Boolean.parseBoolean(parseSettings("Temperature", 4));
    }

    public boolean getSettingsDrain(){
        // Parse from tag "Pump", attribute 0
        return Boolean.parseBoolean(parseSettings("Pump", 0));
    }

    public boolean getSettingsFill(){
        // Parse from tag "Pump", attribute 1
        return Boolean.parseBoolean(parseSettings("Pump", 1));
    }

    public boolean getSettingsAutoWaterChange(){
        // Parse from tag "Pump", attribute 2
        return Boolean.parseBoolean(parseSettings("Pump", 2));
    }

    public float getSettingsPHMin(){
        // Parse from tag "Sensor", attribute 0
        return Float.parseFloat(parseSettings("Sensor", 0));
    }

    public float getSettingsPHMax(){
        // Parse from tag "Sensor", attribute 1
        return Float.parseFloat(parseSettings("Sensor", 1));
    }

    public float getSettingsCMin() {
        // Parse from tag "Conductivity", attribute 0
        return Float.parseFloat(parseSettings("Conductivity", 0));
    }

    public float getSettingsCMax() {
        // Parse from tag "Conductivity", attribute 1
        return Float.parseFloat(parseSettings("Conductivity", 1));
    }

    public ArrayList<FeedSchedule> getFeedSchedules(){
        ArrayList<FeedSchedule> feeds = new ArrayList<>();
        try{
            File file = new File(context.getFilesDir() + "/" + id + "_mobile_settings.xml");
            FileInputStream fis = new FileInputStream(file);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(fis, null);

            while ((parser.next()) != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();
                if (tag.equals("Feed")) {
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
                                feed.setWeek(i-2);
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
            File file = new File(context.getFilesDir() + "/" + id + "_mobile_settings.xml");
            FileInputStream fis = new FileInputStream(file);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(fis, null);

            while ((parser.next()) != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();
                if (tag.equals("Light")) {
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

    // Parsing of the tank_<id>_action_log.xml file, file saved in Assets
    public Logs getLogs(){
        Logs logs = new Logs();
        try{
            is = context.getAssets().open(id + "_action_log.xml");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            is.reset();
            parser.setInput(is, null);

            while ((parser.next()) != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();

                parser.nextTag();
                tag = parser.getName();
                while(parser.getName().equals("Log")) {
                    logs.add(
                            LogEnum.valueOf(parser.getAttributeValue(0)),
                             new piDate(parser.getAttributeValue(1)));
                    // next will take the same detail tag twice, I think because it needs a closing
                    // tag to go with the opening one. next() twice to get to the actual next tag
                    parser.nextTag();
                    parser.nextTag();
                }
                is.close();
                return logs;
            }
            is.close();
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}
        return logs;
    }

    // Update data
    private void write(String tag, String attribute, String value) {

        String filepath = id + "_mobile_settings.xml";
        File newXml = new File(context.getFilesDir() + "/" + filepath);

        // Check to see if file exists, if file does not exist, create new
        if(!newXml.isFile())
            createXml(filepath);

        try {
            // Modify XML using DOM
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(context.openFileInput(filepath));

            // search for Updated tag
            NodeList nodes = doc.getElementsByTagName("Update");
            // get the Updated tag
            Node detail = nodes.item(0);
            // get the only child of Updated, detail, and place into array
            NodeList details = detail.getChildNodes();
            // get the single detail tag and place into Node
            Node checks = details.item(0);
            // get all attributes into Attr array
            NamedNodeMap allAtts = checks.getAttributes();
            // loop through all attributes, set the one that we are changing to true, all else are false
            for (int i = 0; i<6; i++){
                Attr attribs = (Attr)allAtts.item(i);
                if(attribs.getNodeName().toLowerCase().equals(tag))
                    attribs.setNodeValue("true");
                else
                    attribs.setNodeValue("false");
            }

            // Search for tag and insert into a node object
            nodes = doc.getElementsByTagName(tag);
            detail = nodes.item(0);

            // Get all child tags of our parent
            details = detail.getChildNodes();
            for(int i = 0; i < details.getLength(); i++){
                Node item = details.item(i);

                // Get all the attributes of the details tag
                NamedNodeMap detailNodes = item.getAttributes();
                for(int z = 0; z < detailNodes.getLength(); z++){
                    Attr attr = (Attr) detailNodes.item(z);
                    if(attr.getNodeName().equals(attribute))
                        attr.setNodeValue(value);
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

//    FeedSchedule  - need to implement
//    LightSchedule - need to implement

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

    public void setCMin(float value){
        write("Conductivity", "cmin", String.valueOf(value));
    }

    public void setCMax(float value){
        write("Conductivity", "cmax", String.valueOf(value));
    }

    // Create new xml file with default values incase there is no #_mobile_settings.xml
    // used mostly for testing
    private void createXml(String filename){
        try {
            FileOutputStream myFile = context.openFileOutput(filename, context.MODE_PRIVATE);

            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();

            // Initial data to fill into new XML file - temporary
            String updateFeed = "false";
            String updateLight = "false";
            String updateTemp = "false";
            String updatePump = "false";
            String updatepH = "false";
            String updateCond = "false";
            String code = "abc123";
            String password = "helloworld";
            String size = "20";
            String description = "my fishy home";
            String dateSent = "2017-02-20T19:19:19+0500";
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
            String cMin = "1.5";
            String cMax = "7.5";

            xmlSerializer.setOutput(writer);
            // start document
            xmlSerializer.startDocument("UTF-8", true);
            // open tags and insert attributes into each individual
            xmlSerializer.startTag("", "Piseas");

            // Update
            xmlSerializer.startTag("", "Update");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "feed", updateFeed);
            xmlSerializer.attribute("", "light", updateLight);
            xmlSerializer.attribute("", "temperature", updateTemp);
            xmlSerializer.attribute("", "pump", updatePump);
            xmlSerializer.attribute("", "pH", updatepH);
            xmlSerializer.attribute("", "conductivity", updateCond);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Update");

            // Date
            xmlSerializer.startTag("", "Date");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "date", dateSent);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Date");

            // Tank
            xmlSerializer.startTag("", "Tank");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "code", code);
            xmlSerializer.attribute("", "password", password);
            xmlSerializer.attribute("", "size", size);
            xmlSerializer.attribute("", "description", description);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Tank");

            // Feed
            xmlSerializer.startTag("", "Feed");
            xmlSerializer.attribute("", "schedules", "3");
            xmlSerializer.attribute("", "feed", feed);
            xmlSerializer.attribute("", "autoFeed", autoFeed);

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

            xmlSerializer.endTag("", "Feed");

            // Light
            xmlSerializer.startTag("", "Light");
            xmlSerializer.attribute("", "schedules", "2");
            xmlSerializer.attribute("", "light", light);
            xmlSerializer.attribute("", "autoLight", autoLight);
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
            xmlSerializer.endTag("", "Light");

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

            // Conductivity
            xmlSerializer.startTag("", "Conductivity");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "cMin", cMin);
            xmlSerializer.attribute("", "cMax", cMax);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Conductivity");

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