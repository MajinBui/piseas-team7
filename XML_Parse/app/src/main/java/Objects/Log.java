package Objects;

import com.babol.android.xml_parse.piDate;

/**
 * Created by mmbab on 2/23/2017.
 *
 * Single log containing time stand, notification, action, and manual action
 *
 */

public class Log {
    private piDate date;
    private String type;
    private String description;

    public Log(){
        type = "";
        description = "";
    }

    public Log(String dd, String tt, String desc){
        date = new piDate(dd);
        type = tt;
        description = desc;
    }

    public String getDate(){
        return date.toString();
    }

    public String getType(){
        return type;
    }

    public String getDescription(){
        return description;
    }

    public String toString(){
        return date.toString() + " " + type + " " + description;
    }
    //LogDesc dl = new LogDesc("PHRANGE");

}
