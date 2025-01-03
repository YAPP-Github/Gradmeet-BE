package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByOauthEmailAndStatus(oauthEmail: String, status: MemberStatus): Member?
}
