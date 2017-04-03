package group7.piseas.Objects;

import android.os.AsyncTask;
import android.widget.Toast;

import piseas.network.FishyClient;

/**
 * Created by Van on 2017-04-03.
 */

public class WaterConductivity {
    private boolean auto;
    private float conMax;
    private float conMin;
    private float value;
    private Tank tank;

    public WaterConductivity(Tank tank) {
        this.tank = tank;
        loadLocalXmlData();
    }

    public void loadLocalXmlData() {
        auto = tank.getPiSeasXmlHandler().getSettingsCAuto();
        conMax = tank.getPiSeasXmlHandler().getSettingsCMax();
        conMin = tank.getPiSeasXmlHandler().getSettingsCMin();
        value = tank.getPiSeasXmlHandler().getSensorConductivity();
    }

    public void sendWCSettingsToServer() {
        new WaterConductivity.UpdateWCTask().execute();
    }
    private class UpdateWCTask extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void ... voids) {
            return FishyClient.updateConductivity(tank.getId(),(int) conMin, (int)conMax, auto);
        }

        protected void onPostExecute(Boolean result) {
            if (!result)
                Toast.makeText(tank.getContext(), "No internet connection;  Unable to Water Conductivity settings!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
        tank.getPiSeasXmlHandler().setAutoConductivity(auto);
    }

    public float getConMax() {
        return conMax;
    }

    public void setConMax(float conMax) {
        this.conMax = conMax;
        tank.getPiSeasXmlHandler().setCMax(conMax);
    }

    public float getConMin() {
        return conMin;
    }

    public void setConMin(float conMin) {
        this.conMin = conMin;
        tank.getPiSeasXmlHandler().setCMin(conMin);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
