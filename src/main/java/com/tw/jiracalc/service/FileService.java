package com.tw.jiracalc.service;

import com.tw.jiracalc.model.card.JiraCard;
import com.tw.jiracalc.model.card.JiraCards;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    public String generateCycleTimeFile(JiraCards jiraCards, final List<String> displayStages) {

        final StringBuffer contentBuffer = new StringBuffer();
        final StringBuffer stageHeader = new StringBuffer();
        displayStages.forEach(stage -> stageHeader.append(",").append(stage));
        final String tableHeader = "jiraId,Issue Type,Status,Summary,Priority,Assignee,Reporter" + stageHeader.toString();
        contentBuffer.append(tableHeader);
        contentBuffer.append("\r\n");

        jiraCards.issues.forEach(jiraCard -> {
            contentBuffer
                    .append(jiraCard.key).append(",")
                    .append(jiraCard.fields.issuetype.name).append(",")
                    .append(jiraCard.fields.status.statusCategory.name).append(",")
                    .append(jiraCard.fields.summary.replaceAll(",", " ")).append(",");

            if (null == jiraCard.fields.priority) {
                contentBuffer.append("-").append(",");
            } else {
                contentBuffer.append(jiraCard.fields.priority.name).append(",");
            }

            if (null == jiraCard.fields.assignee) {
                contentBuffer.append("-").append(",");
            } else {
                contentBuffer.append(jiraCard.fields.assignee.displayName).append(",");
            }

            contentBuffer
                    .append(jiraCard.fields.reporter.displayName);
            displayStages.forEach(stage -> {
                final String stageCostStr = getLeadTimes(jiraCard, stage);
                contentBuffer.append(",").append(stageCostStr);
            });
            contentBuffer.append("\r\n");
        });

        return contentBuffer.toString();
    }

    private static String getLeadTimes(JiraCard jiraCard, String stageName) {
        Double stageTime = jiraCard.fields.cycleTimeBean.cycleTime.get(stageName.toLowerCase());
        String leadHours;
        if (null != stageTime) {
            leadHours = String.valueOf(stageTime);
        } else {
            leadHours = "-";
        }
        return leadHours;
    }
}
