package com.dobby.model.member

import com.dobby.util.TimeProvider
import java.time.LocalDate
import java.time.LocalDateTime

data class MemberConsent(
    val memberId: String,
    val adConsent: Boolean,
    val matchConsent: Boolean,
    val adConsentedAt: LocalDate?,
    val matchConsentedAt: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun newConsent(
            memberId: String,
            adConsent: Boolean,
            matchConsent: Boolean,
            createdAt: LocalDateTime = TimeProvider.currentDateTime()
        ): MemberConsent {
            val nowDate = createdAt.toLocalDate()

            return MemberConsent(
                memberId = memberId,
                adConsent = adConsent,
                matchConsent = matchConsent,
                adConsentedAt = if (adConsent) nowDate else null,
                matchConsentedAt = if (matchConsent) nowDate else null,
                createdAt = createdAt,
                updatedAt = createdAt
            )
        }
    }
    fun update(
        adConsent: Boolean,
        matchConsent: Boolean,
        updatedAt: LocalDateTime = TimeProvider.currentDateTime()
    ): MemberConsent {
        val nowDate = updatedAt.toLocalDate()
        return this.copy(
            adConsent = adConsent,
            matchConsent = matchConsent,
            adConsentedAt = when {
                adConsent == this.adConsent -> this.adConsentedAt
                adConsent -> nowDate
                else -> null
            },
            matchConsentedAt = when {
                matchConsent == this.matchConsent -> this.matchConsentedAt
                matchConsent -> nowDate
                else -> null
            },
            updatedAt = updatedAt
        )
    }
}
