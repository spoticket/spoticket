package com.spoticket.teamstadium.global.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginatedResponse<T>(
    Long totalElements,
    Integer totalPages,
    Integer currentPage,
    Integer pageSize,
    List<T> content
) {

  public static <T> PaginatedResponse<T> of(Page<T> page) {
    return new PaginatedResponse<>(
        page.getTotalElements(),
        page.getTotalPages(),
        page.getNumber(),
        page.getSize(),
        page.getContent()
    );
  }
}
