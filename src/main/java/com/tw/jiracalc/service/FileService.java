package com.tw.jiracalc.service;

import com.tw.jiracalc.beans.JiraCard;
import com.tw.jiracalc.beans.JiraCards;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    public String generateFile(JiraCards jiraCards, final List<String> cardStages) {

        final StringBuffer contentBuffer = new StringBuffer();
        final StringBuffer stageHeader = new StringBuffer();
        cardStages.forEach(stage -> stageHeader.append(",").append(stage));
        final String tableHeader = "jiraId,Issue Type,Status,Summary,Priority,Assignee,Reporter" + stageHeader.toString();
        contentBuffer.append(tableHeader);
        contentBuffer.append("\r\n");

        jiraCards.getIssues().forEach(jiraCard -> {
            contentBuffer
                    .append(jiraCard.getKey()).append(",")
                    .append(jiraCard.getFields().getIssuetype().getName()).append(",")
                    .append(jiraCard.getFields().getStatus().getStatusCategory().getName()).append(",")
                    .append(jiraCard.getFields().getSummary().replaceAll(",", " ")).append(",")
                    .append(jiraCard.getFields().getPriority().getName()).append(",")
                    .append(jiraCard.getFields().getAssignee().getName()).append(",")
                    .append(jiraCard.getFields().getReporter().getName());
            cardStages.forEach(stage -> {
                final String stageCostStr = getLeadTimes(jiraCard, stage);
                contentBuffer.append(",").append(stageCostStr);
            });
            contentBuffer.append("\r\n");
        });

        return contentBuffer.toString();
    }

    private String getLeadTimes(JiraCard jiraCard, String stageName) {
        Float stageTime = jiraCard.getFields().getCycleTimeBean().getCycleTime().get(stageName);
        String leadHours;
        if (null != stageTime) {
            leadHours = String.valueOf(stageTime);
        } else {
            leadHours = "-";
        }
        return leadHours;
    }

//    @Deprecated
//    private String getLeadTimes2(JiraCard jiraCard, String stageName) {
//        Long stageTime = jiraCard.getFields().getCycleTimeBean().getCycleTime().get(stageName);
//        String leadHours;
//        if (null != stageTime) {
//            final Long oneDay = 3600000L * 24L;
//            if (stageTime < oneDay) {
//                leadHours = "<1";
//            } else {
//                leadHours = String.valueOf(stageTime / oneDay);
//            }
//        } else {
//            leadHours = "-";
//        }
//        return leadHours;
//    }
}
