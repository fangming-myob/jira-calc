package com.tw.jiracalc.model.history;

public class HistoryDetail {
    public String fieldId;
    public Long timestamp;
    public Status from;
    public Status to;

    @Override
    public String toString() {
        return "HistoryDetail{" +
                "fieldId='" + fieldId + '\'' +
                ", timestamp=" + timestamp +
                ", from=" + from +
                ", to=" + to +
                '}';
    }

}
