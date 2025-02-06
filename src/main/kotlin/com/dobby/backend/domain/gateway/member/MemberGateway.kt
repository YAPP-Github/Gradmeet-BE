package com.dobby.backend.domain.gateway.member

import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.MemberStatus

interface MemberGateway {
    fun getById(memberId: String): Member
    fun findById(memberId: String): Member?
    fun findByOauthEmailAndStatus(email: String, status: MemberStatus): Member?
    fun findByOauthEmail(email: String): Member?
    fun save(savedMember: Member) : Member
    fun existsByContactEmail(contactEmail: String) : Boolean
    fun findContactEmailByMemberId(memberId: String): String
}
