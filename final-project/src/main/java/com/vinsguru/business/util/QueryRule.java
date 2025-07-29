package com.vinsguru.business.util;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.vinsguru.business.dto.SearchRequestParameters;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public record QueryRule(Predicate<SearchRequestParameters> predicate,
                        Function<SearchRequestParameters, Query> function) {
    public static QueryRule of(Predicate<SearchRequestParameters> predicate,
                               Function<SearchRequestParameters, Query> function) {
        return new QueryRule(predicate, function);
    }

    public Optional<Query> build(SearchRequestParameters parameters) {
        Optional<Query> query = Optional.of(parameters)
                .filter(predicate())
                .map(function());
        return query;
    }
}
