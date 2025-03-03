package com.dobby.database.repository

import com.dobby.database.entity.member.MemberWithdrawalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberWithdrawalRepository: JpaRepository<MemberWithdrawalEntity, String> {
}
