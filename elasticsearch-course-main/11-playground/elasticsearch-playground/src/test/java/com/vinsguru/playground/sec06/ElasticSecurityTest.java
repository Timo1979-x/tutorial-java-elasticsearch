package com.vinsguru.playground.sec06;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

/*
This class aims to demo the application's connection to Elasticsearch with password authentication and SSL enabled.
Before running this test, run the Docker Compose file located in src/test/resources/sec06 manually.
 */

@SpringBootTest
@TestPropertySource(locations = "classpath:sec06/application.properties")
public class ElasticSecurityTest {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    record Student(String name, int age){}

    @Test
    public void securityDemo(){

        var indexCoordinates = IndexCoordinates.of("students");
        var indexOperations = this.elasticsearchOperations.indexOps(indexCoordinates);

        // create index
        Assertions.assertTrue(indexOperations.create());

        var students = List.of(
                new Student("sam", 10),
                new Student("mike", 9)
        );

        // save docs
        this.elasticsearchOperations.withRefreshPolicy(RefreshPolicy.IMMEDIATE).save(students, indexCoordinates);

        // query docs
        var searchHits = this.elasticsearchOperations.search(
                this.elasticsearchOperations.matchAllQuery(),
                Student.class,
                indexCoordinates
        );

        // let's confirm if we have 2 docs
        Assertions.assertEquals(2, searchHits.getTotalHits());

        // delete
        Assertions.assertTrue(indexOperations.delete());

    }

}
