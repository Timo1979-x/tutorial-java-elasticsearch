## Term Query

- Used for exact matches.
- Best suited for `keyword` fields and not ideal for `text` fields because `text` fields are analyzed (tokens).
- Similar to `SELECT * FROM customers WHERE name = 'sam'` in SQL.


### Demo

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
      "brand":  { "type" : "keyword"}
    }
  }
}

# insert records
POST /products/_bulk
{ "create": { "_id": "1" } }
{"name":"Air Max 270","size":["7","8","9","10"],"color":["Black","White","Grey"],"brand":"Nike"}
{ "create": { "_id": "2" } }
{"name":"Adirush Running Shoe","size":["6","7","8","9","10"],"color":["Black","White","Blue"],"brand":"Adidas"}
{ "create": { "_id": "3" } }
{"name":"Running and Training shoe","size":["6","7","8","9","10"],"color":["Red","White","Black"],"brand":"Puma"}
{ "create": { "_id": "4" } }
{"name":"Fusion Lux Walking Shoe","size":["6","7","8","9","10"],"color":["White","Black","Red"],"brand":"Reebok"}
{ "create": { "_id": "5" } }
{"name":"Old Skool Casual","size":["6","7","8","9","10"],"color":["Black","White","Blue"],"brand":"Vans"}
{ "create": { "_id": "6" } }
{"name":"Hiking Boots","size":["7","8","9","10","11"],"color":["Brown","Black"],"brand":"Timberland"}
{ "create": { "_id": "7" } }
{"name":"Formal Shoe","size":["7","8","9","10","11"],"color":["Brown","Black"],"brand":"Clarks"}

# find by term (keyword field)
POST /products/_search
{
  "query": {
    "term": {
      "color": {
        "value": "Blue"
      }
    }
  }
}

# this will not return results
POST /products/_search
{
  "query": {
    "term": {
      "color": {
        "value": "blue"
      }
    }
  }
}

# this will work as we make it case insensitive
POST /products/_search
{
  "query": {
    "term": {
      "color": {
        "value": "blue",
        "case_insensitive": true
      }
    }
  }
}

# how can we use multiple colors. the below appoach wil not work
POST /products/_search
{
  "query": {
    "term": {
      "color": {
        "value": [ "blue", "black" ],
        "case_insensitive": true
      }
    }
  }
}

# we have "terms" - but it is case-senstive.
POST /products/_search
{
  "query": {
    "terms": {
      "color": [ "Blue", "Black" ]
    }
  }
}
```