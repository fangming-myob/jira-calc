package com.tw.jiracalc.beans.history;

import lombok.Data;

@Data
public class HistoryDetail {
    private String fieldId;
    private Long timestamp;
    private Status from;
    private Status to;

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
