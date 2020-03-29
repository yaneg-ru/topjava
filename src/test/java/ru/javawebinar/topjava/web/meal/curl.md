curl --location --request GET 'http://localhost:8080/topjava/rest/meals/'

curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100008'

curl --location --request POST 'http://localhost:8080/topjava/rest/meals/' \
--header 'Content-Type: application/json' \
--data-raw '
{
    "id": null,
    "dateTime": "2020-02-15T20:00:00",
    "description": "RestУжин",
    "calories": 510,
    "user": null
}'

curl --location --request PUT 'http://localhost:8080/topjava/rest/meals/100011' \
--header 'Content-Type: application/json' \
--data-raw '
{
    "id": 100011,
    "dateTime": "2020-02-15T20:00:00",
    "description": "UpdateRestУжин",
    "calories": 510,
    "user": null
}'

curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100011'

