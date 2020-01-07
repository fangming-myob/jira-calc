now="$(date +"%Y-%m-%d %H:%M:%S")"
#echo $now
curl -X GET https://service-pc8cr97p-1256727478.bj.apigw.tencentcs.com/release/jira/getCardsFile -H "$(cat ./app/jql.txt)" -H "$(cat ./app/token.txt)" -H "$(cat ./app/stages.txt)" > $now.csv