package com.tw.jiracalc.service;

import com.tw.jiracalc.beans.card.JiraCards;
import com.tw.jiracalc.beans.cycletime.CycleTimeBean;
import com.tw.jiracalc.beans.history.HistoryDetail;
import com.tw.jiracalc.beans.history.JiraCardHistory;
import com.tw.jiracalc.util.CardHelper;
import com.tw.jiracalc.util.TimeTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class JiraService {

    @Autowired
    RestTemplate restTemplate;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public JiraCards getCards(final String jql, final String jiraToken) {
        logger.info("JiraService.getCards starts");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", jiraToken);

        String url = "https://arlive.atlassian.net/rest/api/2/search?jql=" + jql;

        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<JiraCards> response = restTemplate.exchange(url, HttpMethod.GET, entity, JiraCards.class);

        logger.info("JiraService.getCards will return soon");
        return response.getBody();
    }

    @Async
    public CompletableFuture<Map<String, Double>> getCycleTime(final String jiraId, final String jiraToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", jiraToken);

        String url = "https://arlive.atlassian.net/rest/internal/2/issue/"+jiraId+"/activityfeed";
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<JiraCardHistory> response = restTemplate.exchange(url, HttpMethod.GET, entity, JiraCardHistory.class);

        JiraCardHistory jiraCardHistory = response.getBody();
        Map<String, Double> finalResult = CardHelper.calculateCycleTime(jiraCardHistory);

        return CompletableFuture.completedFuture(finalResult);
    }


    public JiraCards enrichCardDetail(final JiraCards jiraCards, final String jiraToken) {
        final Map<String, CompletableFuture<Map<String, Double>>> cycleTimeMap = new HashMap<>();
        logger.info("enrichCardDetail starts");
        jiraCards.getIssues().forEach(card -> cycleTimeMap.put(card.getKey(), this.getCycleTime(card.getKey(), jiraToken)));
        logger.info("enrichCardDetail ends");

        jiraCards.getIssues().forEach(card -> {
            Map<String, Double> cycleTime = null;
            try {
                cycleTime = cycleTimeMap.get(card.getKey()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            CycleTimeBean cycleTimeBean = new CycleTimeBean();
            cycleTimeBean.setCycleTime(cycleTime);
            card.getFields().setCycleTimeBean(cycleTimeBean);
        });

        logger.info("enrichCardDetail returns");
        return jiraCards;
    }
}
