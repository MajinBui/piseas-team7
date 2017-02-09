package Objects;

import java.util.Arrays;

/**
 * Created by mmbab on 2/8/2017.
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

        public void setWeek(int day){
            week[day] = true;
        }

        public boolean getWeek(int day){
            return week[day];
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
