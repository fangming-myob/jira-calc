package com.tw.jiracalc.util;

import java.util.Calendar;

public class TimeTool {
    public static Float getWorkDay(final long startMillis, final long endMillis) {
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startMillis);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endMillis);

        long day = 86400000;
        long dayGap = (end.getTimeInMillis() - start.getTimeInMillis()) / day;

        if (dayGap < 1) {
            float workingTime = (float) (endMillis - startMillis) / day;
            return (float) Math.round(workingTime * 100) / 100;
        }

        int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
        long weekGap = dayGap / 7;

        long weekends = weekGap * 2;
        long yushu = dayGap % 7;

        if (dayOfWeek + yushu > 7) weekends = weekends + 2;
        if (dayOfWeek + yushu == 7) weekends++;

        return (float) (dayGap - weekends);
    }
}
