package com.dobby.api.dto.response

data class PaginatedResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalCount: Int,
    val isLast: Boolean
)
