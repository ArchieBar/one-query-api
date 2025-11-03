# One Query API

[![Java](https://img.shields.io/badge/java-17+-blue.svg)](https://adoptium.net/)
[![Gradle](https://img.shields.io/badge/gradle-8.x-green.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)

One Query API — это надстройка над [jOOQ](https://www.jooq.org/), которая упрощает построение
пагинируемых SQL-запросов. Библиотека автоматически пересобирает запрос, подсчитывает общее
количество элементов и предоставляет вспомогательные инструменты для фильтрации, сортировки,
использования CTE и преобразования результатов

## Возможности
- Быстрое добавление пагинации к существующим `Select*`-запросам jOOQ без ручного подсчёта `COUNT(*)`
- Поддержка динамической фильтрации и сортировки на базе моделей `Filter` и `Sort`
- Работа с общими табличными выражениями (CTE) и выбором отдельных полей
- Гибкая система мапперов для разных SQL диалектов (готовый `PostgresMapper`)
- Интеграция с `ObjectMapper` Jackson и валидацией Jakarta

## Требования
- Java 17 или новее.
- Gradle 8.x (используется Gradle Wrapper, поэтому установка не обязательна)
- Поддерживаемый драйвер БД и диалект, совместимый с jOOQ (из коробки — PostgreSQL)

## Установка
### Gradle (Kotlin DSL)
```kotlin
implementation("one.query.api:one-query-api:0.0.9")
```

### Gradle (Groovy DSL)
```groovy
implementation 'one.query.api:one-query-api:0.0.9'
```

### Maven
```xml
<dependency>
  <groupId>one.query.api</groupId>
  <artifactId>one-query-api</artifactId>
  <version>0.0.9</version>
</dependency>
```

## Конфигурация
1. Зарегистрируйте подходящий `OneQueryMapper` для используемого диалекта, например PostgreSQL:
   ```java
   PostgresMapper mapper = new PostgresMapper(configurationMap);
   OneQueryConfiguration.registerMapper(mapper);
   ```
2. При необходимости настройте `ObjectMapper` Jackson и сопоставление имён полей:
   ```java
   PostgresMapper mapper = new PostgresMapper(customObjectMapper, fieldMap);
   ```
3. Если вы используете Spring, настройте регистрацию мапперов через бины и вызовите `register()` в инициализации

## Примеры использования
### Базовый пример запроса
```java
Filter<String> filter =
    mapper.createFilter(
        C_CNAME.getKey(),
        EQ,
        List.of(
            "Alfreds Futterkiste",
            "Ana Trujillo Emparedados y helados",
            "Antonio Moreno Taquería"));
Sort sort = mapper.createSort(C_ID.getKey(), DESC);
Page page = new Page(0, 2);

PaginationResult<CustomersRecord> result =
    OneQuery.query(ctx.selectFrom(CUSTOMERS))
        .filter(filter)
        .sort(sort)
        .paginate(page)
        .toPaginationResult(CustomersRecord.class);
```

### Подключение CTE
```java
OneQueryCommonTableStep<Record2<Integer, String>> tmp =
    OneQuery.query(ctx.select(SHIPPERS.SHIPPERID, SHIPPERS.SHIPPERNAME).from(SHIPPERS))
        .paginate(0, 2)
        .fields("id", "name");

Long total = tmp.getTotal();
CommonTableExpression<Record2<Integer, String>> cte = tmp.toCommonTable("cte");
```

## FAQ
**Что произойдёт, если диалект не зарегистрирован?**  
`OneQueryConfiguration.getMapper(dialect)` выбросит `NoSuchElementException`. Зарегистрируйте свой `OneQueryMapper` заранее

**Можно ли использовать API без пагинации?**  
Да. Вы можете вызвать `.toList()` или `.fetch()` без `.paginate()` — фильтрация и сортировка будут применены

**Какие интерфейсы jOOQ поддерживаются?**  
Основные `Select*`-шаги, включая `SelectFinalStep`, `SelectConnectByStep`, `SelectQuery` и т.д. Для несовместимых интерфейсов будет выброшено `NotImplementedException`

## Contributing
- Откройте issue с описанием проблемы или предложением
- Оформите pull request с тестами. Все тесты запускаются командой `./gradlew test`
- Соблюдайте стиль кода: используется Spotless + Google Java Format
  
## Лицензия
Лицензировано по [Apache License 2.0](LICENSE).

© 2025 One Query API contributors

