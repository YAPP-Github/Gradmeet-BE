package com.dobby.model.experiment

import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region

data class ExperimentPostStats(
    val region: Region,
    val area: Area?,
    val count: Long
)
