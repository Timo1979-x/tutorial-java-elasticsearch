## Custom Analyzer

```
# delete index
DELETE /my-index

# create an index with custom analyzer
PUT /my-index
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0,
    "analysis": {
      "analyzer": {
        "my-custom-analyzer": {
          "char_filter": [
            "html_strip"
          ],
          "tokenizer": "standard",
          "filter": [
            "uppercase"
          ]
        }
      }
    }
  }
}

# test
POST /my-index/_analyze
{
    "text": "<b>hello world</b>",
    "analyzer": "my-custom-analyzer"
}
```