package com.dobby.gateway.member

import com.dobby.model.member.Participant

interface ParticipantGateway {
    fun save(participant: Participant): Participant
    fun findByMemberId(memberId: String): Participant?
}
