MEALS REST API 

**Get all meals** 

curl --location --request GET 'http://localhost:8080/topjava/rest/meals/'

**Example of response**

[
    {
        "id": 100008,
        "dateTime": "2020-01-31T20:00:00",
        "description": "Ужин",
        "calories": 510,
        "excess": true
    },
    {
        "id": 100007,
        "dateTime": "2020-01-31T13:00:00",
        "description": "Обед",
        "calories": 1000,
        "excess": true
    }
]

**Get meal via id**

curl --location --request GET 'http://localhost:8080/topjava/rest/meals/100008'

**Example of response**

[
    {
        "id": 100008,
        "dateTime": "2020-01-31T20:00:00",
        "description": "Ужин",
        "calories": 510,
        "excess": true
    }
]

**Create meal**

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

**Update meal via id**

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

**Delete meal via id**

curl --location --request DELETE 'http://localhost:8080/topjava/rest/meals/100011'

