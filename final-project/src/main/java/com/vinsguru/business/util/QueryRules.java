package com.vinsguru.business.util;

import org.springframework.data.util.Predicates;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.vinsguru.business.util.Constants.*;
import static com.vinsguru.business.util.ElasticsearchUtil.*;

public class QueryRules {
    public static final String BOOST_FIELD_FORMAT = "%s^%f";

    public static final QueryRule STATE_QUERY = QueryRule.of(
            srp -> srp.state() != null,
            srp -> buildTermQuery(Business.STATE, srp.state(), 1f)
    );

    public static final QueryRule OFFERINGS_QUERY = QueryRule.of(
            srp -> srp.offerings() != null,
            srp -> buildTermQuery(Business.OFFERINGS_RAW, srp.offerings(), 1f)
    );

    public static final QueryRule RATING_QUERY = QueryRule.of(
            srp -> srp.rating() != null,
            srp -> buildRangeQuery(Business.RATING, b -> b.lte(srp.rating()))
    );

    public static final QueryRule DISTANCE_QUERY = QueryRule.of(
            srp -> Stream.of(srp.distance(), srp.latitude(), srp.longitude()).allMatch(Objects::nonNull),
            srp -> buildGeoDistanceQuery(Business.LOCATION, srp.distance(), srp.latitude(), srp.longitude())
    );

    public static final QueryRule CATEGORY_QUERY = QueryRule.of(
            // query на входе метода не может быть null, поэтому можно пропустить проверку
            Predicates.isTrue(),
            srp -> buildTermQuery(Business.CATEGORY_RAW, srp.query(), 5f)
    );

    private static final List<String> SEARCH_BOOST_FIELDS = List.of(
            boostField(Business.NAME, 2f),
            boostField(Business.CATEGORY, 1.5f),
            boostField(Business.OFFERINGS, 1.5f),
            boostField(Business.ADDRESS, 1.2f),
            Business.DESCRIPTION
    );

    public static final QueryRule SEARCH_QUERY = QueryRule.of(
            // query на входе метода не может быть null, поэтому можно пропустить проверку
            Predicates.isTrue(),
            srp -> buildMultiMatchQuery(SEARCH_BOOST_FIELDS, srp.query())
    );

    private static String boostField(String field, float boost) {
        return BOOST_FIELD_FORMAT.formatted(field, boost);
    }
}
