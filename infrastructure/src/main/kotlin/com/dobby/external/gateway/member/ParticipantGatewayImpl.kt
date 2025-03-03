package com.dobby.external.gateway.member

import com.dobby.model.member.Participant
import com.dobby.persistence.entity.member.ParticipantEntity
import com.dobby.persistence.repository.ParticipantRepository
import org.springframework.stereotype.Component

@Component
class ParticipantGatewayImpl(
    private val participantRepository: ParticipantRepository
): ParticipantGateway {
    override fun save(participant: Participant) : Participant {
        val savedEntity = participantRepository
            .save(ParticipantEntity.fromDomain(participant))
        return savedEntity.toDomain()
    }

    override fun findByMemberId(memberId: String): Participant? {
        return participantRepository
            .findByMemberId(memberId)
            ?.let(ParticipantEntity::toDomain)
    }
}
