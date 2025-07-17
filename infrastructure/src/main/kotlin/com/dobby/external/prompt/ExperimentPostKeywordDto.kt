package com.dobby.external.prompt

import com.dobby.enums.MatchType
import com.dobby.enums.experiment.TimeSlot
import com.fasterxml.jackson.annotation.JsonProperty

data class ExperimentPostKeywordDto(
    @JsonProperty("targetGroupInfo")
    val targetGroup: TargetGroupDto?,
    @JsonProperty("applyMethodInfo")
    val applyMethod: ApplyMethodDto?,
    val matchType: MatchType?,
    val reward: String?,
    val count: Int?,
    val timeRequired: TimeSlot?
)
