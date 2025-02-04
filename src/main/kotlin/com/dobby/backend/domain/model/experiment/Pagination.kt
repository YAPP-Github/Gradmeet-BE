package com.dobby.backend.domain.model.experiment

data class Pagination(
    val page: Int,
    val count: Int
) {
    companion object {
        fun newPagination(page: Int, count: Int): Pagination {
            return Pagination(page, count)
        }
    }
}
