package com.dobby.domain.model.experiment

import com.dobby.domain.enums.areaInfo.Area
import com.dobby.domain.enums.areaInfo.Region

data class ExperimentPostStats(
    val region: Region,
    val area: Area?,
    val count: Long
)
