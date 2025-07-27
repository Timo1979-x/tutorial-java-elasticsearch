## Sorting Results

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


# text fields can NOT be used for sorting as they are analyzed and tokenized
# use keyword field. 
# if you need a text field to sort, remember multi-field mapping

# try to sort using text field
POST /movies/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "title": {
        "order": "asc"
      }
    }
  ]
}

# sory by imdb rating desc
POST /movies/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "imdb_rating": {
        "order": "desc"
      }
    }
  ],
  "_source": ["title", "imdb_rating", "year"]
}

# sory by imdb rating desc and year asc
POST /movies/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "imdb_rating": {
        "order": "desc"
      }
    },
    {
      "year": {
        "order": "asc"
      }
    }
  ],
  "_source": ["title", "imdb_rating","year" ]
}


# sort by director birth country
POST /movies/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "director.birth_country": {
        "order": "asc"
      }
    }
  ],
   "_source": [ "title", "director" ]
}

# sort using nested field
# sory by actor's dob asc and mode is max
POST /movies/_search
{
  "query": {
    "match_all": {}
  },
  "sort": [
    {
      "actors.birth_date": {
        "order": "asc",
        "mode": "max",
        "nested": {
          "path": "actors"
        }
      }
    }
  ]
}
```