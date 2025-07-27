## Disjunction Max

```
# delete index
DELETE /articles

# create index with the mapping
PUT /articles
{
  "mappings": {
    "properties": {
      "title": { "type" : "text"}
    }
  }
}

# insert records
POST /articles/_bulk
{ "create": {} }
{ "title": "Spring Framework" }
{ "create": {} }
{ "title": "Spring Boot"}
{ "create": {} }
{ "title": "Spring Season"}
{ "create": {} }
{ "title": "Spring Cleaning"}
{ "create": {} }
{ "title": "Spring Festival" }
{ "create": {} }
{ "title": "Spring Activities" }
{ "create": {} }
{ "title": "Spring Fashion Trends" }
{ "create": {} }
{ "title": "Spring Health Tips" }
{ "create": {} }
{ "title": "Spring Recipes" }
{ "create": {} }
{ "title": "Spring Gardening"}
{ "create": {} }
{ "title": "Spring Java Development" }

# if we search for "spring framework", we get all the results
POST /articles/_search
{
  "query": {
    "match": {
      "title": {
        "query": "spring framework"
      }
    }
  }
}

# we can use "and" to get the results related to spring framework
# but we get only one
POST /articles/_search
{
  "query": {
    "match": {
      "title": {
        "query": "spring framework",
        "operator": "and"
      }
    }
  }
}

# Best for cases where you have different search variations (Ex: synonyms, related terms, alternative phrases) to match documents.
POST /articles/_search
{
  "query": {
    "dis_max": {
      "queries": [
        {
          "match": {
            "title": {
              "query": "spring framework",
              "operator": "and"
            }
          }
        },
        {
          "match": {
            "title": {
              "query": "spring boot",
              "operator": "and"
            }
          }
        },        
        {
          "match": {
            "title": {
              "query": "spring java",
              "operator": "and"
            }
          }
        }
      ],
      "tie_breaker": 0.3
    }
  }
}
```