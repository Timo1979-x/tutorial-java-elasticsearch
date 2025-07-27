## Character Filter

- [Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-charfilters.html)

### Demo

```
# strips html tags / decodes HTML entities like &amp;
POST /_analyze
{
  "text": "I work with tools like <b>Text-Analyzer</b> everyday to process large datasets!!",
  "char_filter": [
    "html_strip"
  ]
}

# ideally we should write like this. but no further configuration is needed. 
POST /_analyze
{
  "text": "I work with tools like <b>Text-Analyzer</b> everyday to process large datasets!!",
  "char_filter": [
    {
      "type": "html_strip"
    }
  ]
}

# mapping char filter
POST /_analyze
{
  "text": "I work with tools like <b>Text-Analyzer</b> daily to process large datasets!!",
  "char_filter": [
    {
      "type": "mapping",
      "mappings": [
        "daily => everyday",
        "- => _",
        "!! => ."
      ]
    }
  ]
}

# pattern replace char filter
POST /_analyze
{
  "text": "At $100, the product is quite expensive.",
  "char_filter": [
    {
      "type": "pattern_replace",
      "pattern": "\\$(\\d+)",
      "replacement": "$1 dollars"
    }
  ]
}
```