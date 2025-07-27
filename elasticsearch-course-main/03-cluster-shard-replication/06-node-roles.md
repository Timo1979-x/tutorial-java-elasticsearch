## Cluster 

- In order to vote, the node should have the master role set. 

```yaml
services:
  es01:
    image: docker.elastic.co/elasticsearch/elasticsearch:9.0.2
    environment:
    - node.name=es01
    - cluster.name=my-cluster  
    - xpack.security.enabled=false
    - xpack.security.http.ssl.enabled=false
    - cluster.initial_master_nodes=es01
    - "ES_JAVA_OPTS=-Xms512m -Xmx512m"      
    - node.roles=master    
  es02:
    image: docker.elastic.co/elasticsearch/elasticsearch:9.0.2
    environment:
    - node.name=es02
    - cluster.name=my-cluster 
    - xpack.security.enabled=false
    - xpack.security.http.ssl.enabled=false  
    - cluster.initial_master_nodes=es01
    - discovery.seed_hosts=es01
    - "ES_JAVA_OPTS=-Xms512m -Xmx512m"    
    - node.roles=master,data,voting_only   
    ports:
    - 9202:9200 
  es03:
    image: docker.elastic.co/elasticsearch/elasticsearch:9.0.2
    environment:
    - node.name=es03
    - cluster.name=my-cluster 
    - xpack.security.enabled=false
    - xpack.security.http.ssl.enabled=false
    - cluster.initial_master_nodes=es01
    - discovery.seed_hosts=es01
    - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    - node.roles=master,data,voting_only
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
# we have 3 nodes. es01 would be the master
GET /_cat/nodes?v

# get node detailed information
GET /_nodes

# create indices
PUT /products1
PUT /products2
PUT /products3
PUT /products4

# get the information about shards
GET /_cat/shards/products*?v

# delete the indices
DELETE /products1,products2,products3,products4
```