package com.dobby.domain.gateway.member

import com.dobby.domain.model.member.MemberConsent

interface MemberConsentGateway {
    fun save(savedConsent: MemberConsent): MemberConsent
    fun findByMemberId(memberId: String): MemberConsent?
    fun updateMatchConsent(memberId: String, matchConsent: Boolean): MemberConsent
}
