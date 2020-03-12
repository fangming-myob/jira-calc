package com.tw.jiracalc.beans.history;

import lombok.Data;

@Data
public class Status {
    private String displayValue;

    public Status() {
    }

    public Status(String displayValue) {
        this.displayValue = displayValue;
    }
}
