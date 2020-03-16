package com.tw.jiracalc.service;

import com.tw.jiracalc.beans.card.JiraCards;
import com.tw.jiracalc.beans.cycletime.CycleTimeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class JiraService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CardHttpService cardHttpService;

    public JiraCards getCards(final String jql, final String jiraApiToken) {
        logger.info("enrichCardDetail starts");
        final Map<String, CompletableFuture<Map<String, Double>>> cycleTimeMap = new HashMap<>();
        final JiraCards jiraCards = cardHttpService.getCards(jql, jiraApiToken);
        jiraCards.getIssues().forEach(card -> cycleTimeMap.put(card.getKey(), cardHttpService.getCycleTime(card.getKey(), jiraApiToken)));
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
