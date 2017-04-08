package group7.piseas.Objects;

import java.util.Arrays;

/**
 * Created by mmbab on 12/4/2016.
 *
 */

public class FeedSchedule {
    private boolean week[];
    private int hour, min;

    public FeedSchedule(int hr, int mn){
        week = new boolean[7];
        Arrays.fill(week, false);
        hour = hr;
        min = mn;
    }

    public void setWeek(int day, boolean set){
        week[day] = set;
    }

    public boolean getWeek(int day){
        return week[day];
    }

    public boolean[] getWeek(){
        return week;
    }

    public int getHour(){
        return hour;
    }

    public int getMin(){
        return min;
    }

    public int getTimeCompare(){
        return hour * 100 + min;
    }

    public String getTime(){
        String padding = "00";
        String temp = Integer.toString(min);
        return (hour + ":"  + (padding.substring(temp.length()) + temp));
    }

}
