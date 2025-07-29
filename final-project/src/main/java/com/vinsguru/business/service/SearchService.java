package com.vinsguru.business.service;

import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import com.vinsguru.business.dto.Business;
import com.vinsguru.business.dto.Facet;
import com.vinsguru.business.dto.FacetItem;
import com.vinsguru.business.dto.Pagination;
import com.vinsguru.business.dto.SearchRequestParameters;
import com.vinsguru.business.dto.SearchResponse;
import com.vinsguru.business.util.Constants;
import com.vinsguru.business.util.NativeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.Aggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    public static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final ElasticsearchOperations esOps;

    public SearchService(ElasticsearchOperations esOps) {
        this.esOps = esOps;
    }

    public SearchResponse search(SearchRequestParameters p) {
        log.info("search request: {}", p);
        NativeQuery query = NativeQueryBuilder.toSearchQuery(p);
        log.info("bool query: {}", query.getQuery());

        var searchHits = esOps.search(query, Business.class, Constants.Index.BUSINESS);
        return buildResponse(p, searchHits);
    }

    private SearchResponse buildResponse(SearchRequestParameters p, SearchHits<Business> searchHits) {
        List<Business> results = searchHits
                .getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();

        List<Facet> facets = buildFacets((List<ElasticsearchAggregation>) searchHits.getAggregations().aggregations());

        SearchPage<Business> searchPage = SearchHitSupport.searchPageFor(searchHits, PageRequest.of(p.page(), p.size()));
        Pagination pagination = new Pagination(
                searchPage.getNumber(), searchPage.getNumberOfElements(), searchPage.getTotalElements(), searchPage.getTotalPages()
        );
        long timeTaken = searchHits.getExecutionDuration().toMillis();
        return new SearchResponse(results, facets, pagination, timeTaken);
    }

    private List<Facet> buildFacets(List<ElasticsearchAggregation> aggregations) {
        var mapOfAggregates = aggregations
                .stream()
                .map(ElasticsearchAggregation::aggregation)
                .collect(Collectors.toMap(Aggregation::getName, Aggregation::getAggregate));
        List<Facet> facets = List.of(buildFacet(Constants.Business.OFFERINGS_AGGREGATE_NAME, mapOfAggregates.get(Constants.Business.OFFERINGS_AGGREGATE_NAME).sterms()));
        return facets;
    }

    private Facet buildFacet(String name, StringTermsAggregate stringTermsAggregate) {
        List<FacetItem> facetItems = stringTermsAggregate
                .buckets()
                .array()
                .stream()
                .map(x -> new FacetItem(x.key().stringValue(), x.docCount()))
                .toList();
        return new Facet(name, facetItems);
    }
}
