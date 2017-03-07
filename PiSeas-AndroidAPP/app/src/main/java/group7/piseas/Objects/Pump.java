package group7.piseas.Objects;

import android.content.Context;
import android.os.AsyncTask;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import group7.piseas.Helpers.XmlPullParserHandler;
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
        this.tank = tank;
        loadLocalXmlData();
    }

    // Load all local data
    public void loadLocalXmlData() {
        FishyClient.retrieveMobileXmlData(tank.getId(), tank.getContext().getFilesDir().getAbsolutePath());
        auto = tank.getPiSeasXmlHandler().getSettingsAutoWaterChange();
        manualDrain = tank.getPiSeasXmlHandler().getSettingsDrain();
        manualFill = tank.getPiSeasXmlHandler().getSettingsFill();
    }

    // Save to server
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
