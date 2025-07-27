## Reindex

### Demo 1
```
/*
create "old-index" with 1 shard. I use the index name as old-index just for demo purposes. we can use meaningful name when we develop an application later. but feel free to change it.
*/ 
PUT /old-index
{
    "settings": {
        "number_of_shards": 1,
        "number_of_replicas": 0
    }
}

# bulk insert
POST /old-index/_bulk
{ "create" : { } }
{ "name" : "item1" }
{ "create" : { } }
{ "name" : "item2" }
{ "create" : {  } }
{ "name" : "item3" }
{ "create" : {  } }
{ "name" : "item4" }
{ "create" : {  } }
{ "name" : "item5" }
{ "create" : {  } }
{ "name" : "item6" }

# query all
GET /old-index/_search

# get shards info
GET /_cat/shards/old-index?v

# create new index with 2 shards
PUT /new-index
{
    "settings": {
        "number_of_shards": 2,
        "number_of_replicas": 0
    }
}

# reindex
POST /_reindex
{
 "source": {
   "index": "old-index"
 },
 "dest": {
   "index": "new-index"
 }
}

# query all
GET /new-index/_search

# get shards info
GET /_cat/shards/new-index?v

# delete indices
DELETE /old-index,new-index
```

### Demo 2 - Reindex with specific fields

```
/*
create "old-index" with 1 shard. I use the index name as old-index just for demo purposes. we can use meaningful name when we develop an application later. but feel free to change it.
*/ 
PUT /old-index
{
    "settings": {
        "number_of_shards": 1,
        "number_of_replicas": 0
    }
}

# bulk insert
POST /old-index/_bulk
{ "create" : { } }
{"name": "Alice Johnson", "age": 28, "gender": "Female", "city": "New York"}
{ "create" : { } }
{"name": "Michael Smith", "age": 34, "gender": "Male", "city": "Los Angeles"}
{ "create" : { } }
{"name": "Sophia Davis", "age": 22, "gender": "Female", "city": "Chicago"}
{ "create" : { } }
{"name": "James Brown", "age": 45, "gender": "Male", "city": "Houston"}
{ "create" : { } }
{"name": "Emily Garcia", "age": 30, "gender": "Female", "city": "Phoenix"}

# query all
GET /old-index/_search

# get shards info
GET /_cat/shards/old-index?v

# create new index with 2 shards
PUT /new-index
{
    "settings": {
        "number_of_shards": 2,
        "number_of_replicas": 0
    }
}

# reindex with specific fields
POST /_reindex
{
 "source": {
   "index": "old-index",
   "_source": []
 },
 "dest": {
   "index": "new-index"
 }
}

# query all
GET /new-index/_search

# get shards info
GET /_cat/shards/new-index?v

# delete indices
DELETE /old-index,new-index
```