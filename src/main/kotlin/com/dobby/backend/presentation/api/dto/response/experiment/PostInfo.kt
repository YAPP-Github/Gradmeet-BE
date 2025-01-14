package com.dobby.backend.presentation.api.dto.response.experiment

import java.time.LocalDate
data class PostInfo(
    val postId: Long,
    val title: String,
    val views: Int,
    val univName: String?,
    val reward: String?,
    val durationInfo: DurationInfo,
 )

data class DurationInfo(
    val startDate: LocalDate?,
    val endDate: LocalDate?
)
