package com.dobby.gateway

import com.dobby.model.UsageSnapshot

interface UsageLimitGateway {
    fun incrementAndCheckLimit(memberId: String, dailyLimit: Int): Boolean
    fun getCurrentUsage(memberId: String, dailyLimit: Int): UsageSnapshot
}
