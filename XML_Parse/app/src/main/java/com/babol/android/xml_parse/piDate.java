package com.babol.android.xml_parse;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mmbab on 2/20/2017.
 */

public class piDate {
    private String date;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private int sec;
    private String timeZone;        // need to implement

    // Create Date object using current time
    public piDate(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR);
        min = cal.get(Calendar.MINUTE);
        sec = cal.get(Calendar.SECOND);
    }

    // Create Date time with a time string coming in
    public piDate(String d){
        date = d;
        parseData();
    }

    private void parseData(){
        // Retrieve date
        year = Integer.parseInt(date.substring(0, date.indexOf("-")));
        int i = date.indexOf("-") + 1;
        month = Integer.parseInt(date.substring(i, i+2));
        i+=3;
        day = Integer.parseInt(date.substring(i, i+2));

        // Retrieve time
        i = date.indexOf("T") + 1;
        int hour = Integer.parseInt(date.substring(i, i+2));
        i+=3;
        int min = Integer.parseInt(date.substring(i, i+2));
        i+=3;
        int sec = Integer.parseInt(date.substring(i, i+2));

        // Retrieve the time zone
        String zone = date.substring(date.length() - 5, date.length());
        char c = zone.charAt(0);            // + → subtract time, - → add time
        int zoneHr = Integer.parseInt(zone.substring(1, 3));
        int zoneMin = Integer.parseInt(zone.substring(3, 5));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);

        cal.add(Calendar.MINUTE, ((c=='-') ? zoneMin : zoneMin * -1));
        cal.add(Calendar.HOUR_OF_DAY, ((c=='-') ?  zoneHr : zoneHr * -1));

        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);
    }

    public int getYear(){
        return year;
    }

    public int getMonth(){
        return month;
    }

    public int getDay(){
        return day;
    }

    public int getHour(){
        return hour;
    }

    public int getMin(){
        return min;
    }

    public int getSec(){
        return sec;
    }

    public String toString(){
        return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec;
    }

}
