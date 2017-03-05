package group7.piseas.Objects;

import android.content.Context;

import group7.piseas.Helpers.XmlPullParserHandler;
/**
 * Created by Van on 2017-03-04.
 */

public class Pump {
    private boolean auto;
    private boolean manualFill;
    private boolean manualDrain;

    public Pump(XmlPullParserHandler piSeasXmlHandler) {
        loadXmlData(piSeasXmlHandler);
    }

    public void loadXmlData(XmlPullParserHandler piSeasXmlHandler) {
        auto = piSeasXmlHandler.getSettingsAutoWaterChange();
        manualDrain = piSeasXmlHandler.getSettingsDrain();
        manualFill = piSeasXmlHandler.getSettingsFill();
    }

    public void saveXmlData(XmlPullParserHandler piSeasXmlHandler) {
        piSeasXmlHandler.setAutoWaterChange(auto);
        piSeasXmlHandler.setDrain(manualDrain);
        piSeasXmlHandler.setFill(manualFill);
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public void setManualFill(boolean manualFill) {
        this.manualFill = manualFill;
    }

    public void setManualDrain(boolean manualDrain) {
        this.manualDrain = manualDrain;
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
