## Bool Query Assignments

### Assignment 1
- I am looking for `shoe`.
- I do not want to pay more than $100.
- The rating should be at least 4.
- It would be nice if something is available for under $50.

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


POST /products/_search
{
  "query": {
    "bool": {
      "filter": [
        {
          "range": {
            "price": {
              "lte": 100
            }
          }
        },
        {
          "range": {
            "rating": {
              "gte": 4
            }
          }
        }        
      ],
      "must": [
        {
          "match": {
            "name": "shoe"
          }
        }
      ],
      "should": [
        {
          "range": {
            "price": {
              "lte": 50
            }
          }
        }
      ]
    }
  }
```

### Assignment 2

- I am looking for either `shoe which are below $60` or `boots with at least 4.5 rating`

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

POST /products/_search
{
  "query": {
    "bool": {
      "should": [
        {
          "bool": {
            "filter": [
              {
                "range": {
                  "price": {
                    "lt": 60
                  }
                }
              }     
            ],
            "must": [
              {
                "match": {
                  "name": "shoe"
                }
              }
            ]
          }
        },
        {
          "bool": {
            "filter": [
              {
                "range": {
                  "rating": {
                    "gte": 4.5
                  }
                }
              }     
            ],
            "must": [
              {
                "match": {
                  "name": "boots"
                }
              }
            ]
          }
        }        
      ]
    }
  }
}
```