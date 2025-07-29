package com.vinsguru.business.dto;

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
}
