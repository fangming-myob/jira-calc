package com.tw.jiracalc.service;

import com.tw.jiracalc.beans.card.JiraCards;
import com.tw.jiracalc.beans.history.HistoryDetail;
import com.tw.jiracalc.beans.history.JiraCardHistory;
import com.tw.jiracalc.util.TimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class JiraService {

    @Autowired
    RestTemplate restTemplate;

    public JiraCards getCards(final String jql, final String jiraToken) {
        long start = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", jiraToken);

        String url = "https://arlive.atlassian.net/rest/api/2/search?jql=" + jql;

        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<JiraCards> response = restTemplate.exchange(url, HttpMethod.GET, entity, JiraCards.class);

        long end = System.currentTimeMillis();
        System.out.println("Get cards costs " + (end - start) + "ms");
        return response.getBody();
    }

    @Async
    public CompletableFuture<Map<String, Double>> getCycleTime(final String jiraId, final String jiraToken) {
        Map<String, Float> result = new HashMap<>();
        Map<String, Double> finalResult = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", jiraToken);

        String url = "https://arlive.atlassian.net/rest/internal/2/issue/"+jiraId+"/activityfeed";
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<JiraCardHistory> response = restTemplate.exchange(url, HttpMethod.GET, entity, JiraCardHistory.class);

        JiraCardHistory jiraCardHistory = response.getBody();

        List<HistoryDetail> activities = jiraCardHistory.getItems().stream()
                .filter(x -> "status".equals(x.getFieldId()))
                .collect(Collectors.toList());
        for (int index = 0; index < activities.size(); index++) {
            HistoryDetail nextActivity = null;
            if (index < activities.size() - 1) {
                nextActivity = activities.get(index + 1);
            }

            HistoryDetail currentActivity = activities.get(index);
            Float costHour = result.get(currentActivity.getTo().getDisplayValue());

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

        result.forEach((key, value) -> finalResult.put(key, TimeTool.roundUp(value, 1)));

        return CompletableFuture.completedFuture(finalResult);
    }
}
