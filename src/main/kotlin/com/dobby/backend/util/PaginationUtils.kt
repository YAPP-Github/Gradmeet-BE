package com.dobby.backend.util

fun isLastPage(totalCount: Int, pageSize: Int, currentPage: Int): Boolean {
    return totalCount <= pageSize * currentPage
}
