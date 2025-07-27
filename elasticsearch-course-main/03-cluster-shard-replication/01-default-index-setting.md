
## Index Default Settings

```
# create an index
PUT /products

# status could be yellow
GET /_cluster/health

# get the information about shards
GET /_cat/shards?v

# get the information about shards for a specific index
GET /_cat/shards/products?v

# delete index
DELETE /products
```

## Overriding The Default Settings

```
# create an index
PUT /products
{
    "settings": {
        "number_of_shards": 1,
        "number_of_replicas": 0
    }
}

# status could be green
GET /_cluster/health
```