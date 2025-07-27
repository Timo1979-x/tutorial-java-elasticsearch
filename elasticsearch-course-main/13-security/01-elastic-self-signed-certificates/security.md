
### Docker Compose

```yaml
services:
  elastic:
    container_name: elastic
    image: docker.elastic.co/elasticsearch/elasticsearch:9.0.2
    ports:
    - 9200:9200
    environment:
    - discovery.type=single-node
    - ELASTIC_PASSWORD=password123
```

### Steps

- Copy the above yaml and create a docker-compose.yaml and run `docker compose up`.
- Once it is up and running, run this below command.
```sh
curl https://localhost:9200
```
- Elasticsearch uses self signed certificates which allows us to use https, by default. The `curl` commands might fail as the client can not verify the certificate. so we have to copy the CA certificate from the container.
```sh
docker cp elastic:/usr/share/elasticsearch/config/certs/http_ca.crt .
```
- Run this command. We should see a credentials issue this time.
```sh
curl --cacert http_ca.crt https://localhost:9200
```
- Now provide valid credentials
```sh
curl --cacert http_ca.crt https://localhost:9200 -u elastic:password123 
```