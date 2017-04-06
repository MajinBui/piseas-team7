package group7.piseas.Objects.PiseasLog;

import group7.piseas.Helpers.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by mmbab on 2/20/2017.
 *
 * Container of all piseasLogs
 *
 */

public class Logs {
    private Date stamp;
    private ArrayList<PiseasLog> piseasLogs;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    public Logs(){ piseasLogs = new ArrayList<PiseasLog>(); }

    // Add timestamp
    public void timeStamp(String time){ stamp = Utilities.stringToDate(time); }

    public void add(PiseasLog ll){ piseasLogs.add(ll); }

    public int size(){
        return piseasLogs.size();
    }

    public ArrayList<PiseasLog> getPiseasLogs(){ return piseasLogs; }

    public String getTimeStamp(){ return Utilities.dateToString(stamp); }

    public String getType(int i){
        return piseasLogs.get(i).getType();
    }

    public String getDate(int i){return piseasLogs.get(i).getDateString(); }

    public String getDesccription(int i){
        return piseasLogs.get(i).getDescription();
    }

    public PiseasLog getLog(int i) { return piseasLogs.get(i); }

    public void remove(int i){ piseasLogs.remove(i); }

    public void clear(){ piseasLogs.clear(); }

    public void sort() {
        if (piseasLogs != null)
            Collections.sort(piseasLogs, new Comparator<PiseasLog>() {
                @Override
                public int compare(PiseasLog lhs, PiseasLog rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getDate().compareTo(rhs.getDate())*-1;
                }
            });
    }

}
