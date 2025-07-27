package com.vinsguru.business.dto;

import com.vinsguru.business.exceptions.BadRequestException;
import org.springframework.util.StringUtils;

import java.util.Objects;

public record SuggestionRequestParameters(String prefix,
                                          Integer limit) {

    public SuggestionRequestParameters {
        if(!StringUtils.hasText(prefix)){
            throw new BadRequestException("prefix can not be empty");
        }
        limit = Objects.requireNonNullElse(limit, 10);
    }

}
