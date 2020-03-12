package com.tw.jiracalc.controller;

import com.tw.jiracalc.beans.card.JiraCards;
import com.tw.jiracalc.beans.cycletime.CycleTimeBean;
import com.tw.jiracalc.service.FileService;
import com.tw.jiracalc.service.JiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/jira")
public class JiraController {

    @Autowired
    JiraService jiraService;

    @GetMapping(value = "/getCardsFile")
    String getCardsFile(@RequestHeader Map<String, String> header) {
        final List<String> cardStages = new ArrayList<>(Arrays.asList(header.get("card-stage").split(",")))
                .stream().map(String::trim).collect(Collectors.toList());
        return FileService.generateCycleTimeFile(
                enrichCardDetail(jiraService.getCards(header.get("jql"), header.get("jira-token")), header.get("jira-token")),
                cardStages);
    }

    private JiraCards enrichCardDetail(final JiraCards jiraCards, final String jiraToken) {
        final Map<String, CompletableFuture<Map<String, Double>>> cycleTimeMap = new HashMap<>();

        jiraCards.getIssues().forEach(card -> {
            cycleTimeMap.put(card.getKey(), jiraService.getCycleTime(card.getKey(), jiraToken));
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

}
