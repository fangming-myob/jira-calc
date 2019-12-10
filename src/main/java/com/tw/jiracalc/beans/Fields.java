package com.tw.jiracalc.beans;

public class Fields {
    private String summary;
    private Status status;
    private TransitionBean transitionBean;
    private Assignee assignee;
    private Issuetype issuetype;
    private Priority priority;
    private Reporter reporter;


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TransitionBean getTransitionBean() {
        return transitionBean;
    }

    public void setTransitionBean(TransitionBean transitionBean) {
        this.transitionBean = transitionBean;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public void setAssignee(Assignee assignee) {
        this.assignee = assignee;
    }

    public Issuetype getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(Issuetype issuetype) {
        this.issuetype = issuetype;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Reporter getReporter() {
        return reporter;
    }

    public void setReporter(Reporter reporter) {
        this.reporter = reporter;
    }
}
