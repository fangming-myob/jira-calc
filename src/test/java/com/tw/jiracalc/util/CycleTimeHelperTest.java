package com.tw.jiracalc.util;

import com.tw.jiracalc.model.history.HistoryDetail;
import com.tw.jiracalc.model.history.JiraCardHistory;
import com.tw.jiracalc.model.history.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class CycleTimeHelperTest {

    @Test
    void size_should_be_0_when_no_status() {
        JiraCardHistory jiraCardHistory = new JiraCardHistory();

        List<HistoryDetail> items = new ArrayList<>();
        HistoryDetail historyDetail = new HistoryDetail();
        historyDetail.fieldId = "description";
        items.add(historyDetail);
        jiraCardHistory.items = items;
        Map<String, Double> actualCycleTime = CycleTimeHelper.calculateCycleTime(jiraCardHistory);

        Assertions.assertEquals(0, actualCycleTime.size());
    }

    @Test
    void size_should_be_1_when_have_1_status() {
        JiraCardHistory jiraCardHistory = new JiraCardHistory();

        List<HistoryDetail> items = new ArrayList<>();
        HistoryDetail historyDetail = new HistoryDetail();
        historyDetail.fieldId = "status";
        historyDetail.to = new Status("To Do");
        historyDetail.timestamp = 1579101032811L;
        items.add(historyDetail);
        jiraCardHistory.items = items;
        Map<String, Double> actualCycleTime = CycleTimeHelper.calculateCycleTime(jiraCardHistory);

        Assertions.assertEquals(1, actualCycleTime.size());
    }

    @Test
    void size_should_be_2_when_have_2_status() {
        JiraCardHistory jiraCardHistory = new JiraCardHistory();

        List<HistoryDetail> items = new ArrayList<>();
        HistoryDetail firstItem = new HistoryDetail();
        firstItem.fieldId = "status";
        firstItem.to = new Status("To Do");
        firstItem.timestamp = 1579101032811L;
        items.add(firstItem);
        HistoryDetail secondItem = new HistoryDetail();
        secondItem.fieldId = "status";
        secondItem.from = new Status("To Do");
        secondItem.to = new Status("In Progress");
        secondItem.timestamp = 1579187432000L;
        items.add(secondItem);
        jiraCardHistory.items = items;
        Map<String, Double> actualCycleTime = CycleTimeHelper.calculateCycleTime(jiraCardHistory);

        Assertions.assertEquals(2, actualCycleTime.size());
    }

    @Test
    void return_1_day_in_TO_DO() {
        JiraCardHistory jiraCardHistory = new JiraCardHistory();

        List<HistoryDetail> items = new ArrayList<>();
        HistoryDetail firstItem = new HistoryDetail();
        firstItem.fieldId = "status";
        firstItem.to = new Status("To Do");
        firstItem.timestamp = 1579101032811L;
        items.add(firstItem);
        HistoryDetail secondItem = new HistoryDetail();
        secondItem.fieldId = "status";
        secondItem.from = new Status("To Do");
        secondItem.to = new Status("In Progress");
        secondItem.timestamp = 1579187432000L;
        items.add(secondItem);
        jiraCardHistory.items = items;
        Map<String, Double> actualCycleTime = CycleTimeHelper.calculateCycleTime(jiraCardHistory);

        double oneDay = 1;
        double actually = actualCycleTime.get("to do");
        Assertions.assertEquals(oneDay, actually);
    }
}
