## Bucket Terms Aggregation

### Example

```sql
select country, count(*)
from customer
group by country
```
- Similar to `Group By` in SQL. Use a `keyword` field for grouping. `text` fields can not be used. 

### Demo

```sh
 # delete index
DELETE /products

# create index with the mapping
PUT /products
{
  "mappings": {
    "properties": {
      "name": { "type": "text" },
      "size": { "type": "keyword" },
      "color": { "type": "keyword" },
      "material": { "type": "keyword" },
      "brand": { "type": "keyword" },
      "occasion": { "type": "keyword" },
      "neck_style": { "type": "keyword" },
      "price": { "type": "integer" }
    }
  }
}

# insert records
POST /products/_bulk
{ "create": { "_id": "1" } }
{ "name": "Floral Summer", "size": ["S", "M", "L"], "color": ["Red", "Pink"], "material": "Cotton", "brand": "Zara", "occasion": "Casual", "neck_style": "V-Neck", "price": 49 }
{ "create": { "_id": "2" } }
{ "name": "Evening Gown", "size": ["M", "L", "XL"], "color": ["Black", "Navy"], "material": "Silk", "brand": "H&M", "occasion": "Formal", "neck_style": "Off-Shoulder", "price": 129 }
{ "create": { "_id": "3" } }
{ "name": "Casual Maxi", "size": ["XS", "S", "M"], "color": ["Blue", "White"], "material": "Cotton", "brand": "Forever 21", "occasion": "Casual", "neck_style": "Round", "price": 39 }
{ "create": { "_id": "4" } }
{ "name": "Cocktail", "size": ["S", "M"], "color": ["Green", "Emerald"], "material": "Satin", "brand": "Zara", "occasion": "Party", "neck_style": "Halter", "price": 79 }
{ "create": { "_id": "5" } }
{ "name": "Office Wear Sheath", "size": ["M", "L"], "color": ["Gray", "Black"], "material": "Polyester", "brand": "Mango", "occasion": "Work", "neck_style": "Boat Neck", "price": 59 }
{ "create": { "_id": "6" } }
{ "name": "Casual Wrap", "size": ["S", "M", "L"], "color": ["Beige", "Brown"], "material": "Cotton", "brand": "Forever 21", "occasion": "Casual", "neck_style": "V-Neck", "price": 44 }
{ "create": { "_id": "7" } }
{ "name": "Velvet Party", "size": ["M", "L", "XL"], "color": ["Maroon", "Burgundy"], "material": "Velvet", "brand": "Zara", "occasion": "Party", "neck_style": "Halter", "price": 99 }
{ "create": { "_id": "8" } }
{ "name": "Satin Slip", "size": ["XS", "S"], "color": ["Champagne", "Gold"], "material": "Satin", "brand": "H&M", "occasion": "Formal", "neck_style": "Spaghetti Strap", "price": 89 }
{ "create": { "_id": "9" } }
{ "name": "Summer Boho", "size": ["S", "M", "L"], "color": ["Yellow", "Orange"], "material": "Cotton", "brand": "Mango", "occasion": "Casual", "neck_style": "Square", "price": 54 }
{ "create": { "_id": "10" } }
{ "name": "A-Line Midi", "size": ["M", "L"], "color": ["Navy", "Teal"], "material": "Polyester", "brand": "Forever 21", "occasion": "Work", "neck_style": "Round", "price": 69 }
{ "create": { "_id": "11" } }
{ "name": "Formal Pencil", "size": ["S", "M", "L"], "color": ["Black", "Dark Gray"], "material": "Polyester", "brand": "H&M", "occasion": "Work", "neck_style": "Boat Neck", "price": 79 }
{ "create": { "_id": "12" } }
{ "name": "Chiffon Bridesmaid", "size": ["S", "M", "L", "XL"], "color": ["Lavender", "Blush"], "material": "Chiffon", "brand": "Zara", "occasion": "Formal", "neck_style": "Sweetheart", "price": 139 }
{ "create": { "_id": "13" } }
{ "name": "Denim Shirt", "size": ["XS", "S", "M"], "color": ["Blue", "Light Blue"], "material": "Denim", "brand": "Mango", "occasion": "Casual", "neck_style": "Collared", "price": 59 }
{ "create": { "_id": "14" } }
{ "name": "Knitted Bodycon", "size": ["M", "L"], "color": ["Beige", "Brown"], "material": "Wool", "brand": "Forever 21", "occasion": "Casual", "neck_style": "Turtleneck", "price": 74 }
{ "create": { "_id": "15" } }
{ "name": "Tulle Ball Gown", "size": ["M", "L", "XL"], "color": ["Pink", "Rose"], "material": "Tulle", "brand": "H&M", "occasion": "Formal", "neck_style": "Strapless", "price": 199 }
{ "create": { "_id": "16" } }
{ "name": "Pleated Maxi", "size": ["S", "M", "L"], "color": ["Green", "Mint"], "material": "Polyester", "brand": "Mango", "occasion": "Party", "neck_style": "One-Shoulder", "price": 89 }
{ "create": { "_id": "17" } }
{ "name": "Sequin Party", "size": ["M", "L", "XL"], "color": ["Silver", "Gold"], "material": "Sequin", "brand": "Zara", "occasion": "Party", "neck_style": "Round", "price": 149 }
{ "create": { "_id": "18" } }
{ "name": "Halter Beach", "size": ["XS", "S", "M"], "color": ["White", "Sky Blue"], "material": "Cotton", "brand": "Forever 21", "occasion": "Casual", "neck_style": "Halter", "price": 44 }
{ "create": { "_id": "19" } }
{ "name": "Mesh Party", "size": ["S", "M", "L"], "color": ["Black", "Gray"], "material": "Mesh", "brand": "H&M", "occasion": "Party", "neck_style": "Spaghetti Strap", "price": 129 }
{ "create": { "_id": "20" } }
{ "name": "Lace Mini", "size": ["XS", "S"], "color": ["White", "Ivory"], "material": "Lace", "brand": "Mango", "occasion": "Formal", "neck_style": "Sweetheart", "price": 99 }

# group by size
POST /products/_search
{
  "size": 0, 
  "aggs": {
    "group-by-size":{
      "terms": {
        "field": "size"
      }
    }
  }
}

# build facets for left navigation
POST /products/_search
{
  "size": 0, // adjust this if you also need docs
  "aggs": {
    "group-by-size": {
      "terms": {
        "field": "size"
      }
    },
    "group-by-color": {
      "terms": {
        "field": "color"
      }
    },
    "group-by-material": {
      "terms": {
        "field": "material"
      }
    },
    "group-by-brand": {
      "terms": {
        "field": "brand"
      }
    },
    "group-by-occasion": {
      "terms": {
        "field": "occasion"
      }
    },
    "group-by-neckstyle": {
      "terms": {
        "field": "neck_style"
      }
    }
  }
}

# assume the user has selected occasion=Party
# build facets for left navigation
POST /products/_search
{
  "size": 0, // adjust this if you also need docs
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "occasion": {
              "value": "Party"
            }
          }
        }
      ]
    }
  },
  "aggs": {
    "group-by-size": {
      "terms": {
        "field": "size"
      }
    },
    "group-by-color": {
      "terms": {
        "field": "color"
      }
    },
    "group-by-material": {
      "terms": {
        "field": "material"
      }
    },
    "group-by-brand": {
      "terms": {
        "field": "brand"
      }
    },
    "group-by-occasion": {
      "terms": {
        "field": "occasion"
      }
    },
    "group-by-neckstyle": {
      "terms": {
        "field": "neck_style"
      }
    }
  }
}

# assume the user has selected occasion=Party & brand=Zara
# build facets for left navigation
POST /products/_search
{
  "size": 0, // adjust this if you also need docs
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "occasion": {
              "value": "Party"
            }
          }
        },
        {
          "term": {
            "brand": {
              "value": "Zara"
            }
          }
        }        
      ]
    }
  },
  "aggs": {
    "group-by-size": {
      "terms": {
        "field": "size"
      }
    },
    "group-by-color": {
      "terms": {
        "field": "color"
      }
    },
    "group-by-material": {
      "terms": {
        "field": "material"
      }
    },
    "group-by-brand": {
      "terms": {
        "field": "brand"
      }
    },
    "group-by-occasion": {
      "terms": {
        "field": "occasion"
      }
    },
    "group-by-neckstyle": {
      "terms": {
        "field": "neck_style"
      }
    }
  }
}

# min_doc_count - to return terms that match more than configured number of hits
POST /products/_search
{
  "size": 0, 
  "aggs": {
    "group-by-occasion":{
      "terms": {
        "field": "occasion",
        "min_doc_count": 5 // any key with count < 5 will not be included
      }
    }
  }
}


# exclude a key
POST /products/_search
{
  "size": 0,
  "aggs": {
    "group-by-occasion": {
      "terms": {
        "field": "occasion",
        "exclude": ".*mal" // exclude formal
      }
    }
  }
}

# for each brand, find the avg price
POST /products/_search
{
  "size": 0,
  "aggs": {
    "group-by-brand": {
      "terms": {
        "field": "brand"
      },
      "aggs": {
        "price_avg": {
          "avg": {
            "field": "price"
          }
        }
      }
    }
  }
}
```