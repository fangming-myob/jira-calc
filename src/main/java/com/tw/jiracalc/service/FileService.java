package com.tw.jiracalc.service;

import com.tw.jiracalc.beans.JiraCard;
import com.tw.jiracalc.beans.JiraCards;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    public String generateFile(JiraCards jiraCards) {

        final String tableHeader = "jiraId,Issue Type,Status,Summary,Priority,Assignee,Reporter,Backlog(h),Analysis(h),Selected for Development(h),In-Progress(h),Showcase(h)";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(tableHeader);
        stringBuffer.append("\r\n");

        jiraCards.getIssues().forEach(jiraCard -> {
            String backlogStr = getLeadTimes(jiraCard, "Backlog");
            String analysis = getLeadTimes(jiraCard, "Analysis");
            String selectedForDevelopment = getLeadTimes(jiraCard, "Selected for Development");
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
                    .append(selectedForDevelopment).append(",")
                    .append(inProgress).append(",")
                    .append(showcase)
                    .append("\r\n");
        });

        return stringBuffer.toString();
    }

    private String getLeadTimes(JiraCard jiraCard, String stageName) {
        Long stageTime = jiraCard.getFields().getCycleTimeBean().getCycleTime().get(stageName);
        String leadHours;
        if (null != stageTime) {
            final Long oneHour = 3600000L;
            if (stageTime < oneHour) {
                leadHours = "<1";
            } else {
                leadHours = String.valueOf(stageTime / oneHour);
            }
        } else {
            leadHours = "<1";
        }
        return leadHours;
    }
}
