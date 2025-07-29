package com.vinsguru.business.dto;

public record Pagination(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
