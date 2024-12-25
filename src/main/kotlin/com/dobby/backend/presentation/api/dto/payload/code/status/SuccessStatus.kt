package com.dobby.backend.presentation.api.dto.payload.code.status

enum class SuccessStatus(
    val code : String,
    val message: String
) {
    OK("2000", "OK"),
    CREATED("2010", "Created"),
    ACCEPTED("2020", "Accepted"),
    NO_CONTENT("2040", "No Content"),
    RESET_CONTENT("2050", "Reset Content"),
    PARTIAL_CONTENT("2060", "Partial Content"),
    MULTI_STATUS("2070", "Multi-Status"),
    ALREADY_REPORTED("2080", "Already Reported"),
    IM_USED("2260", "IM Used");

}