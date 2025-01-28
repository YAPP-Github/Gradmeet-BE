package com.dobby.backend.infrastructure.gateway.member

import com.dobby.backend.domain.gateway.member.ParticipantGateway
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.database.entity.member.ParticipantEntity
import com.dobby.backend.infrastructure.database.repository.ParticipantRepository
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

    override fun findByMemberId(memberId: Long): Participant? {
        return participantRepository
            .findByMemberId(memberId)
            ?.let(ParticipantEntity::toDomain)
    }
}
