package com.dobby.persistence.repository

import com.dobby.persistence.entity.member.MemberConsentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberConsentRepository : JpaRepository<MemberConsentEntity, String> {
    fun findByMemberId(memberId: String): MemberConsentEntity?
}
