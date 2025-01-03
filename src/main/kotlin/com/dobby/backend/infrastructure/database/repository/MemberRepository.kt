package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.MemberEntity
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<MemberEntity, Long> {
    fun findByOauthEmailAndStatus(oauthEmail: String, status: MemberStatus): MemberEntity?
}
