package com.dobby.backend.application.service

import org.springframework.stereotype.Service

@Service
class PaginationService {

    fun isLastPage(totalElements: Int, pageSize: Int, currentPage: Int): Boolean {
        return totalElements <= pageSize * currentPage
    }
}
