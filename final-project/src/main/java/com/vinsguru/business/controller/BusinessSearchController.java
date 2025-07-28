package com.vinsguru.business.controller;

import com.vinsguru.business.dto.SuggestionRequestParameters;
import com.vinsguru.business.service.SuggestionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BusinessSearchController {
    private final SuggestionService suggestionService;
    public BusinessSearchController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping("/api/suggestions")
    public List<String> fetchSuggestions(SuggestionRequestParameters parameters) {
        return suggestionService.fetchSuggestions(parameters);
    }
}
