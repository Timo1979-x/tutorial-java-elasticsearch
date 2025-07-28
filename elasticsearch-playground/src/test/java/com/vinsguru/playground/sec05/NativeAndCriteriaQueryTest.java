package com.vinsguru.playground.sec05;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vinsguru.playground.AbstractTest;
import com.vinsguru.playground.sec05.entity.Garment;
import com.vinsguru.playground.sec05.repository.GarmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;

public class NativeAndCriteriaQueryTest extends AbstractTest {

    @Autowired
    private GarmentRepository repository;

    @BeforeAll
    public void setupData() {
        List<Garment> garments = this.readResource("sec05/garments-data.json", new TypeReference<List<Garment>>() {
        });
        repository.saveAll(garments);
        Assertions.assertEquals(20, repository.count());
    }


}
