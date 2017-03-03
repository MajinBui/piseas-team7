package com.babol.android.xml_parse;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mmbab on 2/20/2017.
 */

public class piDate {
    private Calendar cal = Calendar.getInstance();
    private String date;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int min;
    private int sec;
    private int timeZoneHr;        // need to implement
    private int timeZoneMin;
    char c;

    // Create Date object using current time
    public piDate(){
        Date date = new Date();
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
        cal = Calendar.getInstance();

        // Retrieve date
        cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, date.indexOf("-"))));

        int i = date.indexOf("-") + 1;
        cal.set(Calendar.MONTH, Integer.parseInt(date.substring(i, i+2)));
        i+=3;
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(i, i+2)));

        // Retrieve time
        i = date.indexOf("T") + 1;
        cal.set(Calendar.HOUR, Integer.parseInt(date.substring(i, i+2)));
        i+=3;
        cal.set(Calendar.MINUTE, Integer.parseInt(date.substring(i, i+2)));
        i+=3;
        cal.set(Calendar.SECOND, Integer.parseInt(date.substring(i, i+2)));

        // Retrieve the time zone
        String zone = date.substring(date.length() - 5, date.length());
        c = zone.charAt(0);            // + → subtract time, - → add time
        timeZoneHr = Integer.parseInt(zone.substring(1, 3));
        timeZoneMin = Integer.parseInt(zone.substring(3, 5));

        cal.add(Calendar.MINUTE, ((c=='-') ? timeZoneMin : timeZoneMin * -1));
        cal.add(Calendar.HOUR_OF_DAY, ((c=='-') ?  timeZoneHr : timeZoneHr * -1));
    }

    public int getYear(){
        return cal.get(Calendar.YEAR);
    }

    public int getMonth(){
        return cal.get(Calendar.MONTH);
    }

    public int getDay(){
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour(){
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    public int getMin(){
        return cal.get(Calendar.MINUTE);
    }

    public int getSec(){
        return cal.get(Calendar.SECOND);
    }

    public String getFullDate(){
        Calendar tempCal = cal;

        tempCal.add(Calendar.MINUTE, ((c=='-') ? timeZoneMin : timeZoneMin * -1));
        tempCal.add(Calendar.HOUR_OF_DAY, ((c=='-') ?  timeZoneHr : timeZoneHr * -1));

        return String.format("%04d", tempCal.get(Calendar.YEAR)) + "-" + String.format("%02d", tempCal.get(Calendar.MONTH))
                + "-" + String.format("%02d", tempCal.get(Calendar.DAY_OF_MONTH)) + "T" + String.format("%02d", tempCal.get(Calendar.HOUR_OF_DAY))
                + ":" + String.format("%02d", tempCal.get(Calendar.MINUTE)) + ":" + String.format("%02d", tempCal.get(Calendar.SECOND))
                + c + String.format("%02d", timeZoneHr) + String.format("%02d", timeZoneMin);
    }

    public String toString(){
        return String.format("%04d", cal.get(Calendar.YEAR)) + "-" + String.format("%02d", cal.get(Calendar.MONTH))
                + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + " " + String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))
                + ":" + String.format("%02d", cal.get(Calendar.MINUTE)) + ":" + String.format("%02d", cal.get(Calendar.SECOND));
    }
}
