package Objects;

import com.babol.android.xml_parse.piDate;

import java.util.ArrayList;

/**
 * Created by mmbab on 2/20/2017.
 */

public class Logs {

    ArrayList<LogEnum> logList;
    ArrayList<piDate> date;
    public Logs(){
        logList = new ArrayList<LogEnum>();
        date = new ArrayList<piDate>();
    }

    public void add(LogEnum log, piDate dd){
        logList.add(log);
        date.add(dd);
    }
    public void remove(int i){
        logList.remove(i);
        date.remove(i);
    }

    public void clear(){
        logList.clear();
        date.clear();
    }

}
