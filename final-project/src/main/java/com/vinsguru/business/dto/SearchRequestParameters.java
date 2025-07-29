package com.vinsguru.business.dto;

import com.vinsguru.business.exceptions.BadRequestException;
import org.springframework.util.StringUtils;

import java.util.Objects;

public record SearchRequestParameters(
        String query,
        String distance,
        Double latitude,
        Double longitude,
        Double rating,
        String state,
        String offerings,
        Integer page,
        Integer size
) {
    public SearchRequestParameters {
        if (!StringUtils.hasText(query)) {
            throw new BadRequestException("query cannot be empty");
        }
        page = Objects.requireNonNullElse(page, 0);
        size = Objects.requireNonNullElse(size, 10);
    }
}
