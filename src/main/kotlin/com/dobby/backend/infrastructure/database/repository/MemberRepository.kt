package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.enums.member.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enums.member.RoleType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository : JpaRepository<MemberEntity, String> {
    fun findByOauthEmail(oauthEmail: String): MemberEntity?
    fun findByOauthEmailAndStatus(oauthEmail: String, status: MemberStatus): MemberEntity?
    fun existsByContactEmail(contactEmail: String): Boolean
    fun findContactEmailById(memberId: String): String
    fun findByContactEmail(contactEmail: String): MemberEntity?
    @Query("SELECT m.role FROM MemberEntity m WHERE m.id = :memberId AND m.deletedAt IS NULL")
    fun findRoleByIdAndDeletedAtIsNull(@Param("memberId") memberId: String): RoleType?
    fun findByIdAndDeletedAtIsNull(memberId: String): MemberEntity?
}
