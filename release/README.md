## What's this?
This is a tool to generate Jira Cycle Time report

## How to use this tool?

Open ```app``` folder and fill in the ```jql``` and ```token``` file, you can get your token at [here](https://id.atlassian.com/manage/api-tokens).
Then copy the token into token.txt with the format like:
```
jira-token: Paste-Your-Token-Here
```


Start application:
```
sh 1-startApp.sh
```

Get report, please be awared this step takes time and relies on how many stories/cards you will get:
```
sh 2-getJiraReport.sh
```

Then you can get the report which named **currentTimestamp**.csv

Finally you should stop the application by performing:
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
