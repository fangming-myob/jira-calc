package com.tw.jiracalc.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tw.jiracalc.Constant;
import com.tw.jiracalc.beans.JiraCards;
import com.tw.jiracalc.beans.history.HistoryDetail;
import com.tw.jiracalc.beans.history.JiraCardHistory;
import com.tw.jiracalc.util.TimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static com.mashape.unirest.http.Unirest.get;

@Service
public class JiraService {

    @Autowired
    RestTemplate restTemplate;

    public JiraCards getCards(final String jql, final String jiraToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", jiraToken);

        String url = "https://arlive.atlassian.net/rest/api/2/search?jql=" + jql;

        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<JiraCards> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                JiraCards.class);

        return response.getBody();
    }

    @Async
    public CompletableFuture<Map<String, Long>> getTransition(final String cloudSessionToken, final String jiraId) throws UnirestException {
        System.out.println("getTransition: " + Thread.currentThread());
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.set("cookie", "cloud.session.token=" + cloudSessionToken);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://arlive.atlassian.net/rest/gira/1/");
        String requestBody = "{\"query\":\"\\n    query issueViewInteractiveQuery($issueKey: String!) {\\n        viewIssue(issueKey: $issueKey) {\\n            \\n    comments (first: 0, maxResults: 5, orderBy: \\\"-created\\\") {\\n        nodes {\\n            id\\n            author {\\n                accountId\\n                name\\n                displayName\\n                avatarUrl\\n            }\\n            updateAuthor {\\n                accountId\\n                name\\n                displayName\\n                avatarUrl\\n            }\\n            visibility {\\n                type\\n                value\\n            }\\n            created\\n            updated\\n            body\\n            renderedBody\\n            jsdPublic\\n            bodyAdf\\n            \\n        }\\n        totalCount\\n    }\\n\\n            \\n    ecosystem {\\n      operations {\\n        name\\n        url: href\\n        icon {\\n          url\\n        }\\n        tooltip\\n        styleClass\\n      }\\n      contentPanels {\\n        name\\n        iframe {\\n          addonKey\\n          moduleKey\\n          options\\n        }\\n        icon {\\n          url\\n        }\\n        tooltip\\n        type\\n        showByDefault\\n        manuallyAddedToIssue\\n      }\\n      activityPanels {\\n        name\\n        iframe {\\n          addonKey\\n          moduleKey\\n          options\\n        }\\n      }\\n      \\n      ecoSystemOnDate\\n      contentPanelsCustomised \\n    }\\n\\n            fields {\\n                \\n__typename \\n  ... on AttachmentsField {\\n    fieldKey\\n    value {\\n      id\\n      mediaApiFileId\\n      createdDate\\n      \\n      accountId\\n      mimetype\\n      filename\\n    }\\n  }\\n\\n                \\n      ... on GlancesField {\\n        fieldKey\\n        value {\\n          name\\n          status\\n          iframe {\\n            addonKey\\n            moduleKey\\n            options\\n          }\\n          icon {\\n            url\\n          }\\n          content\\n          type\\n        }\\n      }\\n\\n            }\\n        }\\n        \\n    jiraSettings {\\n      \\n      timeTracking {\\n        isTimeTrackingEnabled\\n        hoursPerDay\\n        daysPerWeek\\n        defaultUnit\\n      }\\n\\n      issueLinkTypes {\\n        id\\n        name\\n        inward\\n        outward\\n      }\\n    }\\n\\n        \\n    permissions(issueKey:$issueKey, keys:[\\\"ADMINISTER\\\",\\\"ADMINISTER_PROJECTS\\\",\\\"ASSIGN_ISSUES\\\",\\\"EDIT_ISSUES\\\",\\\"SCHEDULE_ISSUES\\\",\\\"ADD_COMMENTS\\\",\\\"DELETE_ALL_COMMENTS\\\",\\\"DELETE_OWN_COMMENTS\\\",\\\"EDIT_ALL_COMMENTS\\\",\\\"EDIT_OWN_COMMENTS\\\",\\\"CREATE_ISSUES\\\",\\\"DELETE_ISSUES\\\",\\\"MOVE_ISSUES\\\",\\\"CREATE_ATTACHMENTS\\\",\\\"DELETE_ALL_ATTACHMENTS\\\",\\\"DELETE_OWN_ATTACHMENTS\\\",\\\"WORK_ON_ISSUES\\\",\\\"ASSIGNABLE_USER\\\",\\\"LINK_ISSUES\\\",\\\"VIEW_VOTERS_AND_WATCHERS\\\",\\\"MANAGE_WATCHERS\\\",\\\"EDIT_ALL_WORKLOGS\\\",\\\"EDIT_OWN_WORKLOGS\\\",\\\"DELETE_ALL_WORKLOGS\\\",\\\"DELETE_OWN_WORKLOGS\\\",\\\"USER_PICKER\\\",\\\"VIEW_DEV_TOOLS\\\"]) {\\n        key\\n        havePermission\\n    }\\n\\n        \\n    myPreferences(keys: [\\\"jira.user.issue.changeboarding.lastinteracted\\\", \\\"jira.user.issue.clickthrough-banner.lastinteracted\\\"])\\n\\n        \\n    }\\n\",\"variables\":{\"issueKey\":\"" + jiraId + "\"}}";

        HttpEntity entity = new HttpEntity<>(requestBody, headers);
        HttpEntity<String> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                String.class);

        String responseBody = response.getBody();
        assert responseBody != null;
        final int transitionUrlIndex = responseBody.indexOf("https://mwec-production.herokuapp.com/panels/transition-history");
        final int transitionUrlEndIndex = responseBody.substring(transitionUrlIndex).indexOf("\\\"");
        String url = responseBody.substring(transitionUrlIndex, transitionUrlIndex + transitionUrlEndIndex);

        Map<String, Long> cycleTime = new HashMap<>();
        HttpResponse<String> transitionResponse;
        transitionResponse = get(url)
                .header("User-Agent", "PostmanRuntime/7.20.1")
                .header("Accept", "*/*")
                .header("Cache-Control", "no-cache")
                .header("Host", "mwec-production.herokuapp.com")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Connection", "keep-alive")
                .header("cache-control", "no-cache")
                .asString();

        String[] sections = transitionResponse.getBody().split("<span class='jira-issue-status-tooltip-title'>");
        String prevStage = "";
        for (int i = 2; i < sections.length; i++) {
            String section = sections[i];
            if (i % 2 == 0) {
                prevStage = section.substring(0, section.indexOf("</span>"));
            }

            if (section.contains("<duration ms=\"")) {
                String duration = section.substring(section.indexOf("<duration ms=\"") + 14, section.indexOf("\"></duration>"));

                if (cycleTime.get(prevStage) == null) {
                    cycleTime.put(prevStage, Long.parseLong(duration));
                } else {
                    Long sumDuration = cycleTime.get(prevStage) + Long.parseLong(duration);
                    cycleTime.put(prevStage, sumDuration);
                }
            }
        }

        return CompletableFuture.completedFuture(cycleTime);
    }

    @Async
    public CompletableFuture<Map<String, Long>> getCycleTime(final String jiraId, final String jiraToken) {
        Map<String, Long> result = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization", jiraToken);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://arlive.atlassian.net/rest/internal/2/issue/"+jiraId+"/activityfeed");
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<JiraCardHistory> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                JiraCardHistory.class);

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
            Long costHour = result.get(currentActivity.getTo().getDisplayValue());

            if (null == nextActivity) {
                if (costHour != null) {
                    costHour += TimeTool.getWorkDay(System.currentTimeMillis(), currentActivity.getTimestamp());
                } else {
                    costHour = TimeTool.getWorkDay(System.currentTimeMillis(), currentActivity.getTimestamp());
                }
            } else {
                if (costHour != null) {
                    costHour += subtract(nextActivity, currentActivity);
                } else {
                    costHour = subtract(nextActivity, currentActivity);
                }
            }
            result.put(currentActivity.getTo().getDisplayValue(), costHour);
        }
        return CompletableFuture.completedFuture(result);
    }

    private static Long subtract(HistoryDetail left, HistoryDetail right) {
        return TimeTool.getWorkDay(left.getTimestamp(), right.getTimestamp());
    }

    private static Long msToHour(Long ms) {
        return ms / 1000 / 60 / 60;
    }
}
