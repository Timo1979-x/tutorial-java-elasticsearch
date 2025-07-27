## Boosting Query

-  To demote certain documents without excluding them from the search results.

```
# delete index
DELETE /products

# create index with the mapping
PUT /products
{
  "mappings": {
    "properties": {
      "name": { "type" : "text"},
      "size":  { "type" : "keyword"},
      "color":  { "type" : "keyword"},
      "brand":  { "type" : "keyword"},
      "price": { "type": "float" },
      "rating": { "type": "float" }
    }
  }
}

# insert records
POST /products/_bulk
{ "create": { "_id": "1" } }
{"name":"Tennis Shoe","size":["7","8","9","10"],"color":["Black","White","Grey"],"brand":"Nike", "price": 99.99, "rating": 4.8 }
{ "create": { "_id": "2" } }
{"name":"Running Shoe","size":["6","7","8","9","10"],"color":["Black","White","Blue"],"brand":"Nike", "price": 69.99, "rating": 3.6}
{ "create": { "_id": "3" } }
{"name":"Running and Training shoe","size":["6","7","8","9","10"],"color":["Red","White","Black"],"brand":"Nike", "price": 79.99, "rating": 4.3}
{ "create": { "_id": "4" } }
{"name":"Walking Shoe","size":["6","7","8","9","10"],"color":["White","Black","Red"],"brand":"Nike", "price": 39.99, "rating": 4.0}
{ "create": { "_id": "5" } }
{"name":"Winter Boots","size":["6","7","8","9","10"],"color":["Black","White","Blue"],"brand":"Timberland", "price": 65.99, "rating": 4.6}
{ "create": { "_id": "6" } }
{"name":"Hiking Boots","size":["7","8","9","10","11"],"color":["Brown","Black"],"brand":"Timberland", "price": 48.99, "rating": 3.8}
{ "create": { "_id": "7" } }
{"name":"Leather Fashion Boots","size":["7","8","9","10","11"],"color":["Brown","Black"],"brand":"Timberland", "price": 110.99, "rating": 4.8}

# search for boots
POST /products/_search
{
  "query": {
    "match": {
      "name": "boots"
    }
  }
}

# reduces the relevance score for documents matching the "negative" clause, while still allowing them to appear in the results.
POST  /products/_search
{
  "query": {
    "boosting": {
      "positive": {
        "match": {
          "name": "boots"
        }
      }, 
      "negative": {
        "match": {
          "name": "winter"
        }
      },
      "negative_boost": 0.2
    }
  }
}

# positive query can be select all
POST  /products/_search
{
  "query": {
    "boosting": {
      "positive": {
        "match_all": {}
      }, 
      "negative": {
        "match": {
          "name": "tennis"
        }
      },
      "negative_boost": 0.2
    }
  }
}
```