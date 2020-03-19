package com.tw.jiracalc.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TimeToolTest {
    @Test
    void return_1_day_when_give_1_days_millis() {
        long input = 86400000;
        double actual = TimeTool.millis2Day(input);
        Assertions.assertEquals(1, actual);
    }

    @Test
    void return_half_day_when_give_half_days_millis() {
        long input = 43200000;
        double actual = TimeTool.millis2Day(input);
        Assertions.assertEquals(0.5, actual);
    }

    @Test
    void return_0_4_6_day_when_give_less_half_days_millis() {
        long input = 40000000;
        double actual = TimeTool.millis2Day(input);
        Assertions.assertEquals(0.46, actual);
    }

    @Test
    void return_0_47_day_when_give_roundup() {
        long input = 40200000;
        double actual = TimeTool.millis2Day(input);
        Assertions.assertEquals(0.47, actual);
    }

    @Test
    void return_2_days_when_give_2_days_millis() {
        long input = 86400000 * 2;
        double actual = TimeTool.millis2Day(input);
        Assertions.assertEquals(2, actual);
    }

    @Test
    void return_1_5_day_when_give_1_5_days_millis() {
        long input = 129600000;
        double actual = TimeTool.millis2Day(input);
        Assertions.assertEquals(1.5, actual);
    }

    @Test
    void return_1_5_day_when_give_more_than_1_5_days_millis() {
        long input = 129800000;
        double actual = TimeTool.millis2Day(input);
        Assertions.assertEquals(1.5, actual);
    }

    @Test
    void return_more_than_1_5_7_day_when_give_more_than_1_5_7() {
        long input = 135900000;
        double actual = TimeTool.millis2Day(input);
        Assertions.assertEquals(1.57, actual);
    }

    @Test
    void return_1_5_7_days_when_roundup() {
        long input = 135500000;
        double actual = TimeTool.millis2Day(input);
        Assertions.assertEquals(1.57, actual);
    }

    @Test
    void return_0_working_day() {
        long start = 1583985258515L;
        long end = 1583985282880L;
        double actual = TimeTool.calculateWorkingDay(start, end);

        Assertions.assertEquals(0, actual);
    }

    @Test
    void return_1_2_1_working_day() {

        long start = 1583985282880L;
        long end = 1584090137883L;
        double actual = TimeTool.calculateWorkingDay(start, end);

        Assertions.assertEquals(1.21, actual);
    }
}
