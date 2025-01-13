package com.dobby.backend.presentation.api.dto.response.expirement

data class ExperimentPostCountsResponse(
    val total: Int,
    val data: List<DataCount>
)

data class DataCount(
    val name: String,
    val count: Int
)
