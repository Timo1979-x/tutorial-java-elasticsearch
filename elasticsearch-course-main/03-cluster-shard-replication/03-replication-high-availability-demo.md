- Ensure that the 3 node cluster is up and running

```
# create index
PUT /products

# get the information about shards
GET /_cat/shards/products?v

# store some documents
POST /products/_doc
{
    "name": "product 1"
}

# delete index
DELETE /products
```

- Send CURL requests to search for documents

```curl
CURL http://localhost:[PORT]/products/_search
```

- Bring the primary shard node down
- Send the CURL request to search for documents
