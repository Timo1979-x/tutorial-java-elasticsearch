## что это
Попытки создать индекс для справочников в ElasticSearch. Начал с марок/моделей.  

## Подготовка
сначала выгрузить справочник из SQL базы таким запросом в формат json:
```sql
select id, name_rus, name_eng, '1900-01-01' as d1, '9999-01-01' as d2 from ti.gai_model m;
```

должен получиться такой json:
```json
// gai-model.json
[
  	{
		"id": 7020478,
		"name_rus": "HONGYAN 908",
		"name_eng": "HONGYAN 908",
		"d1": "1900-01-01",
		"d2": "9999-01-01"
	}
  ...
]   
```

Потом запустить скрипт `import.sh`, который создаст в Elasticsearch нужный индекс, преобразует выгруженные из SQL данные и загрузит их в ElasticSearch.

## Запросы
Получить первых 10 элементов справочника:
```
post /ref-models/_search
{
  "query": {
    "match_all": {}
  }
}
```

поискать
```json
post /ref-models/_search
{
  "query": {
    "multi_match": {
      "query": "VOLKSWAGEN g",
      "type": "bool_prefix",
      "fields": [
        "name_eng",
        "name_eng._2gram",
        "name_eng._3gram",
        "name_rus",
        "name_rus._2gram",
        "name_rus._3gram"
      ]
    }
  },
  "_source": ["name_rus", "name_eng"]
}
```