## 1:N Inner/Child Objects Mapping

```json
{
  "title": "Inception",
  "actors": [
    {
      "name": "Leonardo DiCaprio",
      "role": "Cobb"
    },
    {
      "name": "Joseph Gordon-Levitt",
      "role": "Arthur"
    }
  ],
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
  "actors": [
    {
      "name": "Leonardo DiCaprio",
      "role": "Cobb"
    },
    {
      "name": "Joseph Gordon-Levitt",
      "role": "Arthur"
    }
  ],
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
      "actors": {
        "type": "nested", // important
        "properties": {
            "name": {
                "type": "text"
            },
            "role": {
                "type": "text"
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
{ "create": {} }
{ "title": "Inception", "actors": [ { "name": "Leonardo DiCaprio", "role": "Cobb" }, { "name": "Joseph Gordon-Levitt", "role": "Arthur" } ], "yearReleased": 2010 }
{ "create": {} }
{ "title": "The Dark Knight", "actors": [ { "name": "Christian Bale", "role": "Bruce Wayne / Batman" }, { "name": "Heath Ledger", "role": "Joker" } ], "yearReleased": 2008 }
{ "create": {} }
{ "title": "Interstellar", "actors": [ { "name": "Matthew McConaughey", "role": "Cooper" }, { "name": "Anne Hathaway", "role": "Brand" } ], "yearReleased": 2014 }

# search
GET /movies/_search?q=interstellar

# we will not see any results. it is expected because of nested type
# we will learn later how to query
GET /movies/_search?q=joker
```