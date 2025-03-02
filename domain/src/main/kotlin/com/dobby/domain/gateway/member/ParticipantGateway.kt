package com.dobby.domain.gateway.member

import com.dobby.domain.model.member.Participant

interface ParticipantGateway {
    fun save(participant: Participant): Participant
    fun findByMemberId(memberId: String): Participant?
}
