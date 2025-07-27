## Search As You Type

- `search_as_you_type` field type is a text-like field that is optimized to provide out-of-the-box support for queries that serve an as-you-type completion use case. [Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-as-you-type.html)
- It needs a special mapping

```json
{
  "mappings": {
    "properties": {
      "your-field": {
        "type": "search_as_you_type"
      }
    }
  }
}
```

### Demo

```sh
# shingle filter demo
POST /_analyze
{
  "text": "Ultra HD Smart OLED Gaming Monitor", // 6 - 15
  "tokenizer": "standard",
  "filter": [
    {
      "type": "shingle",
      "min_shingle_size": 2,
      "max_shingle_size": 3
    }
  ]
}

# delete index
DELETE /products

# create index with the mapping
# it creates additional fields name._2gram, name._3gram
PUT /products
{
  "mappings": {
    "properties": {
      "name":  { 
        "type" : "search_as_you_type"
      },
      "brand":  { "type" : "keyword"},
      "category":  { "type" : "keyword"},
      "price":  { "type" : "integer"},
      "in_stock":  { "type" : "integer"}
    }
  }
}

# insert records
POST /products/_bulk
{"create": {}}
{"name": "Noise Cancelling Headphones", "brand": "Bose", "category": "Electronics", "price": 200, "in_stock": 10}
{"create": {}}
{"name": "4K Gaming Monitor", "brand": "Dell", "category": "Electronics", "price": 500, "in_stock": 5}
{"create": {}}
{"name": "Ergonomic Office Chair", "brand": "Ikea", "category": "Furniture", "price": 250, "in_stock": 8}
{"create": {}}
{"name": "Water Purifier System", "brand": "LG", "category": "Electronics", "price": 350, "in_stock": 12}
{"create": {}}
{ "name": "Smart Sleep Tracker", "brand": "Fitbit", "category": "Electronics", "price": 150, "in_stock": 15}
{"create": {}}
{"name": "Face Moisturizer", "brand": "Neutrogena", "category": "Beauty", "price": 20, "in_stock": 100}
{"create": {}}
{"name": "Air Purifier with Smart Sensor", "brand": "Philips", "category": "Electronics", "price": 200, "in_stock": 7}
{"create": {}}
{"name": "Smart Home Energy Monitor", "brand": "Xiaomi", "category": "Electronics", "price": 100, "in_stock": 18}
{"create": {}}
{"name": "Pro Power Bank", "brand": "Anker", "category": "Electronics", "price": 50, "in_stock": 30}
{"create": {}}
{"name": "Gaming Laptop", "brand": "Lenovo", "category": "Electronics", "price": 1000, "in_stock": 3}
{"create": {}}
{"name": "Smart TV", "brand": "Samsung", "category": "Electronics", "price": 700, "in_stock": 8}
{"create": {}}
{"name": "Wireless Earbuds", "brand": "Apple", "category": "Electronics", "price": 120, "in_stock": 20}
{"create": {}}
{"name": "Smartwatch", "brand": "Apple", "category": "Electronics", "price": 250, "in_stock": 15}
{"create": {}}
{"name": "Fitness Tracker", "brand": "Xiaomi", "category": "Electronics", "price": 80, "in_stock": 25}
{"create": {}}
{"name": "Coffee Table", "brand": "Ikea", "category": "Furniture", "price": 150, "in_stock": 10}
{"create": {}}
{"name": "Sofa Set", "brand": "Nilkamal", "category": "Furniture", "price": 800, "in_stock": 5}
{"create": {}}
{"name": "Dining Table", "brand": "Pepperfry", "category": "Furniture", "price": 300, "in_stock": 8}
{"create": {}}
{"name": "Face Wash", "brand": "Garnier", "category": "Beauty", "price": 15, "in_stock": 100}
{"create": {}}
{"name": "Hair Shampoo", "brand": "Head & Shoulders", "category": "Beauty", "price": 25, "in_stock": 80}
{"create": {}}
{"name": "Lip Balm", "brand": "Nivea", "category": "Beauty", "price": 10, "in_stock": 120}

# 'office chair' exact match works
POST /products/_search
{
  "query": {
    "term": {
      "name._2gram": {
        "value": "office chair"
      }
    }
  }
}

/*
    bool_prefix along with name, name._2gram, name._3gram

    This can be used for autocomplete to match any word / prefix within the product name.
    Behind the scenes,bool_prefix is converted to bool query.
    For ex: "boot spr"
    should[
        term(boot),
        prefix(spr)
    ]
*/
POST /products/_search
{
  "query": {
    "multi_match": {
      "query": "tab",
      "type": "bool_prefix",
      "fields": [
        "name",
        "name._2gram",
        "name._3gram"
      ]
    }
  },
  "_source": ["name"]
}
```