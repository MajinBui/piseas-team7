package group7.piseas.Objects;

import android.os.AsyncTask;
import android.widget.Toast;

import piseas.network.FishyClient;

/**
 * Created by Van on 2017-03-04.
 */

public class Pump {
    private boolean auto;
    private boolean manualFill;
    private boolean manualDrain;
    private Tank tank;

    public Pump(Tank tank) {
        this.tank = tank;  // Reference to main to keep Id and keep the same XMLParser
        loadLocalXmlData();
    }

    /**
     * Load from local xml.   Should be called before a view that requires the data is loaded.
     */
    public void loadLocalXmlData() {
        auto = tank.getPiSeasXmlHandler().getSettingsAutoWaterChange();
        manualDrain = tank.getPiSeasXmlHandler().getSettingsDrain();
        manualFill = tank.getPiSeasXmlHandler().getSettingsFill();
    }

    /**
     * Send the current PumpSettings to the server based on the values set in the OBJECT, not the xml
     */
    public void sendPumpSettingsToServer() {
        new UpdatePumpTask().execute();
    }


    private class UpdatePumpTask extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void ... voids) {
            return FishyClient.updatePump(tank.getId(), manualDrain, manualFill, auto);
        }

        protected void onPostExecute(Boolean result) {
            if (!result)
                Toast.makeText(tank.getContext(), "No internet connection;  Unable to pump details!", Toast.LENGTH_LONG).show();
        }
    }

    // Setters must set the object attribute and modify the xml locally

    public void setAuto(boolean auto) {
        this.auto = auto;
        tank.getPiSeasXmlHandler().setAutoWaterChange(auto);
    }

    public void setManualFill(boolean manualFill) {
        this.manualFill = manualFill;
        tank.getPiSeasXmlHandler().setFill(manualFill);
    }

    public void setManualDrain(boolean manualDrain) {
        this.manualDrain = manualDrain;
        tank.getPiSeasXmlHandler().setDrain(manualDrain);
    }

    // Getters
    public boolean isAuto() {
        return auto;
    }

    public boolean isManualFill() {
        return manualFill;
    }

    public boolean isManualDrain() {
        return manualDrain;
    }

}
