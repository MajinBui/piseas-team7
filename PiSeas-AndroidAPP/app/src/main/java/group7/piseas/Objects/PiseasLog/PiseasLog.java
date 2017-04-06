package group7.piseas.Objects.PiseasLog;

import java.util.Date;

import group7.piseas.Helpers.Utilities;

/**
 * Created by mmbab on 2/23/2017.
 *
 * Single log containing time stand, notification, action, and manual action
 *
 */

public class PiseasLog {
    private Date date;
    private String type;
    private String description;

    public PiseasLog(){
        type = "";
        description = "";
    }

    public PiseasLog(String dd, String tt, String desc){
        date = Utilities.stringToDate(dd);
        type = tt;
        description = desc;
    }

    /**
     * Dangerous, do not use for equality
     * @return
     */
    public String getDateString(){
        return Utilities.dateToString(date);
    }

    public Date getDate(){
        return date;
    }
    public String getType(){
        return type;
    }

    public String getDescription(){
        return description;
    }

    public String toString(){
        return Utilities.dateToString(date)+ " " + type + " " + description;
    }

}
