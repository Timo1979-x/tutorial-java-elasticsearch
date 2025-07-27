## Bulk API

### Demo 1
```
# delete index
DELETE /my-index

# create an index
PUT /my-index

# bulk insert
POST /my-index/_bulk
{ "create" : {} }
{ "name" : "item1" }
{ "create" : {} }
{ "name" : "item2" }
{ "create" : {} }
{ "name" : "item3" }

# query all
GET /my-index/_search

# bulk insert with id
POST /my-index/_bulk
{ "create" : { "_id": 1 } }
{ "name" : "item1" }
{ "create" : { "_id": 2 } }
{ "name" : "item2" }
{ "create" : { "_id": 3 } }
{ "name" : "item3" }

# query all
GET /my-index/_search

# bulk insert / update / delete
POST /my-index/_bulk
{ "create" : { "_id": 1 } }
{ "name" : "item1" }
{ "create" : { "_id": 2 } }
{ "name" : "item2" }
{ "create" : { "_id": 3 } }
{ "name" : "item3" }
{ "update" : { "_id": 2 } }
{ "doc": { "name" : "item2-updated" }}
{ "create" : { "_id": 4 } }
{ "name" : "item4" }
{ "delete" : { "_id": 1 } }

# query all
GET /my-index/_search
```

### Demo 2
```
# delete index
DELETE /my-index

# bulk insert
POST /my-index/_bulk
{ "create" : { "_id": 1 } }
{ "name" : "item1" }
{ "create" : { "_id": 2 } }
{ "name" : "item2" }
{ "create" : { "_id": 3 } }
{ "name" : "item3" }

# bulk update
POST /my-index/_bulk
{ "update" : { "_id": 2, "if_seq_no": "1", "if_primary_term": "1" } }
{ "doc": { "name" : "item2-updated" }}
{ "update" : { "_id": 3, "if_seq_no": "2", "if_primary_term": "1" } }
{ "doc": { "name" : "item3-updated" }}

# query all
GET /my-index/_search

# bulk insert
POST /_bulk
{ "create" : { "_index": "my-index1", "_id": 1 } }
{ "name" : "item1" }
{ "create" : { "_index": "my-index2", "_id": 2 } }
{ "name" : "item2" }
{ "create" : { "_index": "my-index3", "_id": 3 } }
{ "name" : "item3" }

# query my-index1
GET /my-index1/_search

# query all indices
GET /my-index*/_search

# delete all the indices
DELETE /my-index1,my-index2,my-index3
```

### Demo 3 - File Upload

- Use this file `bulk-upload/products-bulk-upload.ndjson` (Create your own file using the format). Ensure there is an empty line at the end of the file to process successfully.
```
{ "create" : {} }
{ "name" : "Wireless Earbuds", "category": "electronics", "price": 199.99, "rating": 4.5, "stock": 20 }
{ "create" : {} }
{ "name" : "Men's Running Shoes", "category": "clothing", "price": 79.99, "rating": 4.2, "stock": 50 }
...
...
```
- Use this below curl command to upload the file content. Use the port you have mapped for elasticsearch.
```curl
curl -XPOST "http://localhost:9201/products/_bulk" -H "Content-Type: application/x-ndjson" --data-binary @products-bulk-upload.ndjson
```