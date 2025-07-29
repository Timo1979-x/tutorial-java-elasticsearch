package com.vinsguru.business.controller;

import com.vinsguru.business.dto.SearchRequestParameters;
import com.vinsguru.business.dto.SearchResponse;
import com.vinsguru.business.dto.SuggestionRequestParameters;
import com.vinsguru.business.service.SearchService;
import com.vinsguru.business.service.SuggestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class BusinessSearchController {
    private final SearchService searchService;
    private final SuggestionService suggestionService;

    public BusinessSearchController(SearchService searchService, SuggestionService suggestionService) {
        this.searchService = searchService;
        this.suggestionService = suggestionService;
    }

    @GetMapping("/suggestions")
    public List<String> fetchSuggestions(SuggestionRequestParameters parameters) {
        return suggestionService.fetchSuggestions(parameters);
    }

    @GetMapping("/search")
    public SearchResponse search(SearchRequestParameters parameters) {
        return searchService.search(parameters);
    }
}
