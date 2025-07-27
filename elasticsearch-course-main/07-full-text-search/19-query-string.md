## Query String

- It is a powerful and flexible query type to write complex search queries in a single string.

### Examples

```
spring OR season
spring AND season
spring NOT season
name:shoe AND color:Blue
```

### Demo 1

```
# delete index
DELETE /articles

# create index with the mapping
PUT /articles
{
  "mappings": {
    "properties": {
      "title": { "type" : "text"},
      "body":  { "type" : "text"}
    }
  }
}

# insert documents
POST /articles/_bulk
{ "create": {} }
{ "title": "The Rainy Season", "body": "The rainy season can be a blessing or a curse."}
{ "create": {} }
{ "title": "The Winter season", "body": "The winter season is often harsh and cold."}
{ "create": {} }
{ "title": "Summer Season", "body": "Summer season brings hot and humid weather."}
{ "create": {} }
{ "title": "The Spring Breeze", "body": "The spring breeze is gentle and refreshing."}
{ "create": {} }
{ "title": "Season of Spring", "body": "Spring is the season of renewal and growth."}
{ "create": {} }
{ "title": "The Spring Season", "body": "The season of spring brings beautiful flowers and warm weather."}
{ "create": {} }
{ "title": "About Me", "body": "I love spending time outdoors during the spring season."}
{ "create": {} }
{ "title": "My favorite season", "body": "Spring is my favorite season of the year."}
{ "create": {} }
{ "title": "Boot Missing", "body": "I lost my favorite hiking boot in the mud during spring" }
{ "create": {} }
{ "title": "Introduction To Spring Framework", "body": "The app was developed using spring boot framework" }
{ "create": {} }
{ "title": "The Bike Rider", "body": "He kicked off his boot and relaxed by the fire." }

# query-string searches across all fields
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "spring"
    }
  }
}

GET /articles/_search?q=spring

# default OR operator
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "spring season"
    }
  }
}

# http://localhost:9200/articles/_search?q=spring season
GET /articles/_search?q=spring%20season

# AND
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "spring AND season"
    }
  }
}

GET /articles/_search?q=spring%20AND%20season


# NOT
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "spring NOT season"
    }
  }
}
GET /articles/_search?q=spring%20NOT%20season

# match phrase
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "\"spring season\""
    }
  }
}
GET /articles/_search?q="spring season"

# specific field search
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "title:boot"
    }
  }
}
GET /articles/_search?q=title:boot

# OR with different fields
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "title:boot OR body:winter"
    }
  }
}

GET /articles/_search?q=title:boot%20OR%20body:winter

# AND with different fields
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "title:boot AND body:spring"
    }
  }
}

GET /articles/_search?q=title:boot%20AND%20body:spring

# we have to be careful as it is easy to make mistake - if we directly pass the user input, then it will fail like this
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "title:(boot AND body:spring"
    }
  }
}

# fuzziness
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "title:book~"
    }
  }
}

GET /articles/_search?q=title:book~

# wildcard support
POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "fir?"
    }
  }
}
GET /articles/_search?q=fir?

POST /articles/_search
{
  "query": {
    "query_string": {
      "query": "frame*"
    }
  }
}

GET /articles/_search?q=frame*
```

### Demo 2

```

# delete index
DELETE /products

# create index with the mapping
PUT /products
{
  "mappings": {
    "properties": {
      "name": { "type" : "text"},
      "size":  { "type" : "keyword"},
      "color":  { "type" : "keyword"},
      "brand":  { "type" : "keyword"},
      "price": { "type": "float" },
      "rating": { "type": "float" }
    }
  }
}

# insert records
POST /products/_bulk
{ "create": { "_id": "1" } }
{"name":"Tennis Shoe","size":["7","8","9","10"],"color":["Black","White","Grey"],"brand":"Nike", "price": 99.99, "rating": 4.8 }
{ "create": { "_id": "2" } }
{"name":"Running Shoe","size":["6","7","8","9","10"],"color":["Black","White","Blue"],"brand":"Nike", "price": 69.99, "rating": 3.6}
{ "create": { "_id": "3" } }
{"name":"Running and Training shoe","size":["6","7","8","9","10"],"color":["Red","White","Black"],"brand":"Nike", "price": 79.99, "rating": 4.3}
{ "create": { "_id": "4" } }
{"name":"Walking Shoe","size":["6","7","8","9","10"],"color":["White","Black","Red"],"brand":"Nike", "price": 39.99, "rating": 4.0}
{ "create": { "_id": "5" } }
{"name":"Winter Boots","size":["6","7","8","9","10"],"color":["Black","White","Blue"],"brand":"Timberland", "price": 65.99, "rating": 4.6}
{ "create": { "_id": "6" } }
{"name":"Hiking Boots","size":["7","8","9","10","11"],"color":["Brown","Black"],"brand":"Timberland", "price": 48.99, "rating": 3.8}
{ "create": { "_id": "7" } }
{"name":"Leather Fashion Boots","size":["7","8","9","10","11"],"color":["Brown","Black"],"brand":"Timberland", "price": 110.99, "rating": 4.8}

# >= - range query
POST /products/_search
{
  "query": {
    "query_string": {
      "query": "rating:>=4.6"
    }
  }
}

GET /products/_search?q=rating:>=4.6


# keyword field - exacxt match
POST /products/_search
{
  "query": {
    "query_string": {
      "query": "brand:Timberland"
    }
  }
}

GET /products/_search?q=brand:Timberland
```