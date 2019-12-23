package com.tw.jiracalc.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.tw.jiracalc.beans.CycleTimeBean;
import com.tw.jiracalc.beans.JiraCards;
import com.tw.jiracalc.service.FileService;
import com.tw.jiracalc.service.JiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class JiraController {

    @Autowired
    JiraService jiraService;

    @Autowired
    FileService fileService;

    @Value("${cloud-session-token}")
    private String cloudSessionToken;

    @GetMapping(value = "/getCardsFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    byte[] getCardsFile(@RequestHeader Map<String, String> header) {
        JiraCards jiraCards = enrichCardDetail(jiraService.getCards(header.get("jql")));
        String csv = fileService.generateFile(jiraCards);
        return csv.getBytes();
    }

    private JiraCards enrichCardDetail(JiraCards jiraCards) {
        final Map<String, CompletableFuture<Map<String, Long>>> cycleTimeMap = new HashMap<>();

        jiraCards.getIssues().forEach(card -> {
            final CompletableFuture<Map<String, Long>> futureCycleTime = jiraService.getCycleTime(card.getKey());
            cycleTimeMap.put(card.getKey(), futureCycleTime);
        });

        jiraCards.getIssues().forEach(card -> {
            Map<String, Long> cycleTime = null;
            try {
                cycleTime = cycleTimeMap.get(card.getKey()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            CycleTimeBean cycleTimeBean = new CycleTimeBean();
            cycleTimeBean.setCycleTime(cycleTime);
            card.getFields().setCycleTimeBean(cycleTimeBean);
        });

        return jiraCards;
    }

    /**
     * This method based on Jira transition feature
     */
    @Deprecated
    private JiraCards enrichCardDetail(JiraCards jiraCards, String cloudSessionToken) {
        final Map<String, CompletableFuture<Map<String, Long>>> transitionMap = new HashMap<>();

        jiraCards.getIssues().forEach(card -> {
            try {
                final CompletableFuture<Map<String, Long>> futureTransition = jiraService.getTransition(cloudSessionToken,
                        card.getKey());
                transitionMap.put(card.getKey(), futureTransition);
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        });

        jiraCards.getIssues().forEach(card -> {
            Map<String, Long> cycleTime = null;
            try {
                cycleTime = transitionMap.get(card.getKey()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            CycleTimeBean cycleTimeBean = new CycleTimeBean();
            cycleTimeBean.setCycleTime(cycleTime);
            card.getFields().setCycleTimeBean(cycleTimeBean);
        });

        return jiraCards;
    }
}
