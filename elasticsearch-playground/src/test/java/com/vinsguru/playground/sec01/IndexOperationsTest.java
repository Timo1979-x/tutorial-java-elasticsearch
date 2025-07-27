package com.vinsguru.playground.sec01;

import com.vinsguru.playground.AbstractTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

import java.util.Map;

public class IndexOperationsTest extends AbstractTest {
    public static final Logger log = LoggerFactory.getLogger(IndexOperationsTest.class);

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Test
    public void createIndex() {
        var indexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of("albums"));
        boolean creationResult = indexOperations.create();
        Assertions.assertTrue(creationResult);
        verifyIndex(indexOperations, 1, 1);
    }

    private void verifyIndex(IndexOperations indexOperations, int expectedShards, int expectedReplicas) {
        Settings settings = indexOperations.getSettings();
        log.info("settings: {}", settings);
        log.info("mappings: {}", indexOperations.getMapping());

        int shards = Integer.parseInt((String) (settings.get("index.number_of_shards")));
        int replicas = Integer.parseInt((String) (settings.get("index.number_of_replicas")));
        Assertions.assertEquals(expectedShards, shards);
        Assertions.assertEquals(expectedReplicas, replicas);

        Assertions.assertTrue(indexOperations.delete());
    }
}
