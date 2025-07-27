## Dynamic Mapping

### Demo

```
# delete index
DELETE /my-index

# insert a doc
POST /my-index/_doc
{
    "quantity": 10,
    "isAvailable": true,
    "lastUpdated": "2000/01/01"
}

# get mapping for the index
GET /my-index/_mapping

# dynamic mapping can lead to unexpected issues!

# delete index
DELETE /my-index

# insert a doc
POST /my-index/_doc
{
    "path": "2000/01/01"
}

# then insert this! but fails!
POST /my-index/_doc
{
    "path": "02/02/2000"
}

# similar issues could happen for other fields as well.

# long type is assumed here
POST /my-index/_doc
{
 "phone": 1234567890
}

# this string value can be converted into long
POST /my-index/_doc
{
 "phone": "4343434342"
}

# can not be converted into long.
POST /my-index/_doc
{
 "phone": "200-300-4000"
}

# search - we can see all the documents we have indexed with the appropriate types
GET /my-index/_search
```
