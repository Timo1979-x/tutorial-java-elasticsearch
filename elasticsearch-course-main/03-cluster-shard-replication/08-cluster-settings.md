## Cluster Settings
- [Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/misc-cluster-settings.html)

```
# check if the index is present
GET /_cat/indices/my-index?v

# try to insert w/o creating an index
POST /my-index/_doc
{
    "name": "vinoth"
}

/*
    cluster settings
        persistent - permanent (recommended)
        transient - temporary
*/
GET /_cluster/settings

# we can disable the auto index creation
PUT /_cluster/settings
{
    "transient": {
        "action.auto_create_index": false
    }
}

# try to insert w/o creating an index
POST /my-index1/_doc
{
    "name": "vinoth"
}

```