## Token Filter

- [Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-tokenfilters.html)

### Demo

```
# lowercase token filter
# there is also uppercase token filter
POST /_analyze
{
  "text": "This is a sample text to see how tokens are generated.",
  "tokenizer": "standard",
  "filter": [
    "uppercase"
  ]
}

# lowercase token filter with char filter
POST /_analyze
{
  "text": "Reach out to <b>Support</b> at support@domain.com or send mail to 123,Non Main Street, Atlanta, for assistance!",
  "char_filter": [
    "html_strip"
  ],
  "tokenizer": "standard",
  "filter": [
    "lowercase"
  ]
}

# removes tokens based on length
POST /_analyze
{
  "text": "Reach out to Support at support@domain.com or send mail to 123,Non Main Street, Atlanta, for assistance!",
  "tokenizer": "standard",
  "filter": [
    {
      "type": "length",
      "min": 3,
      "max": 5
    }
  ]
}

# unique
POST /_analyze
{
  "text": "Reach out to Support at support@domain.com or send mail to 123,Non Main Street, Atlanta, for assistance!",
  "tokenizer": "standard",
  "filter": [
    "unique"
  ]
}

/*
synonym filter

couch / sofa
child / kid
car / automobile
buy / purchase
help / assist / support
pc / personal computer
travel / commute / journey
mug / cup / tumbler
*/    
POST /_analyze
{
  "text": "10 Stylish Couch Ideas to Transform Your Living Room",
  "tokenizer": "standard",
  "filter": [
    "lowercase",
    {
      "type": "synonym",
      "synonyms": [
        "couch, sofa",
        "mug, cup, tumbler"
      ]
    }
  ]
}

# synonym filter
POST /_analyze
{
  "text": "Amazon Web Services offers cloud computing solutions for various needs. AWS provides tools for storage, computing power, and database management",
  "tokenizer": "standard",
  "filter": [
    "lowercase",
    {
      "type": "synonym",
      "synonyms": [
        "amazon web services, amazon cloud => aws"
      ]
    }
  ]
}

/*
stop words
a, an, and, are, as, at, be, but, by, for, if, in, into, is, it, no, not, of, on, or, such, that, the, their, then, there, these, they, this, to, was, will, with
*/ 
POST /_analyze
{
  "text": "Reach out to Support at support@domain.com or send mail to 123,Non Main Street, Atlanta, for assistance!",
  "tokenizer": "standard",
  "filter": [
    "stop"
  ]
}

# stemmer
# https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-stemmer-tokenfilter.html
# porter stemmer - https://snowballstem.org/algorithms/porter/stemmer.html
POST /_analyze
{
  "text": "I cooked dishes while sitting comfortably and listening to music in the kitchen.",
  "tokenizer": "standard",
  "filter": [
    "lowercase",
    "stemmer"
  ]
}
```