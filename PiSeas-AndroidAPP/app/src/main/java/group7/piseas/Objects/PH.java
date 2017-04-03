package group7.piseas.Objects;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import group7.piseas.Helpers.NotificationHelper;
import group7.piseas.Interfaces.TankRunnable;
import group7.piseas.TemperatureManagementActivity;
import piseas.network.FishyClient;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Van on 2016-12-06.
 */

public class PH{
    private boolean auto;
    private float pHMax;
    private float pHMin;
    private float value;
    private Tank tank;

    public PH(Tank tank) {
        this.tank = tank;  // Reference to main to keep Id and keep the same XMLParser
        loadLocalXmlData();
    }

    public void loadLocalXmlData() {
        auto = tank.getPiSeasXmlHandler().getSettingsPHAuto();
        pHMax = tank.getPiSeasXmlHandler().getSettingsPHMax();
        pHMin = tank.getPiSeasXmlHandler().getSettingsPHMin();
        value = tank.getPiSeasXmlHandler().getSensorPH();
    }

    public void sendPHSettingsToServer() {
        new PH.UpdatePHTask().execute();
    }


    private class UpdatePHTask extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void ... voids) {
            return FishyClient.updatePh(tank.getId(), pHMin, pHMax, auto);
        }

        protected void onPostExecute(Boolean result) {
            if (!result)
                Toast.makeText(tank.getContext(), "No internet connection;  Unable to pH settings!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
        tank.getPiSeasXmlHandler().setAutoPH(auto);
    }

    public float getpHMax() {
        return pHMax;
    }

    public void setpHMax(float pHMax) {
        this.pHMax = pHMax;
        tank.getPiSeasXmlHandler().setPHMax(pHMax);
    }

    public float getpHMin() {
        return pHMin;
    }

    public void setpHMin(float pHMin) {
        this.pHMin = pHMin;
        tank.getPiSeasXmlHandler().setPHMin(pHMin);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
