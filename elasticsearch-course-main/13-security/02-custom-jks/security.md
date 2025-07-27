
### Docker Compose With Custom JKS

```yaml
services:
  elastic:
    image: docker.elastic.co/elasticsearch/elasticsearch:9.0.2
    ports:
    - 9200:9200
    environment:
    - discovery.type=single-node
    - xpack.security.enabled=true
    - xpack.security.http.ssl.enabled=true
    - ELASTIC_PASSWORD=password123
    - xpack.security.http.ssl.keystore.path=/usr/share/elasticsearch/config/certs/elastic.keystore.jks
    - xpack.security.http.ssl.keystore.password=changeit
    volumes:
    - ./certs:/usr/share/elasticsearch/config/certs   
```

### Steps

- Copy the above yaml and create a docker-compose.yaml and run `docker compose up`.
- Once it is up and running, run this below command.
    - Note: `curl` does not support jks files. So we will have to export the PEM certificate from the trust-store.jks
```sh
curl --cacert ./certs/trust-store.crt https://localhost:9200 -u elastic:password123 
```

