package com.tw.jiracalc.beans;

import java.util.Map;

public class Transition {
    private String key;
    private Map<String, Long> cycleTime;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Long> getCycleTime() {
        return cycleTime;
    }

    public void setCycleTime(Map<String, Long> cycleTime) {
        this.cycleTime = cycleTime;
    }
}
