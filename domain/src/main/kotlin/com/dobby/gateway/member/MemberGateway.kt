package com.dobby.gateway.member

import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.RoleType
import com.dobby.model.member.Member

interface MemberGateway {
    fun getById(memberId: String): Member
    fun findByIdAndDeletedAtIsNull(memberId: String): Member?
    fun findByOauthEmailAndStatus(email: String, status: MemberStatus): Member?
    fun findByOauthEmail(email: String): Member?
    fun save(savedMember: Member): Member
    fun existsByContactEmail(contactEmail: String): Boolean
    fun findContactEmailByMemberId(memberId: String): String
    fun findByContactEmail(contactEmail: String): Member?
    fun findRoleByIdAndDeletedAtIsNull(memberId: String): RoleType?
    fun findMemberIdByName(name: String): String?
}
