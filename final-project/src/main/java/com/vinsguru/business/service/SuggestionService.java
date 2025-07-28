package com.vinsguru.business.service;

import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.vinsguru.business.dto.SuggestionRequestParameters;
import com.vinsguru.business.util.Constants;
import com.vinsguru.business.util.NativeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SuggestionService {
    public static final Logger log = LoggerFactory.getLogger(SuggestionService.class);

    private ElasticsearchOperations elasticsearchOperations;

    public SuggestionService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public List<String> fetchSuggestions(SuggestionRequestParameters parameters) {
        log.info("suggestion request: {}", parameters);
        NativeQuery query = NativeQueryBuilder.toSuggestQuery(parameters);
        SearchHits<Object> searchHits = elasticsearchOperations.search(query, Object.class, Constants.Index.SUGGESTION);
        List<String> list = Optional.ofNullable(searchHits.getSuggest())
                .map(s -> s.getSuggestion(Constants.Suggestions.SUGGEST_NAME))
                .stream()
                .map(Suggest.Suggestion::getEntries)
                .flatMap(Collection::stream)
                .map(Suggest.Suggestion.Entry::getOptions)
                .flatMap(Collection::stream)
                .map(Suggest.Suggestion.Entry.Option::getText)
                .toList();
        return list;
    }
}
