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


public class XmlPullParserHandler {

    private Context context;

    public XmlPullParserHandler(Context context){
        this.context=context;
    }

    // Retrieve

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
        return parseMobile(is, "Tank", 0);
    }

    public String getPassword(InputStream is){
        // Parse from tag "Tank", attribute 1
        return parseMobile(is, "Tank", 1);
    }

    public String getDateSent(InputStream is){
        // Parse from tag "Date", attribute 0
        return parseMobile(is, "Date", 0);
    }

    public String getTotalFeeds(InputStream is){
        // Parse from tag "Feed", attribute 0
        return parseMobile(is, "Feed", 0);
    }

    public String getFeedTime(InputStream is){
        // Parse from tag "Feed", attribute 1
        return parseMobile(is, "Feed", 1);
    }

    public String getCurrentTemp(InputStream is){
        // Parse from tag "Temperature", attribute 0
        return parseMobile(is, "Temperature", 0);
    }

    public String getPH(InputStream is){
        // Parse from tag "Sensor", attribute 0
        return parseMobile(is, "Sensor", 0);
    }

    public String getConductivity(InputStream is){
        // Parse from tag "Sensor", attribute 1
        return parseMobile(is, "Sensor", 1);
    }

    // Testing purposes only
    private String parsePi(InputStream is, String TAG_NAME, int attribute){
        String code = "-";
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            //is.reset();

            parser.setInput(is, null);

            int event;

            while ((event = parser.next()) != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String tag = parser.getName();
                    if (TAG_NAME.equals(tag)) {
                        parser.nextTag();
                        tag = parser.getName();
                        if(tag.equals("details"))
                            code = parser.getAttributeValue(attribute);
                    }
                }
            }
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch (IOException e) {e.printStackTrace();}

        return code;
    }

    public String getPiCode(InputStream is){
        // Parse from tag "Tank", attribute 0
        return parsePi(is, "Tank", 0);
    }

    public String getPiPassword(InputStream is){
        // Parse from tag "Tank", attribute 1
        return parsePi(is, "Tank", 1);
    }

    public String getPiSize(InputStream is){
        // Parse from tag "Tank", attribute 2
        return parsePi(is, "Tank", 2);
    }

    public String getPiDescription(InputStream is){
        // Parse from tag "Tank", attribute 3
        return parsePi(is, "Tank", 3);
    }

    public String getPiDateSent(InputStream is){
        // Parse from tag "Date", attribute 0
        return parsePi(is, "Date", 0);
    }

    public String getPiPush(InputStream is){
        // Parse from tag "Settings", attribute 0
        return parsePi(is, "Settings", 0);
    }

    public String getPiMain(InputStream is){
        // Parse from tag "Settings", attribute 1
        return parsePi(is, "Settings", 1);
    }

    public String getPiFeed(InputStream is){
        // Parse from tag "Feed", attribute 0
        return parsePi(is, "Feed", 0);
    }

    public String getPiAutoFeed(InputStream is){
        // Parse from tag "Feed", attribute 1
        return parsePi(is, "Feed", 1);
    }

    // FeedSchedule

    // LightSchedule

    public String getPiLight(InputStream is){
        // Parse from tag "Light", attribute 0
        return parsePi(is, "Light", 0);
    }

    public String getPiAutoLight(InputStream is){
        // Parse from tag "Light", attribute 1
        return parsePi(is, "Light", 1);
    }

    public String getPiMin(InputStream is){
        // Parse from tag "Temperature", attribute 0
        return parsePi(is, "Temperature", 0);
    }

    public String getPiMax(InputStream is){
        // Parse from tag "Temperature", attribute 1
        return parsePi(is, "Temperature", 1);
    }

    public String getPiAutoTemp(InputStream is){
        // Parse from tag "Temperature", attribute 2
        return parsePi(is, "Temperature", 2);
    }

    public String getPiManualFan(InputStream is){
        // Parse from tag "Temperature", attribute 3
        return parsePi(is, "Temperature", 3);
    }

    public String getPiManualHeater(InputStream is){
        // Parse from tag "Temperature", attribute 4
        return parsePi(is, "Temperature", 4);
    }

    public String getPiDrain(InputStream is){
        // Parse from tag "Pump", attribute 0
        return parsePi(is, "Pump", 0);
    }

    public String getPiFill(InputStream is){
        // Parse from tag "Pump", attribute 1
        return parsePi(is, "Pump", 1);
    }

    public String getPiAutoWaterChange(InputStream is){
        // Parse from tag "Pump", attribute 2
        return parsePi(is, "Pump", 2);
    }

    public String getPiPHMin(InputStream is){
        // Parse from tag "Sensor", attribute 0
        return parsePi(is, "Sensor", 0);
    }

    public String getPiPHMax(InputStream is){
        // Parse from tag "Sensor", attribute 1
        return parsePi(is, "Sensor", 1);
    }

    // Update
    public void write(String tag, String attribute, String value, int id) {

        String filepath = id + "_pi.xml";
        File newXml = new File(context.getFilesDir() + "/" + filepath);

        // Check to see if file exists, if file does not exist, create new
        if(!newXml.isFile())
            createXml(context.getFilesDir() + filepath);

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
            String push = "true";
            String main = "1";
            String log = "This is the log.";
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

            // Settings
            xmlSerializer.startTag("", "Settings");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "push", push);
            xmlSerializer.attribute("", "main", main);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Settings");

            // Feed
            xmlSerializer.startTag("", "Feed");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "feed", feed);
            xmlSerializer.attribute("", "autoFeed", autoFeed);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Feed");

            // FeedSchedule
            xmlSerializer.startTag("", "FeedSchedule");
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