package group7.piseas.Objects;

/**
 * Created by mmbab on 12/5/2016.
 *
 */

public class LightSchedule {
    private int onHour, offHour, onMin, offMin;

    public LightSchedule(int onHr, int offHr, int onMn, int offMn){
        onHour = onHr;
        offHour = offHr;
        onMin = onMn;
        offMin = offMn;
    }

    public int getOnHour(){
        return onHour;
    }

    public int getOnMin(){
        return onMin;
    }

    public int getOffHour(){
        return offHour;
    }

    public int getOffMin(){
        return offMin;
    }

    public String getOnTime(){
        String padding = "00";
        String temp = Integer.toString(onMin);
        return (onHour + ":"  + (padding.substring(temp.length()) + temp));
    }
    public String getOffTime(){
        String padding = "00";
        String temp = Integer.toString(offMin);
        return (offHour + ":"  + (padding.substring(temp.length()) + temp));
    }

    public String getTime(){
        return getOnTime() + " - " + getOffTime();
    }
}
