package com.vinsguru.business;

import com.fasterxml.jackson.core.type.TypeReference;
import com.vinsguru.business.util.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.http.ProblemDetail;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SuggestionTest extends AbstractTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private ElasticsearchOperations elOps;

    public static final String API_PATH = "/api/suggestions?%s";

    @BeforeAll
    public void setUp() {
        Map<String, Object> indexMapping = this.readResource("test-data/suggestion-index-mapping.json", new TypeReference<>() {
        });
        List<Object> data = this.readResource("test-data/suggestion-data.json", new TypeReference<>() {
        });
        IndexOperations indexOps = this.elOps.indexOps(Constants.Index.SUGGESTION);
        indexOps.create(new HashMap<>(), Document.from(indexMapping));

        this.elOps
                .withRefreshPolicy(RefreshPolicy.IMMEDIATE)
                .save(data, Constants.Index.SUGGESTION);
        SearchHits<Object> searchHits = elOps.search(elOps.matchAllQuery(), Object.class, Constants.Index.SUGGESTION);
        long totalHits = searchHits.getTotalHits();
        print(totalHits);
        assertEquals(4, totalHits);
    }

    @ParameterizedTest
    @MethodSource("successTestData")
    public void suggestionSuccessTest(String parameters, List<String> expectedResults) {
        String path = API_PATH.formatted(parameters);
        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                RequestEntity.get(URI.create(path)).build(),
                new ParameterizedTypeReference<List<String>>() {
                }
        );
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        log.info("response entity: {}", responseEntity.getBody());
        assertEquals(expectedResults, responseEntity.getBody());
    }

    @ParameterizedTest
    @MethodSource("failureTestData")
    public void suggestionFailureTest(String parameters, String expectedResult) {
        String path = API_PATH.formatted(parameters);
        ResponseEntity<ProblemDetail> responseEntity = restTemplate.getForEntity(URI.create(path), ProblemDetail.class);
        assertTrue(responseEntity.getStatusCode().is4xxClientError());
        ProblemDetail responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedResult, responseBody.getDetail());
    }

    private Stream<Arguments> successTestData() {
        return Stream.of(
                Arguments.of("prefix=w", List.of("walmart")),
                Arguments.of("prefix=c", List.of("cafe", "coffee")),
                Arguments.of("prefix=c&limit=1", List.of("cafe")),
                Arguments.of("prefix=co", List.of("coffee")),
                Arguments.of("prefix=cofe", List.of("coffee")), // fuzzy - but not cafe because of prefix 2
                Arguments.of("prefix=cffee", List.of()), // fuzzy prefix length 2
                Arguments.of("prefix=12", List.of()),
                Arguments.of("prefix=x", List.of())
        );
    }

    public Stream<Arguments> failureTestData() {
        String prefixCannotBeEmpty = "prefix cannot be empty";
        return Stream.of(
                Arguments.of("prefix=", prefixCannotBeEmpty),
                Arguments.of("", prefixCannotBeEmpty)
        );
    }
}
