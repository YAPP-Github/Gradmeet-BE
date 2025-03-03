package com.dobby.api.dto.response.experiment

data class ExperimentPostCountsResponse(
    val total: Int,
    val data: List<DataCount>
)

data class DataCount(
    val name: String,
    val count: Int
)
