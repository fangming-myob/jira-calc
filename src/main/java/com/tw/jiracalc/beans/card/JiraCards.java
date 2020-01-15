package com.tw.jiracalc.beans.card;

import lombok.Data;

import java.util.List;

@Data
public class JiraCards {
    private int maxResults;
    private int total;
    private List<JiraCard> issues;

}
