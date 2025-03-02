package com.dobby.gateway.member

import com.dobby.model.member.MemberWithdrawal

interface MemberWithdrawalGateway {
    fun save(memberWithdrawal: MemberWithdrawal): MemberWithdrawal
}
