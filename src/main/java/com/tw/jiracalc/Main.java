package com.tw.jiracalc;

import com.tw.jiracalc.util.TimeTool;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Main {

    public static void main(String[] args) {
        // JUNO-733 Analysis
//        test(1556498661136L, 1566186047668L);
//        test(1569298242554L, 1574666269133L);
//
//        System.out.println(TimeTool.getWorkDay(1556498661136L, 1566186047668L));
//        System.out.println(TimeTool.getWorkDay(1569298242554L, 1574666269133L));
//
//        System.out.println(TimeTool.getWorkDay(1577263282000L, 1577277682000L));


        System.out.println("==========JUNO-911");
        System.out.println(TimeTool.getWorkDay(1575862510287L, 1575862536288L));
        System.out.println(TimeTool.getWorkDay(1575862541908L, 1575862606867L));
        System.out.println(TimeTool.getWorkDay(1575862629815L, 1576464069348L));


    }

}