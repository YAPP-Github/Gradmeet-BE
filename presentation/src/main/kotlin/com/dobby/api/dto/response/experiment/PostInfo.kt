package com.dobby.api.dto.response.experiment
import com.dobby.enums.experiment.TimeSlot
import java.time.LocalDate
data class PostInfo(
    val experimentPostId: String,
    val title: String,
    val views: Int,
    val timeRequired: TimeSlot?,
    val count: Int?,
    val isOnCampus: Boolean,
    val place: String?,
    val reward: String?,
    val durationInfo: DurationInfo
)

data class DurationInfo(
    val startDate: LocalDate?,
    val endDate: LocalDate?
)
