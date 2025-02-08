package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.enums.member.MemberStatus
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<MemberEntity, String> {
    fun findByOauthEmail(oauthEmail: String): MemberEntity?
    fun findByOauthEmailAndStatus(oauthEmail: String, status: MemberStatus): MemberEntity?
    fun existsByContactEmail(contactEmail: String): Boolean
    fun findContactEmailById(memberId: String): String
    fun findByContactEmail(contactEmail: String): MemberEntity?
}
