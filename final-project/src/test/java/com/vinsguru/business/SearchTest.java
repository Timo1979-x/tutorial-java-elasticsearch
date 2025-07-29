package com.vinsguru.business;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vinsguru.business.dto.Business;
import com.vinsguru.business.dto.SearchResponse;
import com.vinsguru.business.util.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SearchTest extends AbstractTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ElasticsearchOperations elOps;

    public static final String API_PATH = "/api/search?%s";

    @BeforeAll
    public void setUp() {
        Map<String, Object> indexSetting = this.readResource("test-data/business-index-setting.json", new TypeReference<>() {
        });
        Map<String, Object> indexMapping = this.readResource("test-data/business-index-mapping.json", new TypeReference<>() {
        });
        List<Object> data = this.readResource("test-data/business-data.json", new TypeReference<>() {
        });
        IndexOperations indexOps = this.elOps.indexOps(Constants.Index.BUSINESS);
        indexOps.create(indexSetting, Document.from(indexMapping));

        this.elOps
                .withRefreshPolicy(RefreshPolicy.IMMEDIATE)
                .save(data, Constants.Index.BUSINESS);
        SearchHits<Object> searchHits = elOps.search(elOps.matchAllQuery(), Object.class, Constants.Index.BUSINESS);
        long totalHits = searchHits.getTotalHits();
        print(totalHits);
        assertEquals(10, totalHits);
    }

    @ParameterizedTest
    @MethodSource("successTestData")
    public void searchSuccessTest(String parameters, int expectedResultsCount) {
        String path = API_PATH.formatted(parameters);
        ResponseEntity<SearchResponse> responseEntity = restTemplate.getForEntity(
                URI.create(path), SearchResponse.class
        );
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        SearchResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        List<Business> searchResults = responseBody.results();
        assertNotNull(searchResults);
        log.info("response entity: {}", responseBody);
        assertEquals(expectedResultsCount, searchResults.size());
    }

    @ParameterizedTest
    @MethodSource("failureTestData")
    public void searchFailureTest(String parameters, String expectedResult) {
        String path = API_PATH.formatted(parameters);
        ResponseEntity<ProblemDetail> responseEntity = restTemplate.getForEntity(URI.create(path), ProblemDetail.class);
        assertTrue(responseEntity.getStatusCode().is4xxClientError());
        ProblemDetail responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedResult, responseBody.getDetail());
    }

    private Stream<Arguments> successTestData() {
        return Stream.of(
                Arguments.of("query=coffee", 2),   // no filters
                Arguments.of("query=coffee&rating=4.3", 1), // rating filter
                Arguments.of("query=coffee&state=Washington", 1), // state filter
                Arguments.of("query=coffee&offerings=Wi-Fi", 1), // offerings filter
                Arguments.of("query=electronics&distance=5mi&latitude=36.5179&longitude=-94.0298", 0), // distance - no results within 5 miles
                Arguments.of("query=electronics&distance=25mi&latitude=36.5179&longitude=-94.0298", 1), // distance - 1 result within 25 miles
                Arguments.of("query=electronics&distance=5mi&latitude=36.5179", 2), // longitude is missing. so distance can not be applied
                Arguments.of("query=chain&page=0&size=3", 3), // for chain, we have 5 records. when page=0&size=3, we get the first 3
                Arguments.of("query=chain&page=1&size=3", 2), // for chain, we have 5 records. when page=1&size=3, we get the remaining 2
                Arguments.of("query=markat", 1), // fuzzy
                Arguments.of("query=XYZ", 0)  // no match
        );
    }

    public Stream<Arguments> failureTestData() {
        String queryCannotBeEmpty = "query cannot be empty";
        return Stream.of(
                Arguments.of("prefix=", queryCannotBeEmpty),
                Arguments.of("", queryCannotBeEmpty)
        );
    }
}
