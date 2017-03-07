package group7.piseas.Objects;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

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

    /**
     * Constructor for newly added tanks.
     * @param context
     * @param id
     * @param pw
     * @param name
     * @param type
     * @param size
     * @param desc
     */
    public Tank(Context context, String id, String pw, String name, int type, int size, String desc){
        FishyClient.retrieveMobileXmlData(id, context.getFilesDir().getAbsolutePath());
        this.context = context;
        this.piSeasXmlHandler = new XmlPullParserHandler(context, id);

        this.id = id;
        this.pw = pw;
        this.name = name;
        this.type = type;
        this.size = size;
        this.desc = desc;

        this.pump = new Pump(this);
    }

    /**
     * Constructor used to create values only based off of xml data.
     * @param context
     */
    public Tank(Context context, String id) {
        FishyClient.retrieveMobileXmlData(id, context.getFilesDir().getAbsolutePath());
        this.context = context;
        this.piSeasXmlHandler = new XmlPullParserHandler(context, id);

        this.id = id;
        this.pw = piSeasXmlHandler.getSettingsPassword();
        this.name = piSeasXmlHandler.getSettingsName();
        this.type = (piSeasXmlHandler.getSettingsType()? 1 : 0); //TODO: Boolean type
        this.size = piSeasXmlHandler.getSettingsSize();
        this.desc = piSeasXmlHandler.getSettingsDescription();

        this.pump = new Pump(this);
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

    // References to the tank are available everywhere(tanklist), but the associated pump isn't
    // Updating specific data should be tasked to the class itself
    public Pump getPump() {
        return this.pump;
    }

    //
    public void updatePump() {
        pump.sendPumpSettingsToServer();
    }

    // Not used yet
    @Override
    public void run() {
        FishyClient.retrieveMobileXmlData(id, context.getFilesDir().getAbsolutePath());
        // Retrieve sensor data
    }

    public void sendTankDetailsToServer() {
        new UpdateTankDetailsTask().execute();
    }

    // Asyc tasks used to seamlessly update the xml info without the user feel like they are waiting.
    private class UpdateTankDetailsTask extends AsyncTask<Void, Void, Boolean> {
        // Do in background
        protected Boolean doInBackground(Void ... voids) {
          return FishyClient.updateTankDetailsMobileSettings(id, name, pw, size, desc, (type == 1));
        }
        // Report error if no connection
        protected void onPostExecute(Boolean result) {
            if (!result)
                Toast.makeText(context, "No internet connection;  Unable to update!", Toast.LENGTH_LONG).show();
        }
    }


    public XmlPullParserHandler getPiSeasXmlHandler() {
        return piSeasXmlHandler;
    }

    public Context getContext() {
        return context;
    }
}
