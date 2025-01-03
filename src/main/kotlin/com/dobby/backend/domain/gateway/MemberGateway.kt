package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.model.Member

interface MemberGateway {
    fun getById(memberId: Long): Member
}
