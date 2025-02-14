package com.dobby.backend.presentation.api.dto.response.experiment

import java.time.LocalDate
data class PostInfo(
    val experimentPostId: String,
    val title: String,
    val views: Int,
    val place: String?,
    val reward: String?,
    val durationInfo: DurationInfo,
 )

data class DurationInfo(
    val startDate: LocalDate?,
    val endDate: LocalDate?
)
