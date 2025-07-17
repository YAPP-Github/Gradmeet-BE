package com.dobby.external.prompt

import com.dobby.enums.MatchType
import com.dobby.enums.experiment.TimeSlot
import com.fasterxml.jackson.annotation.JsonProperty

data class ExperimentPostKeywordDto(
    @JsonProperty("targetGroupInfo")
    val targetGroup: TargetGroupDto? = null,
    @JsonProperty("applyMethodInfo")
    val applyMethod: ApplyMethodDto? = null,
    val matchType: String? = null,
    val reward: String? = null,
    val count: Int? = null,
    val timeRequired: String? = null
)
