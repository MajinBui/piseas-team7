package group7.piseas.Helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Van on 2017-04-04.
 */

public class Utilities {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CANADA);
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm aaa", Locale.CANADA);
    private static final DateFormat DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);

    /**
     * Converts a string date to a date object.
     * @param date
     * @return
     */
    public static Date stringToDate(String date) {
        Date startDate = null;

        try {
            startDate = DATE_FORMAT.parse(date);
            return startDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startDate;
    }

    /**
     * Converts a date object to a string.
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String simpleDateToString(Date date) {return TIME_FORMAT.format(date) + "\n"+ DAY_FORMAT.format(date);}
}
