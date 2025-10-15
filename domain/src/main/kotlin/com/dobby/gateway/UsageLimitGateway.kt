package com.dobby.gateway

interface UsageLimitGateway {
    fun incrementAndCheckLimit(memberId: String, dailyLimit: Int): Boolean
}
