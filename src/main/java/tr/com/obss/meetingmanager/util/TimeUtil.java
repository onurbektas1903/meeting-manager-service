package tr.com.obss.meetingmanager.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static Integer findDiffrenceAsMinutes(long startDate,long endDate){
        Long diffrence = TimeUnit.MINUTES.convert(endDate - startDate, TimeUnit.MILLISECONDS);
        return  diffrence.intValue();
    }
    public  static String convertAndGetDate(long date,String timeZone){
        DateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        s.setTimeZone(TimeZone.getTimeZone(timeZone));
        return s.format(new Date(date));
    }
}
