package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.model.member.Participant

interface ParticipantGateway {
    fun save(participant: Participant): Participant
}
