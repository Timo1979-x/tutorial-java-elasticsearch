## Index APIs

- Get all the indices
```
GET /_cat/indices?v
```

- Get all the indices matching a pattern
```
GET /_cat/indices/*transform*?v
```

- Create Index
    - The cluster status might be yellow.
```
PUT /products
```

- Delete index
```
DELETE /products
```