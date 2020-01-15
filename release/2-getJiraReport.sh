now="$(date +"%Y-%m-%d %H:%M:%S")"
#echo $now
curl -X GET http://HOST:8081/jira/getCardsFile -H "$(cat ./env/jql.txt)" -H "$(cat ./env/token.txt)" -H "$(cat ./env/stages.txt)" > $now.csv