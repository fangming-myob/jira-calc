now="$(date +"%Y-%m-%d %H:%M:%S")"
#echo $now
curl -X GET localhost:8081/jira/getCardsFile -H "$(cat ./app/jql.txt)" -H "$(cat ./app/token.txt)" -H "$(cat ./app/stages.txt)" > $now.csv