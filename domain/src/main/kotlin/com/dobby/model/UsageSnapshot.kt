package com.dobby.model

import java.time.LocalDateTime

data class UsageSnapshot(
    val count: Long,
    val limit: Int,
    val remainingCount: Long,
    val resetsAt: LocalDateTime
)
