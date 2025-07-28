package com.vinsguru.playground.sec05;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vinsguru.playground.AbstractTest;
import com.vinsguru.playground.sec05.entity.Garment;
import com.vinsguru.playground.sec05.repository.GarmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;

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
        Assertions.assertEquals(20, repository.count());
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

    private void verify(String title, Criteria criteria, int expectedResultsCount) {
        var printSupplier = this.print();
        printSupplier.accept(title);
        var query = CriteriaQuery.builder(criteria).build();
        SearchHits<Garment> searchHits = elasticsearchOperations.search(query, Garment.class);
        searchHits.getSearchHits().forEach(printSupplier);
        Assertions.assertEquals(expectedResultsCount, searchHits.getTotalHits());
        printSupplier.accept("-----------------------------");
    }
}
