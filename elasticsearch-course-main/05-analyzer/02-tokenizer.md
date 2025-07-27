## Tokenizer

- [Reference](https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis-tokenizers.html)

### Demo

```
# standard - divides text into terms on word boundaries & it removes most punctuation symbols. 
# unicode text segmentation algorithm - https://unicode.org/reports/tr29/#Word_Boundaries
# mostly splits when it encouters any non-alphanumeric char (with some exceptions)
POST /_analyze
{
  "text": "This is a sample text to see how tokens are generated.",
  "tokenizer": "standard"
}

POST /_analyze
{
  "text": "Reach out to <b>Support</b> at support@domain.com or send mail to 123,Non Main Street, Atlanta, for assistance!",
  "tokenizer": "standard"
}

POST /_analyze
{
  "text": "Reach out to <b>Support</b> at support@domain.com or send mail to 123,Non Main Street, Atlanta, for assistance!",
  "char_filter": [
    "html_strip"
  ],
  "tokenizer": "standard"
}

# standard + keep urls and emails as they are.
POST /_analyze
{
  "text": "Reach out to Support at support@domain.com or send mail to 123,Non Main Street, Atlanta, for assistance!",
  "tokenizer": "uax_url_email"
}

# divides text into terms whenever it encounters any whitespace character.
POST /_analyze
{
  "text": "Reach out to Support at         support@domain.com or send mail to 123, Non Main Street, Atlanta, for assistance!",
  "tokenizer": "whitespace"
}

# The letter tokenizer breaks text into terms whenever it encounters a character which is not a letter.
POST /_analyze
{
  "text": "Reach out to Support at support@domain.com or send mail to 123,Non Main Street, Atlanta, for assistance!",
  "tokenizer": "letter"
}

# keyword - noop
POST /_analyze
{
  "text": "Reach out to Support at support@domain.com or send mail to 123,Non Main Street, Atlanta, for assistance!",
 "tokenizer": "keyword"
}
```