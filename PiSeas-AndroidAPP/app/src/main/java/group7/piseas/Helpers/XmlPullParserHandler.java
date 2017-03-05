package group7.piseas.Helpers;


import android.content.Context;
import android.util.Xml;
import android.widget.Toast;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import group7.piseas.Objects.FeedSchedule;
import group7.piseas.Objects.LightSchedule;
import group7.piseas.Objects.piDate;
import group7.piseas.Objects.Log.LogDesc;
import group7.piseas.Objects.Log.Logs;
import group7.piseas.Objects.Log.Log;
import piseas.network.FishyClient;

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
    private String parseSensor(String TAG_NAME, String attribute){
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
                        code = parser.getAttributeValue(null, attribute);
                        is.close();
                        return code;
                    }
                }
            }
            is.close();
        } catch (XmlPullParserException e) {e.printStackTrace();}
        catch ( IOException e ) {
            e.printStackTrace();
        }

        return code;
    }

    public String getSensorID(){
        // Parse from tag "Tank", attribute id
        return parseSensor("Tank", "id");
    }

    public String getSensorPassword(){
        // Parse from tag "Tank", attribute password
        return parseSensor("Tank", "password");
    }

    public piDate getSensorDate(){
        // Parse from tag Date, attribute date
        return new piDate(parseSensor("Date", "date"));
    }

    public int getSensorTotalFeeds(){
        // Parse from tag "Feed", attribute totalFeeds
        return Integer.parseInt(parseSensor("Feed", "totalFeeds"));
    }

    public int getSensorFeedHr(){
        // Parse from tag "Feed", attribute feedHr
        return Integer.parseInt(parseSensor("Feed", "feedHr"));
    }

    public int getSensorFeedMin(){
        // Parse from tag "Feed", attribute feedMin
        return Integer.parseInt(parseSensor("Feed", "feedMin"));
    }

    public int getSensorCurrentTemp(){
        // Parse from tag "Temperature", attribute currentTemp
        return Integer.parseInt(parseSensor("Temperature", "currentTemp"));
    }

    public float getSensorPH(){
        // Parse from tag "Sensor", attribute pHcurrent
        return Float.parseFloat(parseSensor("Sensor", "pHcurrent"));
    }

    public float getSensorConductivity(){
        // Parse from tag "Sensor", attribute conductivity
        return Float.parseFloat(parseSensor("Sensor", "conductivity"));
    }

    // Parsing of the tank_<id>_mobile_settings.xml file, file saved in internal storage
    private String parseSettings(String TAG_NAME, String attribute){
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
                        if(TAG_NAME.equals("Feed") || TAG_NAME.equals("Light") || TAG_NAME.equals("Date")) {
                            code = parser.getAttributeValue(null, attribute);
                            fis.close();
                            return code;
                        }
                        // Go to details tag
                        parser.nextTag();
                        tag = parser.getName();
                        if(tag.equals("details")){
                            code = parser.getAttributeValue(null,attribute);
                            fis.close();
                            return code;
                        }
                    }
                }
            }
            fis.close();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch ( IOException e) {
            e.printStackTrace();
        }

        return code;
    }

    public String getSettingsID(){
        // Parse from tag "Tank", attribute id
        return parseSettings("Tank", "id");
    }

    public String getSettingsName() {
        // Parse from tag "Tank", attribute name
        return parseSettings("Tank", "name");
    }

    public String getSettingsPassword(){
        // Parse from tag "Tank", attribute password
        return parseSettings("Tank", "password");
    }

    public float getSettingsSize(){
        // Parse from tag "Tank", attribute size
        return Float.parseFloat(parseSettings("Tank", "size"));
    }

    public String getSettingsDescription(){
        // Parse from tag "Tank", attribute description
        return parseSettings("Tank", "description");
    }

    // Type of fish, cold water or tropical
    // cold water = false, tropical = true
    public Boolean getSettingsType(){
        // Parse from tag "Tank", attribute type
        return Boolean.valueOf(parseSettings("Tank", "type"));
    }

    public piDate getSettingsDate(){
        // Parse from tag "Date", attribute date
        return new piDate(parseSettings("Date", "date"));
    }

    public boolean getSettingsManualFeed(){
        // Parse from tag "Feed", attribute manual feed
        return Boolean.parseBoolean(parseSettings("Feed", "manual"));
    }

    public boolean getSettingsAutoFeed(){
        // Parse from tag "Feed", attribute auto feed
        return Boolean.parseBoolean(parseSettings("Feed", "auto"));
    }

    public boolean getSettingsManualLight(){
        // Parse from tag "Light", attribute manual light
        return Boolean.parseBoolean(parseSettings("Light", "manual"));
    }

    public boolean getSettingsAutoLight(){
        // Parse from tag "Light", attribute auto light
        return Boolean.parseBoolean(parseSettings("Light", "auto"));
    }

    public float getSettingsMinTemp(){
        // Parse from tag "Temperature", attribute min temp
        return Float.parseFloat(parseSettings("Temperature", "min"));
    }

    public float getSettingsMaxTemp(){
        // Parse from tag "Temperature", attribute max temp
        return Float.parseFloat(parseSettings("Temperature", "max"));
    }

    public boolean getSettingsDrain(){
        // Parse from tag "Pump", attribute manualDrain
        return Boolean.parseBoolean(parseSettings("Pump", "manualDrain"));
    }

    public boolean getSettingsFill(){
        // Parse from tag "Pump", attribute manualFill
        return Boolean.parseBoolean(parseSettings("Pump", "manualFill"));
    }

    public boolean getSettingsAutoWaterChange(){
        // Parse from tag "Pump", attribute auto water change
        return Boolean.parseBoolean(parseSettings("Pump", "auto"));
    }

    public float getSettingsPHMin(){
        // Parse from tag "Sensor", attribute phMin
        return Float.parseFloat(parseSettings("PH", "pHmin"));
    }

    public float getSettingsPHMax(){
        // Parse from tag "Sensor", attribute phMax
        return Float.parseFloat(parseSettings("PH", "pHmax"));
    }

    public boolean getSettingsPHAuto(){
        // Parse from tag "PH", attribute auto
        return Boolean.parseBoolean(parseSettings("PH", "auto"));
    }

    public float getSettingsCMin() {
        // Parse from tag "Conductivity", attribute cMin
        return Float.parseFloat(parseSettings("Conductivity", "cMin"));
    }

    public float getSettingsCMax() {
        // Parse from tag "Conductivity", attribute cMax
        return Float.parseFloat(parseSettings("Conductivity", "cMax"));
    }

    public boolean getSettingsCAuto(){
        // Parse from tag "Conductivity", attribute auto
        return Boolean.parseBoolean(parseSettings("Conductivity", "auto"));
    }

    public ArrayList<FeedSchedule> getFeedSchedules(){
        ArrayList<FeedSchedule> feeds = new ArrayList<FeedSchedule>();
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
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch ( IOException e) {
            e.printStackTrace();
        }

        return feeds;
    }

    public ArrayList<LightSchedule> getLightSchedules(){
        ArrayList<LightSchedule> lights = new ArrayList<LightSchedule>();
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
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch ( IOException e) {
            e.printStackTrace();
        }
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

                while(!parser.getName().equals("Log")){
                    if(parser.getName().equals("Date"))
                        logs.timeStamp(parser.getAttributeValue(0));
                    parser.nextTag();
                }
                tag = parser.getName();
                while(parser.getName().equals("Log")) {

                    logs.add(new Log(
                            parser.getAttributeValue(null, "date"),
                            parser.getAttributeValue(null, "type"),
                            LogDesc.fromString(parser.getAttributeValue(null, "decs"))
                    ));
                    // next will take the same detail tag twice, I think because it needs a closing
                    // tag to go with the opening one. next() twice to get to the actual next tag
                    parser.nextTag();
                    parser.nextTag();
                }
                is.close();
                return logs;
            }
            is.close();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch ( IOException e) {
            e.printStackTrace();
        }
        return logs;
    }

    // Update data
    private void write(String tag, String attribute, String value) {

        String filepath = id + "_mobile_settings.xml";
        File newXml = new File(context.getFilesDir() + "/" + filepath);

        //boolean del = newXml.delete();              // TESTING ONLY, DELETES EXISTING XML IN STORAGE

        // Check to see if file exists, if file does not exist, create new
        if(!newXml.isFile())
            createXml(filepath);

        try {
            // Modify XML using DOM
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(context.openFileInput(filepath));

            // This segment of code clears all indents of the xml file
            // Required since the xml used by the server side preserves indents
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPathExpression xpathExp = xpathFactory.newXPath().compile(
                    "//text()[normalize-space(.) = '']");
            NodeList emptyTextNodes = (NodeList)
                    xpathExp.evaluate(doc, XPathConstants.NODESET);

            // Remove each empty text node from document.
            for (int i = 0; i < emptyTextNodes.getLength(); i++) {
                Node emptyTextNode = emptyTextNodes.item(i);
                emptyTextNode.getParentNode().removeChild(emptyTextNode);
            }
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


            for (int i = 0; i < 6; i++) {
                Attr attribs = (Attr) allAtts.item(i);
                if (attribs.getNodeName().toLowerCase().equals(tag))
                    attribs.setNodeValue("true");
                else
                    attribs.setNodeValue("false");
            }

            // Search for tag and insert into a node object
            nodes = doc.getElementsByTagName(tag);
            detail = nodes.item(0);

            if(tag.equals("Date")){
                NamedNodeMap detailNodes = detail.getAttributes();
                Attr attr = (Attr) detailNodes.item(0);
                if (attr.getNodeName().equals(attribute))
                    attr.setNodeValue(value);
            }
            else {
                // Get all child tags of our parent
                details = detail.getChildNodes();
                for (int i = 0; i < details.getLength(); i++) {
                    Node item = details.item(i);

                    // Get all the attributes of the details tag
                    NamedNodeMap detailNodes = item.getAttributes();
                    for (int z = 0; z < detailNodes.getLength(); z++) {
                        Attr attr = (Attr) detailNodes.item(z);
                        if (attr.getNodeName().equals(attribute))
                            attr.setNodeValue(value);
                    }
                }
            }

            // Save all changes
            TransformerFactory tranceFactory = TransformerFactory.newInstance();
            Transformer trance = tranceFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(context.openFileOutput(filepath, Context.MODE_PRIVATE));  // context.MODE_PRIVATE
            trance.transform(source, result);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch ( SAXException e) {
            e.printStackTrace();
        } catch ( TransformerException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public void setID(String value){
        write("Tank", "id", value);
    }

    public void setName(String value) { write("Tank", "name", value); }

    public void setPassword(String value){
        write("Tank", "password", value);
    }

    public void setSize(float value){
        write("Tank", "size", String.valueOf(value));
    }

    public void setDescription(String value){
        write("Tank", "description", value);
    }

    public void setType(Boolean value){
        write("Tank", "type", String.valueOf(value));
    }

    public void setDateSent(String value){
        write("Date", "date", value);
    }

    public void setFeed(ArrayList<FeedSchedule> feed, boolean manual, boolean auto) {
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
            // This segment of code clears all indents of the xml file
            // Required since the xml used by the server side preserves indents
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPathExpression xpathExp = xpathFactory.newXPath().compile(
                    "//text()[normalize-space(.) = '']");
            NodeList emptyTextNodes = (NodeList)
                    xpathExp.evaluate(doc, XPathConstants.NODESET);

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
            // loop through all attributes, set feed to true while all others are false
            for (int i = 0; i < 6; i++) {
                Attr attribs = (Attr) allAtts.item(i);
                if (attribs.getNodeName().toLowerCase().equals("feed"))
                    attribs.setNodeValue("true");
                else
                    attribs.setNodeValue("false");
            }

            // Search for tag and insert into a node object
            nodes = doc.getElementsByTagName("Feed");
            detail = nodes.item(0);

            // Fill in Feed tag attributes
            NamedNodeMap detailNodes = detail.getAttributes();
            for (int z = 0; z < detailNodes.getLength(); z++) {
                Attr attr = (Attr) detailNodes.item(z);
                if (attr.getNodeName().equals("schedules")) {
                    attr.setNodeValue(String.valueOf(feed.size()));
                } else if (attr.getNodeName().equals("manual")) {
                    attr.setNodeValue(String.valueOf(manual));
                } else if (attr.getNodeName().equals("auto")) {
                    attr.setNodeValue(String.valueOf(auto));
                } else {
                    Toast.makeText(context, "I am not suppose to be here! setFeed loop 1", Toast.LENGTH_LONG).show();
                }
            }
            // fill in details
            details = detail.getChildNodes();
            for (int i = 0; i < details.getLength(); i++) {
                Node item = details.item(i);

                // Get all the attributes of the details tag
                detailNodes = item.getAttributes();
                for (int z = 0; z < detailNodes.getLength(); z++) {
                    Attr attr = (Attr) detailNodes.item(z);
                    if (attr.getNodeName().equals("hr")) {
                        attr.setNodeValue(String.valueOf(feed.get(i).getHour()));
                    } else if (attr.getNodeName().equals("min")) {
                        attr.setNodeValue(String.valueOf(feed.get(i).getMin()));
                    } else if (attr.getNodeName().equals("Mon")) {
                        attr.setNodeValue(String.valueOf(feed.get(i).getWeek(0)));
                    } else if (attr.getNodeName().equals("Tue")) {
                        attr.setNodeValue(String.valueOf(feed.get(i).getWeek(1)));
                    } else if (attr.getNodeName().equals("Wed")) {
                        attr.setNodeValue(String.valueOf(feed.get(i).getWeek(2)));
                    } else if (attr.getNodeName().equals("Thu")) {
                        attr.setNodeValue(String.valueOf(feed.get(i).getWeek(3)));
                    } else if (attr.getNodeName().equals("Fri")) {
                        attr.setNodeValue(String.valueOf(feed.get(i).getWeek(4)));
                    } else if (attr.getNodeName().equals("Sat")) {
                        attr.setNodeValue(String.valueOf(feed.get(i).getWeek(5)));
                    } else if (attr.getNodeName().equals("Sun")) {
                        attr.setNodeValue(String.valueOf(feed.get(i).getWeek(6)));
                    } else {
                        Toast.makeText(context, "I am not suppose to be here! setFeed loop 1", Toast.LENGTH_LONG).show();
                    }
                }
            }

            // Save all changes
            TransformerFactory tranceFactory = TransformerFactory.newInstance();
            Transformer trance = tranceFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(context.openFileOutput(filepath, Context.MODE_PRIVATE));  // context.MODE_PRIVATE
            trance.transform(source, result);

        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch ( SAXException e) {
            e.printStackTrace();
        } catch ( TransformerException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public void setLight(ArrayList<LightSchedule> light, boolean manual, boolean auto) {
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

            // This segment of code clears all indents of the xml file
            // Required since the xml used by the server side preserves indents
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPathExpression xpathExp = xpathFactory.newXPath().compile(
                    "//text()[normalize-space(.) = '']");
            NodeList emptyTextNodes = (NodeList)
                    xpathExp.evaluate(doc, XPathConstants.NODESET);

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
            // loop through all attributes, set feed to true while all others are false
            for (int i = 0; i < 6; i++) {
                Attr attribs = (Attr) allAtts.item(i);
                if (attribs.getNodeName().toLowerCase().equals("feed"))
                    attribs.setNodeValue("true");
                else
                    attribs.setNodeValue("false");
            }

            // Search for tag and insert into a node object
            nodes = doc.getElementsByTagName("Light");
            detail = nodes.item(0);

            // Fill in Feed tag attributes
            NamedNodeMap detailNodes = detail.getAttributes();
            for (int z = 0; z < detailNodes.getLength(); z++) {
                Attr attr = (Attr) detailNodes.item(z);
                if (attr.getNodeName().equals("schedules")) {
                    attr.setNodeValue(String.valueOf(light.size()));
                } else if (attr.getNodeName().equals("manual")) {
                    attr.setNodeValue(String.valueOf(manual));
                } else if (attr.getNodeName().equals("auto")) {
                    attr.setNodeValue(String.valueOf(auto));
                } else {
                    Toast.makeText(context, "I am not suppose to be here! setLight loop 1", Toast.LENGTH_LONG).show();
                }
            }
            // fill in details
            details = detail.getChildNodes();
            for (int i = 0; i < details.getLength(); i++) {
                Node item = details.item(i);

                // Get all the attributes of the details tag
                detailNodes = item.getAttributes();
                for (int z = 0; z < detailNodes.getLength(); z++) {
                    Attr attr = (Attr) detailNodes.item(z);
                    if (attr.getNodeName().equals("onHr")) {
                        attr.setNodeValue(String.valueOf(light.get(i).getOnHour()));
                    } else if (attr.getNodeName().equals("onMin")) {
                        attr.setNodeValue(String.valueOf(light.get(i).getOnHour()));
                    } else if (attr.getNodeName().equals("offHr")) {
                        attr.setNodeValue(String.valueOf(light.get(i).getOffHour()));
                    } else if (attr.getNodeName().equals("offMin")) {
                        attr.setNodeValue(String.valueOf(light.get(i).getOffMin()));
                    } else {
                        Toast.makeText(context, "I am not suppose to be here! setLight loop 2", Toast.LENGTH_LONG).show();
                    }
                }
            }

            // Save all changes
            TransformerFactory tranceFactory = TransformerFactory.newInstance();
            Transformer trance = tranceFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(context.openFileOutput(filepath, Context.MODE_PRIVATE));  // context.MODE_PRIVATE
            trance.transform(source, result);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch ( SAXException e) {
            e.printStackTrace();
        } catch ( TransformerException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
    }

    public void setMinTemp(int value){
        write("Temperature", "min", String.valueOf(value));
    }

    public void setMaxTemp(int value){
        write("Temperature", "max", String.valueOf(value));
    }

    // Keeping or no? ------------------------------------------------------------------------------------------------------------
    public void setAutoTemp(boolean value){
        write("Temperature", "auto", String.valueOf(value));
    }

    public void setDrain(boolean value){
        write("Pump", "manualDrain", String.valueOf(value));
    }

    public void setFill(boolean value){
        write("Pump", "manualFill", String.valueOf(value));
    }

    public void setAutoWaterChange(boolean value){
        write("Pump", "auto", String.valueOf(value));
    }

    public void setPHMin(float value){
        write("Sensor", "pHmin", String.valueOf(value));
    }

    public void setPHMax(float value){
        write("Sensor", "pHmax", String.valueOf(value));
    }

    public void setAutoPH(boolean value){
        write("PH", "auto", String.valueOf(value));
    }

    public void setCMin(float value){
        write("Conductivity", "cMin", String.valueOf(value));
    }

    public void setCMax(float value){
        write("Conductivity", "cMax", String.valueOf(value));
    }

    public void setAutoConductivity(boolean value){
        write("Conductivity", "auto", String.valueOf(value));
    }

    // Create new xml file with default values incase there is no #_mobile_settings.xml
    // used mostly for testing
    private void createXml(String filename){
        try {
            FileOutputStream myFile = context.openFileOutput(filename, Context.MODE_PRIVATE); //context.MODE_PRIVATE

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
            String type = "true";
            String dateSent = "2017-02-20T19:19:19+0500";
            String feed = "false";
            String autoFeed = "false";
            String light = "false";
            String autoLight = "false";
            String minTemp = "16";
            String maxTemp = "22";
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
            xmlSerializer.attribute("", "date", dateSent);
            xmlSerializer.endTag("", "Date");

            // Tank
            xmlSerializer.startTag("", "Tank");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "id", code);
            xmlSerializer.attribute("", "name", code);
            xmlSerializer.attribute("", "password", password);
            xmlSerializer.attribute("", "size", size);
            xmlSerializer.attribute("", "description", description);
            xmlSerializer.attribute("", "type", type);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Tank");

            // Feed
            xmlSerializer.startTag("", "Feed");
            xmlSerializer.attribute("", "schedules", "3");
            xmlSerializer.attribute("", "manual", feed);
            xmlSerializer.attribute("", "auto", autoFeed);

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
            xmlSerializer.attribute("", "manual", light);
            xmlSerializer.attribute("", "auto", autoLight);
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
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Temperature");

            // Pump
            xmlSerializer.startTag("", "Pump");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "manualDrain", drain);
            xmlSerializer.attribute("", "manualFill", fill);
            xmlSerializer.attribute("", "auto", autoWaterChange);
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "Pump");

            // PH
            xmlSerializer.startTag("", "PH");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "pHmin", pHMin);
            xmlSerializer.attribute("", "pHmax", pHMax);
            xmlSerializer.attribute("", "auto", "true");
            xmlSerializer.endTag("", "details");
            xmlSerializer.endTag("", "PH");

            // Conductivity
            xmlSerializer.startTag("", "Conductivity");
            xmlSerializer.startTag("", "details");
            xmlSerializer.attribute("", "cMin", cMin);
            xmlSerializer.attribute("", "cMax", cMax);
            xmlSerializer.attribute("", "auto", "true");
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