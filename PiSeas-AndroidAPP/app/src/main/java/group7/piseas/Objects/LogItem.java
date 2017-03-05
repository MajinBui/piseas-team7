package group7.piseas.Objects;

import java.util.Date;

/**
 * Created by Salli on 2017-02-20.
 */

public class LogItem {
    String timestamp;
    String description;

    public LogItem(String time, String desc){
        timestamp = time;
        description = desc;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String time) {
        this.timestamp = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }
}