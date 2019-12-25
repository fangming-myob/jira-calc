package com.tw.jiracalc.util;

import java.util.Calendar;

public class TimeTool {
    public static long getWorkDay(final long startMillis, final long endMillis) {
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startMillis);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endMillis);

        long day = 86400000;
        long dayGap = (end.getTimeInMillis() - start.getTimeInMillis()) / day;
        int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
        long weekGap = dayGap / 7;

        long weekends = weekGap * 2;
        long yushu = dayGap % 7;

        if (dayOfWeek + yushu > 7) weekends = weekends + 2;
        if (dayOfWeek + yushu == 7) weekends++;

        long workDay = dayGap - weekends;
        return workDay < 1 ? 1 : workDay;
    }
}
