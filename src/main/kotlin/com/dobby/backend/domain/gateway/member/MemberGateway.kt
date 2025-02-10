package com.dobby.backend.domain.gateway.member

import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.member.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enums.member.RoleType

interface MemberGateway {
    fun getById(memberId: String): Member
    fun findByIdAndDeletedAtIsNull(memberId: String): Member?
    fun findByOauthEmailAndStatus(email: String, status: MemberStatus): Member?
    fun findByOauthEmail(email: String): Member?
    fun save(savedMember: Member) : Member
    fun existsByContactEmail(contactEmail: String) : Boolean
    fun findContactEmailByMemberId(memberId: String): String
    fun findByContactEmail(contactEmail: String): Member?
    fun findRoleByIdAndDeletedAtIsNull(memberId: String): RoleType?
}
