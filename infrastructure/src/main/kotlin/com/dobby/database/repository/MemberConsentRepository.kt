package com.dobby.database.repository

import com.dobby.database.entity.member.MemberConsentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberConsentRepository: JpaRepository<MemberConsentEntity, String> {
    fun findByMemberId(memberId: String): MemberConsentEntity?
}
