## Completion Suggester

- This is used to build auto-complete functionality in the search boxes.
  [Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/search-suggesters.html#completion-suggester)

- auto-complete functionality is supposed to be as fast as possible as it gets triggered for every letter the user types. It needs a special mapping.

```json
{
  "mappings": {
    "properties": {
      "your-field": {
        "type": "completion"
      }
    }
  }
}
```

### Demo 1

```sh
# delete index
DELETE /products

# create index with the mapping
PUT /products
{
  "mappings": {
    "properties": {
      "name":  { 
        "type" : "text",
        "fields": {
          "completion": {
            "type": "completion"
          }
        }
      },
      "brand":  { "type" : "keyword"},
      "category":  { "type" : "keyword"},
      "price":  { "type" : "integer"},
      "in_stock":  { "type" : "integer"}
    }
  }
}

# insert records - there is a duplicate record
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
{"create": {}}
{"name": "Lip Balm", "brand": "Nivea", "category": "Beauty", "price": 10, "in_stock": 120}

# The user's input in the search textbox is used as the prefix for the suggestion query.
POST /products/_search
{
  "suggest": {
    "product-suggest": {
      "prefix": "",  // sma, fac 
      "completion": {         
          "field": "name.completion"
      }
    }
  },
  "_source": false
}

# remove duplicates
POST /products/_search
{
  "suggest": {
    "product-suggest": {
      "prefix": "li",
      "completion": {
        "field": "name.completion",
        "skip_duplicates": false // default false
      }
    }
  },
  "_source": false
}

# fuzziness - it will not work
POST /products/_search
{
  "suggest": {
    "product-suggest": {
      "prefix": "smer",
      "completion": {
        "field": "name.completion"
      }
    }
  },
  "_source": false
}

# fuzziness - this will work
POST /products/_search
{
  "suggest": {
    "product-suggest": {
      "prefix": "smer",
      "completion": {
        "field": "name.completion",
        "fuzzy": {
          "fuzziness": 2
        }
      }
    }
  },
  "_source": false
}

# we have to use only the prefix. this will not return results
POST /products/_search
{
  "suggest": {
    "product-suggest": {
      "prefix": "tab",    
      "completion": {         
          "field": "name.completion"
      }
    }
  },
  "_source": false
}
```

### Demo 2 - Name and Brand Suggestions

```sh
# delete index
DELETE /products

# create index with the mapping
PUT /products
{
  "mappings": {
    "properties": {
      "name":  { 
        "type" : "text",
        "fields": {
          "completion": {
            "type": "completion"
          }
        }
      },
      "brand":  { 
        "type" : "keyword",
         "fields": {
          "completion": {
            "type": "completion"
          }
        }   
      },
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


# get both products & brand suggestion
POST /products/_search
{
  "suggest": {
    "product-suggest": {
      "prefix": "f",    
      "completion": {         
         "field": "name.completion"
      }
    },
    "brand-suggest": {
      "prefix": "f",    
      "completion": {         
         "field": "brand.completion"
      }
    }
  },
  "_source": false
}

```