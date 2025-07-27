## Date Math

- [Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/common-options.html#date-math)

### Note
The demo uses data with specific dates and times. When you copy and test it in Elasticsearch, the results might not match your expectations. Please follow the lecture carefully to ensure it works correctly.

```
# delete index
DELETE /orders

# create index with the mapping
PUT /orders
{
  "mappings": {
    "properties": {
      "customer": { "type" : "text"},
      "product":  { "type" : "text"},
      "order_date":  { "type" : "date"},
      "shipped":  { "type" : "boolean"},
      "total_amount": { "type": "double" }
    }
  }
}

# insert records
POST /orders/_bulk
{ "create": { } }
{ "customer": "Tom", "order_date": "2025-01-28", "shipped": false, "total_amount": 120.00, "product": "Monitor" }
{ "create": {  } }
{ "customer": "Eve", "order_date": "2025-01-23", "shipped": false, "total_amount": 45.50, "product": "Keyboard" }
{ "create": {  } }
{ "customer": "Bob", "order_date": "2024-12-28", "shipped": true, "total_amount": 89.99, "product": "Mouse" }
{ "create": { } }
{ "customer": "Alice", "order_date": "2024-12-01", "shipped": true, "total_amount": 250.75, "product": "Laptop" }



# Get all the shipped orders
GET /orders/_search
{
  "query": {
    "term": {
      "shipped": true
    }
  }
}

/*
Date Math in Elasticsearch
- now -> current date and time (Ex: 2025-May-15 13:00:00)
- now-1d -> 2025-May-14 13:00:00
- now+1h -> 2025-May-15 14:00:00

- y  -> years
- M  -> months
- w  -> weeks
- d  -> days
- h  -> hours
- m  -> minutes
- s  -> seconds

Truncated Time Notation
- /M  -> start of the month
- /d  -> start of the day
- /y  -> start of the year

Example (Assume "now" = 2025-May-15 13:00)
- now/d     -> 2025-May-15 00:00 (start of the day)
- now-1d/d  -> 2025-May-14 00:00 (start of the previous day)
- now/M     -> 2025-May-01 00:00 (start of the month)
- now/y     -> 2025-Jan-01 00:00 (start of the year)
- now-1M/M  -> 2025-Apr-01 00:00 (start of the previous month) 

More: https://www.elastic.co/guide/en/elasticsearch/reference/current/common-options.html#date-math
*/

# Orders placed in the last 1 hour
GET /orders/_search
{
  "query": {
    "range": {
      "order_date": {
        "gte": "now-1h"
      }
    }
  }
}

# Orders placed in the last 24 hours
GET /orders/_search
{
  "query": {
    "range": {
      "order_date": {
        "gte": "now-1d"
      }
    }
  }
}

# Orders placed since yesterday (start of the previous day)
# If no results, check if your test data includes dates within this range
GET /orders/_search
{
  "query": {
    "range": {
      "order_date": {
        "gte": "now-1d/d"
      }
    }
  }
}

# Orders placed in the last 1 week
# If no results, check if your test data includes dates within this range
GET /orders/_search
{
  "query": {
    "range": {
      "order_date": {
        "gte": "now-1w"
      }
    }
  }
}

# Assignment 
# Orders placed in the last month - from the start till the end of the previous month
# If no results, check if your test data includes dates within this range





GET /orders/_search
{
  "query": {
    "range": {
      "order_date": {
        "gte": "now-1M/M",
        "lt": "now/M"
      }
    }
  }
}
```