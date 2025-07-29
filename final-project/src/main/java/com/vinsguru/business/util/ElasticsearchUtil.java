package com.vinsguru.business.util;

import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDistanceQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NumberRangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import co.elastic.clients.elasticsearch.core.search.Suggester;

import java.util.List;
import java.util.function.UnaryOperator;

public class ElasticsearchUtil {
    public static Suggester buildCompletionSuggester(String suggestName, String field, String prefix, int limit) {
        var suggestFuzziness = SuggestFuzziness.of(b -> b
                .fuzziness(Constants.Fuzzy.LEVEL)
                .prefixLength(Constants.Fuzzy.PREFIX_LENGTH));
        CompletionSuggester completionSuggester = CompletionSuggester.of(b -> b
                .field(field)
                .size(limit)
                .fuzzy(suggestFuzziness)
                .skipDuplicates(true)
        );
        var fieldSuggester = FieldSuggester.of(b -> b
                .prefix(prefix)
                .completion(completionSuggester)
        );
        return Suggester.of(b -> b.suggesters(suggestName, fieldSuggester));
    }

    public static Query buildTermQuery(String field, String value, float boost) {
        TermQuery termQuery = TermQuery.of(b -> b
                .field(field)
                .value(value)
                .boost(boost)
                .caseInsensitive(true)
        );
        return Query.of(b -> b.term(termQuery));
    }

    /**
     * usage:
     * <code>
     * Query price = buildRangeQuery("price", builder -> builder.gt(5d).lte(10d));
     * </code>
     *
     * @param field
     * @param function
     * @return
     */
    public static Query buildRangeQuery(String field, UnaryOperator<NumberRangeQuery.Builder> function) {
        NumberRangeQuery numberRangeQuery = NumberRangeQuery.of(b -> function.apply(b.field(field)));
        RangeQuery rangeQuery = RangeQuery.of(b -> b.number(numberRangeQuery));
        return Query.of(b -> b.range(rangeQuery));
    }

    public static Query buildGeoDistanceQuery(String field, String distance, Double latitude, Double longitude) {
        var latlonLocation = LatLonGeoLocation.of(b -> b.lat(latitude).lon(longitude));
        var geoLocation = GeoLocation.of(b -> b.latlon(latlonLocation));
        GeoDistanceQuery geoDistanceQuery = GeoDistanceQuery.of(b -> b
                .field(field)
                .distance(distance)
                .location(geoLocation));
        return Query.of(b -> b.geoDistance(geoDistanceQuery));
    }

    public static Query buildMultiMatchQuery(List<String> fields, String searchTerm) {
        var mmQuery = MultiMatchQuery.of(b -> b
                .query(searchTerm)
                .fields(fields)
                .fuzziness(Constants.Fuzzy.LEVEL)
                .prefixLength(Constants.Fuzzy.PREFIX_LENGTH)
                .type(TextQueryType.MostFields)
                .operator(Operator.And));
        return Query.of(b -> b.multiMatch(mmQuery));
    }

    public static Aggregation buildTermsAggregation(String field) {
        TermsAggregation termsAggregation = TermsAggregation.of(b -> b
                .field(field)
                .size(10)
        );
        return Aggregation.of(b -> b.terms(termsAggregation));
    }
}
