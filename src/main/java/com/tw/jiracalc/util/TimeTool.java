package com.tw.jiracalc.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class TimeTool {

    private static long A_DAY_MILLIS = 86400 * 1000L;

    static double calculateWorkingDay(final long startMillis, final long endMillis) {
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(startMillis);
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(endMillis);

        long dayGapMillis = endMillis - startMillis;

        if (dayGapMillis < A_DAY_MILLIS) {
            return millis2Day(dayGapMillis);
        }

        long whichDayIsStartDay = start.get(Calendar.DAY_OF_WEEK);
        long weekGap = dayGapMillis / (A_DAY_MILLIS * 7);
        long weekends = weekGap * 2;
        long weeks = (dayGapMillis / A_DAY_MILLIS) % 7;

        if (whichDayIsStartDay + weeks > 7) weekends = weekends + 2;
        if (whichDayIsStartDay + weeks == 7) weekends++;

        return millis2Day(dayGapMillis - weekends * A_DAY_MILLIS);
    }

    static Double millis2Day(final long millis) {
        double days = (double) millis / A_DAY_MILLIS;
        BigDecimal bg = new BigDecimal(days).setScale(2, RoundingMode.HALF_UP);
        return bg.doubleValue();
    }
}
