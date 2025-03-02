package com.dobby.domain.gateway.member

import com.dobby.domain.model.member.MemberWithdrawal

interface MemberWithdrawalGateway {
    fun save(memberWithdrawal: MemberWithdrawal): MemberWithdrawal
}
