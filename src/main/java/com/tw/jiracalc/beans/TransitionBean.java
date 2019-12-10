package com.tw.jiracalc.beans;

import java.util.Map;

public class TransitionBean {
    private Map<String, Long> cycleTime;

    public Map<String, Long> getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(Map<String, Long> cycleTime) {
        this.cycleTime = cycleTime;
    }
}
