## Multi Match

- [Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-multi-match-query.html)

```
# delete index
DELETE /articles

# create index with the mapping
PUT /articles
{
  "mappings": {
    "properties": {
      "title": { "type" : "text"},
      "body": { "type": "text"}
    }
  }
}

# insert records
POST /articles/_bulk
{ "create": {} }
{ "title": "The Rainy Season", "body": "The rainy season can be a blessing or a curse."}
{ "create": {} }
{ "title": "The Winter season", "body": "The winter season is often harsh and cold."}
{ "create": {} }
{ "title": "Summer Season", "body": "Summer season brings hot and humid weather."}
{ "create": {} }
{ "title": "The Spring Breeze", "body": "The spring breeze is gentle and refreshing."}
{ "create": {} }
{ "title": "Season of Spring", "body": "Spring is the season of renewal and growth."}
{ "create": {} }
{ "title": "Spring Season", "body": "It brings beautiful flowers and warm weather."}
{ "create": {} }
{ "title": "About Me", "body": "I love spending time outdoors during the spring season."}
{ "create": {} }
{ "title": "My favorite season", "body": "Spring is my favorite season of the year."}
{ "create": {} }
{ "title": "Boot Missing", "body": "I lost my favorite hiking boot in the mud during spring" }
{ "create": {} }
{ "title": "Introduction To Spring Framework", "body": "The app was developed using spring boot framework" }
{ "create": {} }
{ "title": "The Bike Rider", "body": "He kicked off his boot and relaxed by the fire." }

# we try to match spring OR season in the body
POST /articles/_search
{
  "query": {
    "multi_match": {
      "query": "spring season",
      "fields": [ "body" ]
    }
  }
}

# we try to match spring AND season in the body
POST /articles/_search
{
  "query": {
    "multi_match": {
      "query": "spring season",
      "fields": [ "body" ],
      "operator": "and"
    }
  }
}

# lets include title as well
POST /articles/_search
{
  "query": {
    "multi_match": {
      "query": "spring season",
      "fields": [ "title", "body" ],
      "operator": "and"
    }
  }
}

# we can give more weight to title.
POST /articles/_search
{
  "query": {
    "multi_match": {
      "query": "spring season",
      "fields": [ "title^3", "body" ],
      "operator": "and"
    }
  }
}

# types - best fields
POST /articles/_search
{
  "query": {
    "multi_match": {
      "query": "spring season",
      "fields": [ "title", "body" ],
      "operator": "and",
      "type": "best_fields", // default - check the score
      "tie_breaker": 0 // default 0
    }
  }
}

# types - most fields
POST /articles/_search
{
  "query": {
    "multi_match": {
      "query": "spring season",
      "fields": [ "title", "body" ],
      "operator": "and",
      "type": "most_fields"
    }
  }
}

# fuzziness
POST /articles/_search
{
  "query": {
    "multi_match": {
      "query": "seasen",
      "fields": [ "title", "body" ],
      "operator": "and",
      "fuzziness": 1
    }
  }
}

# phrase - (it also supports slop)
POST /articles/_search
{
  "query": {
    "multi_match": {
      "query": "spring season",
      "type": "phrase", 
      "fields": [ "title", "body" ],
      "slop": 0
    }
  }
}
```