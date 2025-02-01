package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.member.MemberEntity
import com.dobby.backend.infrastructure.database.entity.enums.MemberStatus
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<MemberEntity, String> {
    fun findByOauthEmail(oauthEmail: String): MemberEntity?
    fun findByOauthEmailAndStatus(oauthEmail: String, status: MemberStatus): MemberEntity?
}
