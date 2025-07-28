package com.vinsguru.business.util;

import com.vinsguru.business.dto.SuggestionRequestParameters;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;

public class NativeQueryBuilder {
    public static NativeQuery toSuggestQuery(SuggestionRequestParameters parameters){
        var suggester = ElasticsearchUtil.buildCompletionSuggester(
                Constants.Suggestions.SUGGEST_NAME,
                Constants.Suggestions.SEARCH_TERM,
                parameters.prefix(),
                parameters.limit());
        return NativeQuery.builder()
                .withSuggester(suggester)
                .withMaxResults(0)
                .withSourceFilter(FetchSourceFilter.of(b -> b.withExcludes("*")))
                .build();
    }
}
