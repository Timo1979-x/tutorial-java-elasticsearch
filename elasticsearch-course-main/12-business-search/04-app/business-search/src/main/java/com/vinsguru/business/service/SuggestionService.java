package com.vinsguru.business.service;

import com.vinsguru.business.dto.SuggestionRequestParameters;
import com.vinsguru.business.util.Constants;
import com.vinsguru.business.util.NativeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SuggestionService {

    private static final Logger log = LoggerFactory.getLogger(SuggestionService.class);

    private final ElasticsearchOperations elasticsearchOperations;

    public SuggestionService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<String> fetchSuggestions(SuggestionRequestParameters parameters) {
        log.info("suggestion request: {}", parameters);
        var query = NativeQueryBuilder.toSuggestQuery(parameters);
        var searchHits = this.elasticsearchOperations.search(query, Object.class, Constants.Index.SUGGESTION);
        return Optional.ofNullable(searchHits.getSuggest())
                       .map(s -> s.getSuggestion(Constants.Suggestion.SUGGEST_NAME))
                       .stream()
                       .map(Suggest.Suggestion::getEntries)
                       .flatMap(Collection::stream)
                       .map(Suggest.Suggestion.Entry::getOptions)
                       .flatMap(Collection::stream)
                       .map(Suggest.Suggestion.Entry.Option::getText)
                       .toList();
    }

}
