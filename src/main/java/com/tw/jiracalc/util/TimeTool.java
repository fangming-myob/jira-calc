package com.tw.jiracalc.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class TimeTool {
    public static double calculateWorkDay(final long startMillis, final long endMillis) {
        // 开始时间timestamp 结束时间timestamp
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startMillis);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endMillis);

        long day = 86400 * 1000;
        double dayGap = (double) (endMillis - startMillis) / day;

        if (dayGap < 1) {
            return roundUp(dayGap, 2);
        }

        int whichDayIsStartDay = start.get(Calendar.DAY_OF_WEEK);
        int weekGap = (int) (dayGap / 7);
        int weekends = weekGap * 2;
        int weeks = (int) (dayGap % 7);

        if (whichDayIsStartDay + weeks > 7) weekends = weekends + 2;
        if (whichDayIsStartDay + weeks == 7) weekends++;

        return roundUp(dayGap - weekends, 2);
    }

    public static Double roundUp(final double data, int decimal) {
        BigDecimal bg = new BigDecimal(data).setScale(decimal, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }
}
