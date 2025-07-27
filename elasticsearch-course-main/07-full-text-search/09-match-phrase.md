## Match Phrase

```
# delete index
DELETE /articles

# create index with the mapping
PUT /articles
{
  "mappings": {
    "properties": {
      "content": { "type" : "text"}
    }
  }
}

# insert records
POST /articles/_bulk
{ "create": {} }
{"content": "The rainy season can be a blessing or a curse."}
{ "create": {} }
{"content": "The winter season is often harsh and cold."}
{ "create": {} }
{"content": "Summer season brings hot and humid weather."}
{ "create": {} }
{"content": "The spring breeze is gentle and refreshing."}
{ "create": {} }
{"content": "Spring is the season of renewal and growth."}
{ "create": {} }
{"content": "The season of spring brings beautiful flowers and warm weather."}
{ "create": {} }
{"content": "I love spending time outdoors during the spring season."}
{ "create": {} }
{"content": "Spring is my favorite season of the year."}
{ "create": {} }
{ "content": "I lost my favorite hiking boot in the mud during spring" }
{ "create": {} }
{ "content": "The app was developed using spring boot framework" }
{ "create": {} }
{ "content": "He kicked off his boot and relaxed by the fire." }

# match phrase
POST /articles/_search
{
  "query": {
    "match_phrase": {
      "content": {
        "query": "spring season"
      }
    }
  }
}

# what about season of spring, spring is the season.. ?
POST /articles/_search
{
  "query": {
    "match_phrase": {
      "content": {
        "query": "spring season",
        "slop": 0
      }
    }
  }
}
```