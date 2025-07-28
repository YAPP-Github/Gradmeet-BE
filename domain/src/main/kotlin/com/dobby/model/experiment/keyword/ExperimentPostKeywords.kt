package com.dobby.model.experiment.keyword

import com.dobby.enums.MatchType
import com.dobby.enums.experiment.TimeSlot

data class ExperimentPostKeywords(
    val targetGroup: TargetGroupKeyword?,
    val applyMethod: ApplyMethodKeyword?,
    val matchType: MatchType?,
    val reward: String?,
    val count: Int?,
    val timeRequired: TimeSlot?
)
