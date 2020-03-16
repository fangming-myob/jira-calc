package com.tw.jiracalc.util;

import com.tw.jiracalc.model.history.HistoryDetail;
import com.tw.jiracalc.model.history.JiraCardHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CardHelper {

    public static Map<String, Double> calculateCycleTime(JiraCardHistory jiraCardHistory) {
        Map<String, Double> result = new HashMap<>();
        Map<String, Double> finalResult = new HashMap<>();

        List<HistoryDetail> activities = jiraCardHistory.items.stream()
                .filter(activity -> "status".equals(activity.fieldId))
                .collect(Collectors.toList());
        for (int index = 0; index < activities.size(); index++) {
            HistoryDetail nextActivity = null;
            if (index < activities.size() - 1) {
                nextActivity = activities.get(index + 1);
            }

            HistoryDetail currentActivity = activities.get(index);
            Double costHour = result.get(currentActivity.to.displayValue);

            if (null == nextActivity) {
                if (costHour != null) {
                    costHour += TimeTool.getWorkDay(currentActivity.timestamp, System.currentTimeMillis());
                } else {
                    costHour = TimeTool.getWorkDay(currentActivity.timestamp, System.currentTimeMillis());
                }
            } else {
                if (costHour != null) {
                    costHour += TimeTool.getWorkDay(currentActivity.timestamp, nextActivity.timestamp);
                } else {
                    costHour = TimeTool.getWorkDay(currentActivity.timestamp, nextActivity.timestamp);
                }
            }
            result.put(currentActivity.to.displayValue, costHour);
        }

        result.forEach((key, value) -> finalResult.put(key.toLowerCase(), TimeTool.roundUp(value, 1)));
        return finalResult;
    }
}
