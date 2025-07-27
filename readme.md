
### 03-02-cluster-partial
тут создается кластер, у которого ноды es02 и es03 не знают ничего изначально друг о друге, а знают только про es01.   
после запуска отключаем es01 и наблюдаем, что es02 и es03 уже знают друг о друге и берут на себя роли исчезнувшей ноды

### 03-06-node-roles
делаем нодам ограничение по ролям. es01 может быть только мастером, es02 и es03 - только избирателями и хранилками. ПРИМ: чтобы быть избирателем, ноде всё равно нужно указать роль master  

## Лекции
### lesson12-127
Первый тест по созданию и удалению индекса (com.vinsguru.playground.sec01.IndexOperationsTest.createIndex).

Тесты можно запускать так:
```bash
./gradlew clean test --tests com.vinsguru.playground.sec01.IndexOperationsTest.createIndex
```

### lesson12-128
Создание индексов с использованием конфигурационных файлов и аннотаций

### lesson12-129
Работа с документами с использованием `org.springframework.data.elasticsearch.repository.ElasticsearchRepositoryorg.springframework.data.elasticsearch.repository.ElasticsearchRepository`

### lesson12-130
Массовая вставка/обновление/удаление с использованием `org.springframework.data.elasticsearch.repository.ElasticsearchRepositoryorg.springframework.data.elasticsearch.repository.ElasticsearchRepository`

