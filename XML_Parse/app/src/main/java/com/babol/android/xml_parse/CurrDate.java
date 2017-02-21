package com.babol.android.xml_parse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mmbab on 2/8/2017.
 */

public class CurrDate {
    int year;
    int month;
    int day;
    int hour;
    int min;
    int sec;

    CurrDate(){
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

    CurrDate(int y, int m, int d, int hr, int mn, int sc){
        year = y;
        month = m;
        day = d;
        hour = hr;
        min = mn;
        sec = sc;
    }

    public int getYear(){return year;}
    public int getMonth(){return month;}
    public int getDay(){return day;}
    public int getHour(){return hour;}
    public int getMin(){return min;}
    public int getSec(){return sec;}


}
