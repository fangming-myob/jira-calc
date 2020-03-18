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

        List<HistoryDetail> statusActivities = jiraCardHistory.items.stream()
                .filter(activity -> "status".equals(activity.fieldId))
                .collect(Collectors.toList());
        for (int index = 0; index < statusActivities.size(); index++) {
            HistoryDetail nextStatusActivity = null;
            if (index < statusActivities.size() - 1) {
                nextStatusActivity = statusActivities.get(index + 1);
            }

            HistoryDetail currentActivity = statusActivities.get(index);
            Double costHour = result.get(currentActivity.to.displayValue);

            if (null == nextStatusActivity) {
                if (costHour != null) {
                    costHour += TimeTool.getWorkDay(currentActivity.timestamp, System.currentTimeMillis());
                } else {
                    costHour = TimeTool.getWorkDay(currentActivity.timestamp, System.currentTimeMillis());
                }
            } else {
                if (costHour != null) {
                    costHour += TimeTool.getWorkDay(currentActivity.timestamp, nextStatusActivity.timestamp);
                } else {
                    costHour = TimeTool.getWorkDay(currentActivity.timestamp, nextStatusActivity.timestamp);
                }
            }
            result.put(currentActivity.to.displayValue, costHour);
        }

        result.forEach((key, value) -> finalResult.put(key.toLowerCase(), TimeTool.roundUp(value, 1)));
        return finalResult;
    }
}
