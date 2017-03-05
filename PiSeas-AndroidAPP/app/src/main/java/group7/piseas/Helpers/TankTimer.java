package group7.piseas.Helpers;

import android.content.Context;
import android.widget.TextView;

import java.util.HashMap;

import group7.piseas.Objects.PH;
import group7.piseas.Objects.Temperature;
import group7.piseas.Server.FishyClient;

/**
 * Created by Van on 2016-12-06.
 */

public class TankTimer implements Runnable{
    private Temperature temperature;
    private PH pH;
    private TankTimer() {

    }
    public TankTimer(Context context, TextView temperatureTextView, TextView pHTextView) {
        //temperature = new Temperature(context, temperatureTextView);
        //pH = new PH(context, pHTextView);
    }
    public void run() {
        HashMap<String, String> dataList = FishyClient.retrieveServerData("VAN");
        //temperature.run(dataList);
        //pH.run(dataList);
    }
}
