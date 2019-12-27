## What's this?
This is a tool to generate Jira Cycle Time report

## How to use this tool?

### Get Token

1. Generate ```jira API token``` at [here](https://id.atlassian.com/manage/api-tokens).
2. Get Jira Token. Once getting ```Jira API token```, generate jira-token by executing
    ```
    curl -v https://arlive.atlassian.net/ --user [Your-Email]:[Jira-API-Token]
    ```
    The Jira Token looked like ```Basic cWlsaW...```

    Then copy the token into token.txt with the format like:
    ```
    jira-token: Paste-Your-Token-Here
    ```

### Config JQL

Update ```.app/jql.txt``` file to satisfy your team.

### Config Stages

Update ```.app/stages.txt``` file to satisfy your team.


### Start application:
```
sh 1-startApp.sh
```

### Get report:
```
sh 2-getJiraReport.sh
```

Then you can get the report which named **currentTimestamp**.csv

### Stop the application:
```
sh 3-stopApp.sh
```

# Advance
## Precondition
Please make sure you have installed JVM before starting application.

## How to change the result scope?
Open ./app/jql.txt and change the filter condition

### TianZhou
#### JQL:
```
jql: (assignee in (Liang.Cui, Jingjun.Zhang, Qilin.Lou, Jingtian.Mo, Hongxing.Chen, Yunpeng.Ding) OR assignee was in (Liang.Cui, Jingjun.Zhang, Qilin.Lou, Jingtian.Mo, Hongxing.Chen, Yunpeng.Ding)) AND status changed to done before now() after -30d  ORDER BY priority DESC
```

#### Stages:
```
card-stage: Backlog, Analysis, Selected for Development, In-Progress, Showcase, Done
```

### ShenZhou
#### JQL:
```
jql: (assignee in (Jianing.Zheng, Minghao.Tan, Jie.Chen, Yingjuan.Wang, pei.wang2) OR assignee was in (Jianing.Zheng, Minghao.Tan, Jie.Chen, Yingjuan.Wang, pei.wang2)) AND status changed to Complete before now() after -30d  ORDER BY priority DESC
```

#### Stages:
```
card-stage: Backlog, Ready for Dev, In-Progress, Waiting (other dependancy), Complete
```

### YiLong
#### JQL:
```
jql: (assignee in (siyu.gou, sebastian.thiel, ke.liu,Hanxian.Lin,nan.li,junli.li,lin.fang) OR assignee was in (sebastian.thiel, ke.liu,Hanxian.Lin,nan.li,junli.li,lin.fang)) ORDER BY priority DESC
```

#### Stages:
```
card-stage: To Do, In Progress, TEST, Ready For Release, Done
```