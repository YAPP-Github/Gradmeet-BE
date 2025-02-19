package com.dobby.backend.infrastructure.database.entity.member

import com.dobby.backend.domain.model.member.MemberConsent
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "member_consent")
class MemberConsentEntity (
    @Id
    @Column(name = "member_id", columnDefinition = "CHAR(13)")
    val memberId: String,

    @Column(name = "ad_consent", nullable = false)
    val adConsent: Boolean,

    @Column(name = "match_consent", nullable = false)
    var matchConsent: Boolean,

    @Column(name = "ad_consented_at")
    val adConsentedAt: LocalDate?,

    @Column(name = "match_consented_at")
    var matchConsentedAt: LocalDate?,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,
){

    fun toDomain() = MemberConsent(
        memberId = memberId,
        adConsent = adConsent,
        matchConsent = matchConsent,
        adConsentedAt = adConsentedAt,
        matchConsentedAt = matchConsentedAt,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(consent: MemberConsent) = with(consent) {
            MemberConsentEntity(
                memberId = memberId,
                adConsent = adConsent,
                matchConsent = matchConsent,
                adConsentedAt = adConsentedAt,
                matchConsentedAt = matchConsentedAt,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }

}
