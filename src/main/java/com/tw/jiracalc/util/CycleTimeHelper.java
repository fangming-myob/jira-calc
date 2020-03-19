package com.tw.jiracalc.util;

import com.tw.jiracalc.model.history.HistoryDetail;
import com.tw.jiracalc.model.history.JiraCardHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CycleTimeHelper {

    public static Map<String, Double> calculateCycleTime(JiraCardHistory jiraCardHistory) {
        Map<String, Double> result = new HashMap<>();

        List<HistoryDetail> statusActivities = jiraCardHistory.items.stream()
                .filter(activity -> "status".equals(activity.fieldId))
                .collect(Collectors.toList());
        for (int index = 0; index < statusActivities.size(); index++) {
            HistoryDetail nextStatusActivity = null;
            if (index < statusActivities.size() - 1) {
                nextStatusActivity = statusActivities.get(index + 1);
            }

            HistoryDetail currentActivity = statusActivities.get(index);
            Double costDays = result.get(currentActivity.to.displayValue.toLowerCase());

            if (null == nextStatusActivity) {
                if (costDays != null) {
                    costDays += TimeTool.calculateWorkingDay(currentActivity.timestamp, System.currentTimeMillis());
                } else {
                    costDays = TimeTool.calculateWorkingDay(currentActivity.timestamp, System.currentTimeMillis());
                }
            } else {
                if (costDays != null) {
                    costDays += TimeTool.calculateWorkingDay(currentActivity.timestamp, nextStatusActivity.timestamp);
                } else {
                    costDays = TimeTool.calculateWorkingDay(currentActivity.timestamp, nextStatusActivity.timestamp);
                }
            }
            result.put(currentActivity.to.displayValue.toLowerCase(), costDays);
        }

        return result;
    }
}
