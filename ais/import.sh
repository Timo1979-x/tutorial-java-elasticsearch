#!/bin/bash

curl -XDELETE http://localhost:9200/ref-models | jq

curl -XPUT http://localhost:9200/ref-models \
  --header 'Content-Type: application/json' \
  --data '{
  "mappings": {
    "properties": {
      "id": { "type": "integer"},
      "name_rus": { "type" : "search_as_you_type" },
      "name_eng": { "type" : "search_as_you_type" },
      "d1":  { "type" : "date"},
      "d2":  { "type" : "date"}
    }
  }
}' | jq

# curl -XPUT http://localhost:9200/ref-models \
#   --header 'Content-Type: application/json' \
#   --data '{
#   "mappings": {
#     "properties": {
#       "id": { "type": "integer"},
#       "name_rus":  { 
#         "type" : "text",
#         "fields": {
#           "completion": {
#             "type": "completion"
#           }
#         }
#       },
#       "name_eng":  { 
#         "type" : "text",
#         "fields": {
#           "completion": {
#             "type": "completion"
#           }
#         }
#       },
#       "d1":  { "type" : "date"},
#       "d2":  { "type" : "date"}
#     }
#   }
# }' | jq


jq -c '.[] | [{ "create": { "_id": (.id|tostring) } }, { name_rus, name_eng, d1, d2 }]' ./gai-model.json | jq -c '.[]' > gai-model.ndjson

curl -XPOST http://localhost:9200/ref-models/_bulk --header "Content-Type: application/x-ndjson" --data-binary @gai-model.ndjson | jq

curl -XPOST http://localhost:9200/ref-models/_search \
  --header "Content-Type: application/x-ndjson" \
  --data '{
  "size": 0,
  "aggs": { "total_count": { "value_count": { "field": "d1"}} }
}' | jq