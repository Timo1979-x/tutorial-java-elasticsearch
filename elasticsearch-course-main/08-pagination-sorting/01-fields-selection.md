## Fields Selection

- Selecting specific fields from the result set `select name, age from customer`

### Demo

```
# delete index
DELETE /movies

# create index with the mapping
PUT /movies
{
  "mappings": {
    "properties": {
      "title": { "type" : "text"},
      "year":  { "type" : "integer"},
      "genre":  { "type" : "keyword"},
      "imdb_rating":  { "type" : "float"},
      "director.name": { "type": "text" },
      "director.birth_country": { "type": "keyword" },
      "actors": { 
        "type": "nested",
        "properties": {
          "name": { "type" : "text"},
          "birth_date": { "type" : "date"},
          "birth_country": { "type" : "keyword"}
        }
      }
    }
  }
}

# insert records
POST /movies/_bulk
{ "create": {} }
{"title":"Inception","year":2010,"genre":["Sci-Fi","Action","Thriller"],"director":{"name":"Christopher Nolan","birth_country":"UK"},"actors":[{"name":"Leonardo DiCaprio","birth_date":"1974-11-11","birth_country":"USA"},{"name":"Joseph Gordon-Levitt","birth_date":"1981-02-17","birth_country":"USA"},{"name":"Ellen Page","birth_date":"1987-02-18","birth_country":"CA"}],"imdb_rating":8.8}
{ "create": {} }
{"title":"The Shawshank Redemption","year":1994,"genre":["Drama"],"director":{"name":"Frank Darabont","birth_country":"USA"},"actors":[{"name":"Tim Robbins","birth_date":"1958-10-17","birth_country":"USA"},{"name":"Morgan Freeman","birth_date":"1937-06-01","birth_country":"USA"}],"imdb_rating":9.3}
{ "create": {} }
{"title":"The Godfather","year":1972,"genre":["Crime","Drama"],"director":{"name":"Francis Ford Coppola","birth_country":"USA"},"actors":[{"name":"Marlon Brando","birth_date":"1924-04-03","birth_country":"USA"},{"name":"Al Pacino","birth_date":"1940-04-25","birth_country":"USA"}],"imdb_rating":9.2}
{ "create": {} }
{"title":"The Dark Knight","year":2008,"genre":["Action","Crime","Drama"],"director":{"name":"Christopher Nolan","birth_country":"UK"},"actors":[{"name":"Christian Bale","birth_date":"1974-01-30","birth_country":"UK"},{"name":"Heath Ledger","birth_date":"1979-04-04","birth_country":"AU"}],"imdb_rating":9.0}
{ "create": {} }
{"title":"12 Angry Men","year":1957,"genre":["Drama"],"director":{"name":"Sidney Lumet","birth_country":"USA"},"actors":[{"name":"Henry Fonda","birth_date":"1905-05-16","birth_country":"USA"}],"imdb_rating":8.9}
{ "create": {} }
{"title":"The Lord of the Rings: The Return of the King","year":2003,"genre":["Adventure","Fantasy"],"director":{"name":"Peter Jackson","birth_country":"NZ"},"actors":[{"name":"Elijah Wood","birth_date":"1981-01-28","birth_country":"USA"},{"name":"Viggo Mortensen","birth_date":"1958-10-20","birth_country":"USA"}],"imdb_rating":8.9}
{ "create": {} }
{"title":"Pulp Fiction","year":1994,"genre":["Crime","Drama"],"director":{"name":"Quentin Tarantino","birth_country":"USA"},"actors":[{"name":"John Travolta","birth_date":"1954-02-18","birth_country":"USA"},{"name":"Uma Thurman","birth_date":"1970-04-29","birth_country":"USA"}],"imdb_rating":8.9}
{ "create": {} }
{"title":"Schindler's List","year":1993,"genre":["Biography","Drama","History"],"director":{"name":"Steven Spielberg","birth_country":"USA"},"actors":[{"name":"Liam Neeson","birth_date":"1952-06-07","birth_country":"IE"}],"imdb_rating":8.9}
{ "create": {} }
{"title":"The Godfather Part II","year":1974,"genre":["Crime","Drama"],"director":{"name":"Francis Ford Coppola","birth_country":"USA"},"actors":[{"name":"Al Pacino","birth_date":"1940-04-25","birth_country":"USA"},{"name":"Robert De Niro","birth_date":"1943-11-17","birth_country":"USA"}],"imdb_rating":9.0}
{ "create": {} }
{"title":"Fight Club","year":1999,"genre":["Drama"],"director":{"name":"David Fincher","birth_country":"USA"},"actors":[{"name":"Brad Pitt","birth_date":"1963-12-18","birth_country":"USA"},{"name":"Edward Norton","birth_date":"1969-08-18","birth_country":"USA"}],"imdb_rating":8.8}


# get all
POST /movies/_search
{
  "query": {
    "match_all": {}
  }
}

# disable source
POST /movies/_search
{
  "query": {
    "match_all": {}
  },
  "_source": false
}

# fetch only selected fields
POST /movies/_search
{
  "query": {
    "match_all": {}
  },
  "_source": "title"
}

# fetch only selected fields
POST /movies/_search
{
  "query": {
    "match_all": {}
  },
  "_source": [ "title", "director.name", "actors.name" ]
}

# exclude fields
POST /movies/_search
{
  "query": {
    "match_all": {}
  },
  "_source": {
    "excludes": ["genre", "actors.birth_date" ]
  }
}

```