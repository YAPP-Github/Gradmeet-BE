package com.dobby.backend.domain.model.member

import java.time.LocalDate
import java.time.LocalDateTime

data class MemberConsent(
    val memberId: String,
    val adConsent: Boolean,
    val matchConsent: Boolean,
    val adConsentedAt: LocalDate?,
    val matchConsentedAt: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun newConsent(
            memberId: String,
            adConsent: Boolean,
            matchConsent: Boolean,
            createdAt: LocalDateTime = LocalDateTime.now()
        ) : MemberConsent {
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
        memberId: String,
        adConsent: Boolean,
        matchConsent: Boolean,
        updatedAt: LocalDateTime = LocalDateTime.now()
    ): MemberConsent {
        val nowDate = updatedAt.toLocalDate()
        return this.copy(
            memberId = memberId,
            adConsent = adConsent,
            matchConsent = matchConsent,
            adConsentedAt = if(adConsent) nowDate else null,
            matchConsentedAt = if(matchConsent) nowDate else null,
            updatedAt = updatedAt
        )
    }
}
