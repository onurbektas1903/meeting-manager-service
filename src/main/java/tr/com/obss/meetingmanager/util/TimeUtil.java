package tr.com.obss.meetingmanager.util;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    public static Integer findDiffrenceAsMinutes(long startDate,long endDate){
        Long diffrence = TimeUnit.MINUTES.convert(endDate - startDate, TimeUnit.MILLISECONDS);
        return  diffrence.intValue();
    }
}
