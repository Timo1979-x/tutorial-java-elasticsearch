package com.vinsguru.business.service;

import com.vinsguru.business.dto.SuggestionRequestParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuggestionService {
    public static final Logger log = LoggerFactory.getLogger(SuggestionService.class);

    private ElasticsearchOperations elasticsearchOperations;

    public SuggestionService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<String> fetchSuggestions(SuggestionRequestParameters parameters) {
        log.info("suggestion request: {}", parameters);

    }
}
