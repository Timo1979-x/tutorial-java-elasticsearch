## Multi Field Mapping

```
# delete index
DELETE /jobs

# create index with the mapping
PUT /jobs
{
  "mappings": {
    "properties": {
      "title": {
        "type": "text",
        "fields": {
          "raw": {
            "type": "keyword"
          }
        }
      }
    }
  }
}

# insert records
POST /jobs/_bulk
{"index":{}}
{"title": "Software Engineer"}
{"index":{}}
{"title": "Cloud Engineer"}
{"index":{}}
{"title": "Data Scientist"}
{"index":{}}
{"title": "Marketing Manager"}
{"index":{}}
{"title": "DevOps Engineer"}

# match query - no exact match for large text fields
POST /jobs/_search
{
  "query": {
    "match": {
      "title": "Cloud Engineer"
    }
  }
}

# term query
POST /jobs/_search
{
  "query": {
    "term": {
      "title.raw": "Cloud Engineer"
    }
  }
}
```