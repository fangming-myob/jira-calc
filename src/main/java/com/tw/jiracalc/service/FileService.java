package com.tw.jiracalc.service;

import com.tw.jiracalc.beans.JiraCard;
import com.tw.jiracalc.beans.JiraCards;
import org.springframework.stereotype.Service;

@Service
public class FileService {
    private static Long ONE_DAY = 3600000L;
    private static String HEADER = "jiraId,Issue Type,Status,Summary,Priority,Assignee,Reporter,Backlog(h),Analysis(h),Approved for Development(h),In-Progress(h),Showcase(h)";

    public String generateFile(JiraCards jiraCards) {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(HEADER);
        stringBuffer.append("\r\n");

        jiraCards.getIssues().forEach(jiraCard -> {
            String backlogStr = getLeadTimes(jiraCard, "Backlog");
            String analysis = getLeadTimes(jiraCard, "Analysis");
            String afd = getLeadTimes(jiraCard, "Approved for Development");
            String inProgress = getLeadTimes(jiraCard, "In-Progress");
            String showcase = getLeadTimes(jiraCard, "Showcase");
            stringBuffer.append(
                    jiraCard.getKey()).append(",")
                    .append(jiraCard.getFields().getIssuetype().getName()).append(",")
                    .append(jiraCard.getFields().getStatus().getStatusCategory().getName()).append(",")
                    .append(jiraCard.getFields().getSummary()).append(",")
                    .append(jiraCard.getFields().getPriority().getName()).append(",")
                    .append(jiraCard.getFields().getAssignee().getName()).append(",")
                    .append(jiraCard.getFields().getReporter().getName()).append(",")
                    .append(backlogStr).append(",")
                    .append(analysis).append(",")
                    .append(afd).append(",")
                    .append(inProgress).append(",")
                    .append(showcase)
                    .append("\r\n");
        });

        return stringBuffer.toString();
    }

    private String getLeadTimes(JiraCard jiraCard, String stageName) {
        Long stageTime = jiraCard.getFields().getTransitionBean().getCycleTime().get(stageName);
        String leadHours;
        if (null != stageTime) {
            if (stageTime < ONE_DAY) {
                leadHours = "<1";
            } else {
                leadHours = String.valueOf(stageTime / ONE_DAY);
            }
        } else {
            leadHours = "<1";
        }
        return leadHours;
    }
}
