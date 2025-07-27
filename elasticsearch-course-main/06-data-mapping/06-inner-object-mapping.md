## 1:1 Inner/Child Object Mapping

```json
{
  "title": "Inception",
  "director": {
    "name": "Christopher Nolan",
    "country": "USA"
  },
  "yearReleased": 2010
}
```

### Demo

```
# delete index
DELETE /movies

/*
{
  "title": "Inception",
  "director": {
    "name": "Christopher Nolan",
    "country": "USA"
  },
  "yearReleased": 2010
}
*/

# create index with mapping
PUT /movies
{
  "mappings": {
    "properties": {
      "title": {
        "type": "text"
      },
      "yearReleased": {
        "type": "integer"
      },
      "director": {
        "properties": {
            "name": {
                "type": "text"
            },
            "country": {
                "type": "keyword"
            }
        }
      }
    }
  }
}

# get mapping for the index
GET /movies/_mapping

# bulk insert
POST /movies/_bulk
{ "create": { } }
{ "title": "Inception", "director": { "name": "Christopher Nolan", "country": "USA" }, "yearReleased": 2010 }
{ "create": { } }
{ "title": "Parasite", "director": { "name": "Bong Joon-ho", "country": "South Korea" }, "yearReleased": 2019 }
{ "create": { } }
{ "title": "The Godfather", "director": { "name": "Francis Ford Coppola", "country": "USA" }, "yearReleased": 1972 }

# search
GET /movies/_search?q=nolan


/* 1:1 inner object can also be mapped as shown below */ 

# delete index
DELETE /movies

# create index with mapping
PUT /movies
{
  "mappings": {
    "properties": {
      "title": {
        "type": "text"
      },
      "yearReleased": {
        "type": "integer"
      },
      "director.name": {
        "type": "text"
      },
      "director.country": {
        "type": "keyword"
      }
    }
  }
}

# get mapping for the index
GET /movies/_mapping

# bulk insert
POST /movies/_bulk
{ "create": { } }
{ "title": "Inception", "director": { "name": "Christopher Nolan", "country": "USA" }, "yearReleased": 2010 }
{ "create": { } }
{ "title": "Parasite", "director": { "name": "Bong Joon-ho", "country": "South Korea" }, "yearReleased": 2019 }
{ "create": { } }
{ "title": "The Godfather", "director": { "name": "Francis Ford Coppola", "country": "USA" }, "yearReleased": 1972 }

# search
GET /movies/_search?q=nolan
```