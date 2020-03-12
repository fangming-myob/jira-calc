package com.tw.jiracalc.util;

import com.tw.jiracalc.beans.history.HistoryDetail;
import com.tw.jiracalc.beans.history.JiraCardHistory;
import com.tw.jiracalc.beans.history.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CardHelperTest {

    @Test
    void size_should_be_0_when_no_status() {
        JiraCardHistory jiraCardHistory = new JiraCardHistory();
        Map<String, Double> exceptCycleTime = new HashMap<>();

        List<HistoryDetail> items = new ArrayList<>();
        HistoryDetail historyDetail = new HistoryDetail();
        historyDetail.setFieldId("description");
        items.add(historyDetail);
        jiraCardHistory.setItems(items);
        Map<String, Double> actualCycleTime = CardHelper.calculateCycleTime(jiraCardHistory);

        Assertions.assertEquals(exceptCycleTime.size(), actualCycleTime.size());
    }

    @Test
    void size_should_be_1_when_have_1_status() {
        JiraCardHistory jiraCardHistory = new JiraCardHistory();

        List<HistoryDetail> items = new ArrayList<>();
        HistoryDetail historyDetail = new HistoryDetail();
        historyDetail.setFieldId("status");
        historyDetail.setTo(new Status("To Do"));
        historyDetail.setTimestamp(1579101032811L);
        items.add(historyDetail);
        jiraCardHistory.setItems(items);
        Map<String, Double> actualCycleTime = CardHelper.calculateCycleTime(jiraCardHistory);

        Assertions.assertEquals(1, actualCycleTime.size());
    }

    @Test
    void size_should_be_2_when_have_2_status() {
        JiraCardHistory jiraCardHistory = new JiraCardHistory();

        List<HistoryDetail> items = new ArrayList<>();
        HistoryDetail firstItem = new HistoryDetail();
        firstItem.setFieldId("status");
        firstItem.setTo(new Status("To Do"));
        firstItem.setTimestamp(1579101032811L);
        items.add(firstItem);
        HistoryDetail secondItem = new HistoryDetail();
        secondItem.setFieldId("status");
        secondItem.setFrom(new Status("To Do"));
        secondItem.setTo(new Status("In Progress"));
        secondItem.setTimestamp(1579187432000L);
        items.add(secondItem);
        jiraCardHistory.setItems(items);
        Map<String, Double> actualCycleTime = CardHelper.calculateCycleTime(jiraCardHistory);

        Assertions.assertEquals(2, actualCycleTime.size());
    }

    @Test
    void return_1_day_in_TO_DO() {
        JiraCardHistory jiraCardHistory = new JiraCardHistory();

        List<HistoryDetail> items = new ArrayList<>();
        HistoryDetail firstItem = new HistoryDetail();
        firstItem.setFieldId("status");
        firstItem.setTo(new Status("To Do"));
        firstItem.setTimestamp(1579101032811L);
        items.add(firstItem);
        HistoryDetail secondItem = new HistoryDetail();
        secondItem.setFieldId("status");
        secondItem.setFrom(new Status("To Do"));
        secondItem.setTo(new Status("In Progress"));
        secondItem.setTimestamp(1579187432000L);
        items.add(secondItem);
        jiraCardHistory.setItems(items);
        Map<String, Double> actualCycleTime = CardHelper.calculateCycleTime(jiraCardHistory);

        Assertions.assertEquals(1d, actualCycleTime.get("to do"));
    }
}
