package com.dobby.persistence.repository

import com.dobby.persistence.entity.member.MemberWithdrawalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberWithdrawalRepository: JpaRepository<MemberWithdrawalEntity, String> {
}
