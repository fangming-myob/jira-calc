package com.tw.jiracalc.controller;

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

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class JiraController {

    @Autowired
    JiraService jiraService;

    @Autowired
    FileService fileService;

    @Value("${cloud-session-token}")
    private String cloudSessionToken;

    @GetMapping(value = "/cardCycleTime")
    Map<String, Double> getCardCycleTime(@RequestHeader Map<String, String> header) {
        final String jiraToken = header.get("jira-token");
        final String jiraId = header.get("jira-id");
        return jiraService.getCardCycleTime(jiraId, jiraToken);
    }

    @GetMapping(value = "/getCardsFile", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    byte[] getCardsFile(@RequestHeader Map<String, String> header) {
        final String jiraToken = header.get("jira-token");
        final String jql = header.get("jql");
        final List<String> cardStages = new ArrayList<>(Arrays.asList(header.get("card-stage").split(",")))
                .stream().map(String::trim).collect(Collectors.toList());

        JiraCards jiraCards = enrichCardDetail(jiraService.getCards(jql, jiraToken), jiraToken);
        String csv = fileService.generateFile(jiraCards, cardStages);
        return csv.getBytes();
    }

    private JiraCards enrichCardDetail(final JiraCards jiraCards, final String jiraToken) {
        final Map<String, CompletableFuture<Map<String, Double>>> cycleTimeMap = new HashMap<>();

        jiraCards.getIssues().forEach(card -> {
            final CompletableFuture<Map<String, Double>> futureCycleTime = jiraService.getCycleTime(card.getKey(), jiraToken);
            cycleTimeMap.put(card.getKey(), futureCycleTime);
        });

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

        return jiraCards;
    }

    /**
     * This method based on Jira transition feature
     */
//    @Deprecated
//    private JiraCards enrichCardDetailByTransition(JiraCards jiraCards, String cloudSessionToken) {
//        final Map<String, CompletableFuture<Map<String, Float>>> transitionMap = new HashMap<>();
//
//        jiraCards.getIssues().forEach(card -> {
//            try {
//                final CompletableFuture<Map<String, Float>> futureTransition = jiraService.getTransition(cloudSessionToken,
//                        card.getKey());
//                transitionMap.put(card.getKey(), futureTransition);
//            } catch (UnirestException e) {
//                e.printStackTrace();
//            }
//        });
//
//        jiraCards.getIssues().forEach(card -> {
//            Map<String, Float> cycleTime = null;
//            try {
//                cycleTime = transitionMap.get(card.getKey()).get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//            CycleTimeBean cycleTimeBean = new CycleTimeBean();
//            cycleTimeBean.setCycleTime(cycleTime);
//            card.getFields().setCycleTimeBean(cycleTimeBean);
//        });
//
//        return jiraCards;
//    }
}
