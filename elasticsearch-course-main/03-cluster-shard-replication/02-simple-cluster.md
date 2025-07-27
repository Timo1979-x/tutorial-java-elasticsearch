## Elasticsearch Cluster

- create a `docker-compose.yaml` file with below content to create the elasticsearch cluster

```yaml
services:
  es01:
    image: docker.elastic.co/elasticsearch/elasticsearch:9.0.2
    environment:
    - node.name=es01
    - cluster.name=my-cluster 
    - xpack.security.enabled=false
    - xpack.security.http.ssl.enabled=false
    - cluster.initial_master_nodes=es01,es02,es03
    - discovery.seed_hosts=es02,es03
    - "ES_JAVA_OPTS=-Xms512m -Xmx512m"     
    ports:
    - 9201:9200 
  es02:
    image: docker.elastic.co/elasticsearch/elasticsearch:9.0.2
    environment:
    - node.name=es02
    - cluster.name=my-cluster
    - xpack.security.enabled=false
    - xpack.security.http.ssl.enabled=false  
    - cluster.initial_master_nodes=es01,es02,es03
    - discovery.seed_hosts=es01,es03
    - "ES_JAVA_OPTS=-Xms512m -Xmx512m"    
    ports:
    - 9202:9200     
  es03:
    image: docker.elastic.co/elasticsearch/elasticsearch:9.0.2
    environment:
    - node.name=es03
    - cluster.name=my-cluster
    - xpack.security.enabled=false
    - xpack.security.http.ssl.enabled=false
    - cluster.initial_master_nodes=es01,es02,es03    
    - discovery.seed_hosts=es01,es02
    - "ES_JAVA_OPTS=-Xms512m -Xmx512m"        
    ports:
    - 9203:9200 
  kibana:
    image: docker.elastic.co/kibana/kibana:9.0.2
    ports:
    - 5601:5601
    environment:
    - ELASTICSEARCH_HOSTS=["http://es01:9200","http://es02:9200","http://es03:9200"]
```

## Demo

```
# cluster health
GET /_cluster/health

# create index
PUT /products

# cluster status is green. not yellow
GET /_cluster/health

# we have 3 nodes. one of them would be master
GET /_cat/nodes?v

# index - shards info
GET /_cat/shards/products?v

# create 4 more indices
PUT /products2
PUT /products3
PUT /products4
PUT /products5

# index - shards info
GET /_cat/shards/products*?v

# delete index
DELETE /products

# delete multiple indices
DELETE /products2,products3,products4,products5
```