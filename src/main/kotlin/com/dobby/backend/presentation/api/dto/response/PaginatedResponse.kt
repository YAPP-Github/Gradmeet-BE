package com.dobby.backend.presentation.api.dto.response

data class PaginatedResponse<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val isLast: Boolean
)
