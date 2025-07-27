## Access Elasticsearch via Terminal

```
curl http://localhost:9200
```

## Cluster API via Kibana

- To provide information about the health of the Elasticsearch cluster.

```
GET /_cluster/health
```
This is same as the below curl command
```
curl http://localhost:9200/_cluster/health
```

## Health Status

- Green
    - All primary and replica shards are active. The cluster is fully operational.
- Yellow
    - All primary shards are active, but some or all replica shards are not allocated. The data is available but not fully redundant.
- Red
    - Some primary shards are not active. Data could be missing or unavailable. It indicates a serious issue that needs immediate attention.

## Get Nodes Information

```
GET /_nodes
```

## CAT API

```
GET /_cat/health?v
GET /_cat/nodes?v
```