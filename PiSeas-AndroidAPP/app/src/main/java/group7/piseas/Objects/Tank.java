package group7.piseas.Objects;

import android.content.Context;
import android.os.Parcelable;

import group7.piseas.Helpers.XmlPullParserHandler;
import piseas.network.FishyClient;



/**
 * Created by Sallie on 06/12/2016.
 */
public class Tank implements Runnable {
    private Context context;
    private String id;
    private String pw;
    private String name;
    private int type;
    private float size;
    private String desc;
    private XmlPullParserHandler piSeasXmlHandler;
    private PH pH;
    private Pump pump;

    public Tank(Context context, String id, String pw, String name, int type, int size, String desc){
        this.context = context;
        FishyClient.retrieveMobileXmlData(id, context.getFilesDir().getAbsolutePath());
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.type = type;
        this.size = size;
        this.desc = desc;
        this.piSeasXmlHandler = new XmlPullParserHandler(context, id);
    }

    /**
     * Constructor used to create values only based off of xml data.
     * @param context
     */
    public Tank(Context context, String id) {
        android.util.Log.d("parse: ", context.getFilesDir().getAbsolutePath());
        FishyClient.retrieveMobileXmlData(id, context.getFilesDir().getAbsolutePath());
        this.piSeasXmlHandler = new XmlPullParserHandler(context, id);
        this.context = context;
        this.id = id;
        this.pw = piSeasXmlHandler.getSettingsPassword();
        this.name = ""; // TODO: xml must save name
        this.type = (piSeasXmlHandler.getSettingsType()? 1 : 0); //TODO: change?  is it supposed to be boolean?
        this.size = piSeasXmlHandler.getSettingsSize();
        this.desc = piSeasXmlHandler.getSettingsDescription();
        this.pump = new Pump(piSeasXmlHandler);
    }

    public String getId() {
        return id;
    }

    public float getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setType(int type) {
        this.type = type;
    }


    public Pump getPump() {
        return this.pump;
    }

    public void updatePump() {
        pump.saveXmlData(piSeasXmlHandler);
        FishyClient.updatePump(id, pump.isManualDrain(), pump.isManualFill(), pump.isAuto());
        FishyClient.retrieveMobileXmlData(id, context.getFilesDir().getAbsolutePath());
    }
    @Override
    public void run() {
        FishyClient.retrieveMobileXmlData(id, context.getFilesDir().getAbsolutePath());
    }

    public void updateTankDetails() {
        FishyClient.updateTankDetailsMobileSettings(id, pw, size, desc, (type == 1));
    }

}
