## Exists

- Used to check for the presence of a field in the documents.
- Similar to `SELECT * FROM customers WHERE name IS NOT NULL`.

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

# insert records - the last 2 records do not have brand
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
{"name":"Hiking Boots","size":["7","8","9","10","11"],"color":["Brown","Black"], "brand": null }
{ "create": { "_id": "7" } }
{"name":"Formal Shoe","size":["7","8","9","10","11"],"color":["Brown","Black"] }

# Get the records with the brand
POST /products/_search
{
  "query": {
    "exists": {
      "field": "brand"
    }
  }
}

# what about NOT exists? 
# how to get the records which do not have brand (!) - it is slightly different - we have to wait for now
```