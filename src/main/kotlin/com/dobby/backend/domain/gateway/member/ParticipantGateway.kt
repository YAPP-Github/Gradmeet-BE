package com.dobby.backend.domain.gateway.member

import com.dobby.backend.domain.model.member.Participant

interface ParticipantGateway {
    fun save(participant: Participant): Participant
    fun findByMemberId(memberId: String): Participant?
}
