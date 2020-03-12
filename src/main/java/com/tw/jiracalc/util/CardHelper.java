package com.tw.jiracalc.util;

import com.tw.jiracalc.beans.history.HistoryDetail;
import com.tw.jiracalc.beans.history.JiraCardHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CardHelper {

    public static Map<String, Double> calculateCycleTime(JiraCardHistory jiraCardHistory) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Double> finalResult = new HashMap<>();

        List<HistoryDetail> activities = jiraCardHistory.getItems().stream()
                .filter(x -> "status".equals(x.getFieldId()))
                .collect(Collectors.toList());
        for (int index = 0; index < activities.size(); index++) {
            HistoryDetail nextActivity = null;
            if (index < activities.size() - 1) {
                nextActivity = activities.get(index + 1);
            }

            HistoryDetail currentActivity = activities.get(index);
            Double costHour = result.get(currentActivity.getTo().getDisplayValue());

            if (null == nextActivity) {
                if (costHour != null) {
                    costHour += TimeTool.getWorkDay(currentActivity.getTimestamp(), System.currentTimeMillis());
                } else {
                    costHour = TimeTool.getWorkDay(currentActivity.getTimestamp(), System.currentTimeMillis());
                }
            } else {
                if (costHour != null) {
                    costHour += TimeTool.getWorkDay(currentActivity.getTimestamp(), nextActivity.getTimestamp());
                } else {
                    costHour = TimeTool.getWorkDay(currentActivity.getTimestamp(), nextActivity.getTimestamp());
                }
            }
            result.put(currentActivity.getTo().getDisplayValue(), costHour);
        }

        result.forEach((key, value) -> finalResult.put(key.toLowerCase(), TimeTool.roundUp(value, 1)));
        return finalResult;
    }
}
