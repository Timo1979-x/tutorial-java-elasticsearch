## Match Query


### Introduction To Relevance Scoring

```
# below demo is similar to hello world demo to show how the relevance score works

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
{"content": "spring"}
{ "create": {} }
{"content": "spring spring"}
{ "create": {} }
{"content": "spring spring spring"}
{ "create": {} }
{"content": "spring boot"}

/*
    search for spring, spring boot, boot spring, boot
*/
POST /articles/_search
{
    "query": {
        "match": {
          "content": ""
        }
    }
}
```

### Demo 2

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

POST /articles/_search
{
  "query": {
    "match": {
      "content": "boot"
    }
  }
}

POST /articles/_search
{
  "query": {
    "match": {
      "content": "BOOT"
    }
  }
}

# it is spring or season
POST /articles/_search
{
  "query": {
    "match": {
      "content": "spring season"
    }
  }
}

POST /articles/_search
{
  "query": {
    "match": {
      "content": "spring boot"
    }
  }
}

# match all the given words
POST /articles/_search
{
  "query": {
    "match": {
      "content": {
        "query": "spring boot",
        "operator": "and"
      }
    }
  }
}
```
