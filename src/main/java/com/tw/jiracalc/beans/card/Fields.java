package com.tw.jiracalc.beans.card;


import com.tw.jiracalc.beans.cycletime.CycleTimeBean;
import lombok.Data;

@Data
public class Fields {
    private String summary;
    private Status status;
    private CycleTimeBean cycleTimeBean;
    private Assignee assignee;
    private Issuetype issuetype;
    private Priority priority;
    private Reporter reporter;
}
