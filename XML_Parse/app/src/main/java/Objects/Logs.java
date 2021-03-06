package Objects;

import com.babol.android.xml_parse.piDate;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Log.LogDesc;
import Log.LogType;

/**
 * Created by mmbab on 2/20/2017.
 *
 * Container of all logs
 *
 */

public class Logs {
    private piDate stamp;
    private ArrayList<Log> logs;

    public Logs(){ logs = new ArrayList<Log>(); }

    // Add timestamp
    public void timeStamp(String time){ stamp = new piDate(time); }

    public void add(Log ll){ logs.add(ll); }

    public int size(){
        return logs.size();
    }

    public String getTimeStamp(){ return stamp.toString(); }

    public String getType(int i){
        return logs.get(i).getType();
    }

    public String getDate(int i){return logs.get(i).getDate(); }

    public String getDesccription(int i){
        return logs.get(i).getDescription();
    }

    public Log getLog(int i) { return logs.get(i); }

    public void remove(int i){ logs.remove(i); }

    public void clear(){ logs.clear(); }
}
