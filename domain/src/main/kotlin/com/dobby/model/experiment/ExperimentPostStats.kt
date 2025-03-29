package com.dobby.model.experiment

import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region

data class ExperimentPostStats(
    val regionName: String?,
    val areaName: String?,
    val count: Long
) {
    val region: Region?
        get() = regionName?.let { Region.valueOf(it) }

    val area: Area?
        get() = areaName?.let { Area.valueOf(it) }
}
