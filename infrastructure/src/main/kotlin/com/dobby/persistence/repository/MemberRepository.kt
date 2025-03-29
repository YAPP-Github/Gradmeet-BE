package com.dobby.persistence.repository

import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.RoleType
import com.dobby.persistence.entity.member.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository : JpaRepository<MemberEntity, String> {
    fun findByOauthEmail(oauthEmail: String): MemberEntity?
    fun findByOauthEmailAndStatus(oauthEmail: String, status: MemberStatus): MemberEntity?
    fun existsByContactEmail(contactEmail: String): Boolean

    @Query("SELECT m.contactEmail FROM MemberEntity m WHERE m.id = :memberId")
    fun findContactEmailById(@Param("memberId") memberId: String): String
    fun findByContactEmail(contactEmail: String): MemberEntity?

    @Query("SELECT m.role FROM MemberEntity m WHERE m.id = :memberId AND m.deletedAt IS NULL")
    fun findRoleByIdAndDeletedAtIsNull(@Param("memberId") memberId: String): RoleType?
    fun findByIdAndDeletedAtIsNull(memberId: String): MemberEntity?
    fun findTopByNameOrderByIdDesc(name: String): MemberEntity?
}
