## What's this?
This is a tool to generate Jira Cycle Time report

## How to use this tool?
Start application:
```
sh 1-startApp.sh
```

Get report, please be awared this step takes time and relies on how many stories/cards you will get:
```
sh 2-getJiraReport.sh
```

Then you can get the report which named **4-result.csv**

Finally you should stop the application by performing:
```
sh 3-stopApp.sh
```

# Advance
## Precondition
Please make sure you have installed JVM before starting application.

## How to change the result scope?
Open ./app/jql.txt and change the filter condition

The content is like:
```jql: project = JUNO AND assignee in (Yunpeng.Ding, Hongxing.Chen, Qilin.Lou, Jingtian.Mo, Jingjun.Zhang, Liang.Cui) ORDER BY priority DESC```