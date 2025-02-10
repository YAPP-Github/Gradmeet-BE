package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.member.MemberWithdrawalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberWithdrawalRepository: JpaRepository<MemberWithdrawalEntity, String> {
}
