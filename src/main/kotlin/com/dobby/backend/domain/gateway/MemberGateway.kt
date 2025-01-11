package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus

interface MemberGateway {
    fun getById(memberId: Long): Member
    fun findByOauthEmailAndStatus(email: String, status: MemberStatus): Member?
    fun findByOauthEmail(email: String): Member?
    fun save(savedMember: Member) : Member
}
