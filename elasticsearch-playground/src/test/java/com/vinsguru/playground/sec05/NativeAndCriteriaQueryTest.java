package com.vinsguru.playground.sec05;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationRange;
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.StatsAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.fasterxml.jackson.core.type.TypeReference;
import com.vinsguru.playground.AbstractTest;
import com.vinsguru.playground.sec05.entity.Garment;
import com.vinsguru.playground.sec05.repository.GarmentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NativeAndCriteriaQueryTest extends AbstractTest {

    @Autowired
    private GarmentRepository repository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeAll
    public void setupData() {
        List<Garment> garments = this.readResource("sec05/garments-data.json", new TypeReference<List<Garment>>() {
        });
        repository.saveAll(garments);
        assertEquals(20, repository.count());
    }

    @Test
    public void criteriaQueryTest() {
        var nameIsShirt = Criteria.where("name").is("shirt");
        verify("name is shirt", nameIsShirt, 1);

        var priceAbove100 = Criteria.where("price").greaterThan(100);
        verify("priceAbove100", priceAbove100, 5);

        verify("nameIsShirt or price above 100", nameIsShirt.or(priceAbove100), 6);

        var brandIsZara = Criteria.where("brand").is("Zara");
        verify("Price above 100 and brand is not zara", priceAbove100.and(brandIsZara.not()), 3);

        var fuzzyMatchShort = Criteria.where("name").fuzzy("short"); // специально опечатка
        verify("Fuzzy match 'short'", fuzzyMatchShort, 1);

        // We can boost
        // Criteria.where("brand").is("Zara").boost(3.0)

        // We can also do geo point
        // Criteria.where("location").within(point, distance)
    }

    /*
    {
  "query": {
    "bool": {
      "filter": [
        {
          "term": {
            "occasion": "Casual"
          }
        },
        {
            "range": {
              "price": {
                "lte": 50
              }
            }
        }
      ],
      "should": [
        {
          "term": {
            "color": "Brown"
          }
        }
      ]
    }
  }
}
     */
    @Test
    public void boolQuery() {
        Query occasionCasual = Query.of(b -> b.term(
                TermQuery.of(tb -> tb.field("occasion").value("Casual"))
        ));
        Query colorBrown = Query.of(b -> b.term(
                TermQuery.of(tb -> tb.field("color").value("Brown"))
        ));
        Query priceBelow50 = Query.of(b -> b.range(
                RangeQuery.of(rb -> rb.number(
                        NumberRangeQuery.of(nb -> nb.field("price").lte(50d))
                ))
        ));
        Query query = Query.of(b -> b.bool(
                BoolQuery.of(bb -> bb.filter(occasionCasual, priceBelow50).should(colorBrown))
        ));
        NativeQuery nativeQuery = NativeQuery.builder().withQuery(query).build();
        SearchHits<Garment> searchHits = elasticsearchOperations.search(nativeQuery, Garment.class);
        searchHits.forEach(this.print());
        assertEquals(4, searchHits.getTotalHits());
    }

    /*
    {
  "size": 0,
  "aggs": {
    "price-stats": {
      "stats": {
        "field": "price"
      }
    },
    "group-by-brand": {
      "terms": {
        "field": "brand"
      }
    },
    "group-by-color": {
      "terms": {
        "field": "color"
      }
    },
    "price-range": {
      "range": {
        "field": "price",
        "ranges": [
          {
            "to": 50
          },
          {
            "from": 50,
            "to": 100
          },
          {
            "from": 100,
            "to": 150
          },
          {
            "from": 150
          }
        ]
      }
    }
  }
}
     */
    @Test
    public void aggregation() {
        Aggregation priceStats = Aggregation.of(b -> b.stats(
                StatsAggregation.of(sb -> sb.field("price"))
        ));
        Aggregation brandTerms = Aggregation.of(b -> b.terms(
                TermsAggregation.of(tb -> tb.field("brand"))
        ));
        Aggregation colorTerms = Aggregation.of(b -> b.terms(
                TermsAggregation.of(tb -> tb.field("color"))
        ));

        List<AggregationRange> ranges = List.of(
                AggregationRange.of(b -> b.to(50d)),
                AggregationRange.of(b -> b.from(50d).to(100d)),
                AggregationRange.of(b -> b.from(100d).to(150d)),
                AggregationRange.of(b -> b.from(150d))
        );
        Aggregation priceRange = Aggregation.of(b -> b.range(
                RangeAggregation.of(rb -> rb.field("price").ranges(ranges))
        ));
        NativeQuery query = NativeQuery.builder()
                .withMaxResults(0)
                .withAggregation("price-stats", priceStats)
                .withAggregation("group-by-brand", brandTerms)
                .withAggregation("group-by-color", colorTerms)
                .withAggregation("price-range", priceRange)
                .build();
        SearchHits<Garment> searchHits = elasticsearchOperations.search(query, Garment.class);
        var aggregations = (List<ElasticsearchAggregation>) searchHits.getAggregations().aggregations();
        Map<String, Aggregate> map = aggregations.stream()
                .map(ElasticsearchAggregation::aggregation)
                .collect(Collectors.toMap(
                        a -> a.getName(),
                        a -> a.getAggregate()
                ));
        log.info("{}", map);
        assertTrue(map.get("price-stats").isStats());
        assertTrue(map.get("group-by-brand").isSterms());
        assertTrue(map.get("group-by-color").isSterms());
        assertTrue(map.get("price-range").isRange());

        if(map.get("group-by-brand").isSterms()) {
            map.get("group-by-brand").sterms()
                    .buckets()
                    .array()
                    .stream()
                    .map(b -> b.key().stringValue() + ":" + b.docCount())
                    .forEach(this.print());
        }
    }

    private void verify(String title, Criteria criteria, int expectedResultsCount) {
        var printSupplier = this.print();
        printSupplier.accept(title);
        var query = CriteriaQuery.builder(criteria).build();
        SearchHits<Garment> searchHits = elasticsearchOperations.search(query, Garment.class);
        searchHits.getSearchHits().forEach(printSupplier);
        assertEquals(expectedResultsCount, searchHits.getTotalHits());
        printSupplier.accept("-----------------------------");
    }
}
