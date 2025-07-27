## Explicit Mapping

### Demo

```
# delete index
DELETE /customers

# create index with explicit mapping
PUT /customers
{
  "mappings": {
    "properties": {
      "name": {
        "type": "text"
      },
      "age": {
        "type": "integer"
      },
      "email": {
        "type": "keyword"
      }
    }
  }
}

# get mapping
GET /customers/_mapping

# bulk insert
POST /customers/_bulk
{ "create": {} }
{ "name": "John Doe", "age": 15, "email": "john@doe.com" }
{ "create": {} }
{ "name": "Jane Smith", "age": 25, "email": "jane@smith.com" }
{ "create": {} }
{ "name": "Alice Brown", "age": 30, "email": "alice@brown.com" }

# query all
GET /customers/_search

# query by name / email domain
# this is a simple demo. for more complex search queries, be patient
GET /customers/_search?q=brown
GET /customers/_search?q=brown.com

# I can still insert a doc with a new field
POST /customers/_doc
{
 "name": "Michael Jackson",
 "age": "50",
 "phone": 1234567890
}

# get mapping
GET /customers/_mapping

# what if I want to add a new field with a type
PUT /customers/_mappings
{
  "properties": {
    "city": {
      "type": "text"
    }
  }
}
```