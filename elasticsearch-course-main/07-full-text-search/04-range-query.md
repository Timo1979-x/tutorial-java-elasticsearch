## Range Query

- Used to filter documents based on a range of values.
- Similar to `SELECT * FROM customers WHERE age > 5 AND age < 10`.

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
      "price":  { "type" : "double"}
    }
  }
}

# insert records
POST /products/_bulk
{ "create": { "_id": "1" } }
{"name":"Air Max 270", "price": 99.99 }
{ "create": { "_id": "2" } }
{"name":"Adirush Running Shoe","price": 56.99 }
{ "create": { "_id": "3" } }
{"name":"Running and Training shoe", "price": 43.50 }
{ "create": { "_id": "4" } }
{"name":"Fusion Lux Walking Shoe", "price": 63.65 }
{ "create": { "_id": "5" } }
{"name":"Old Skool Casual", "price": 68.99 }
{ "create": { "_id": "6" } }
{"name":"Hiking Boots", "price": 34.99 }
{ "create": { "_id": "7" } }
{"name":"Formal Shoe", "price": 86.99 }

# field <= NUM
POST /products/_search
{
  "query": {
    "range": {
      "price":{
        "lte": 50
      }
    }
  }
}

# field >= NUM1 and field <= NUM2
POST /products/_search
{
  "query": {
    "range": {
      "price":{
        "gte": 50,
        "lte": 80
      }
    }
  }
}

# https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-term-query.html
# We have to use term query for exact match like price, date etc
# Use term query or this for exact match in this case
POST /products/_search
{
  "query": {
    "range": {
      "price":{
        "gte": 68.99,
        "lte": 68.99
      }
    }
  }
}

# we can also the range queries for date fields
# date should be in this format 'yyyy/MM/dd' or 'yyyy/MM/dd HH:mm:ss' or epoch
```