package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.domain.enums.member.MemberStatus
import com.dobby.backend.domain.enums.member.RoleType
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
