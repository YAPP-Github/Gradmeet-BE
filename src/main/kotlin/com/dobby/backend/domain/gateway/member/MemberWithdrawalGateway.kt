package com.dobby.backend.domain.gateway.member

import com.dobby.backend.domain.model.member.MemberWithdrawal

interface MemberWithdrawalGateway {
    fun save(memberWithdrawal: MemberWithdrawal): MemberWithdrawal
}
