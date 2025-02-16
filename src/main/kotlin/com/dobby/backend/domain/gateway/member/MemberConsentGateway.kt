package com.dobby.backend.domain.gateway.member

import com.dobby.backend.domain.model.member.MemberConsent

interface MemberConsentGateway {
    fun save(savedConsent: MemberConsent): MemberConsent
    fun findByMemberId(memberId: String): MemberConsent?
}
