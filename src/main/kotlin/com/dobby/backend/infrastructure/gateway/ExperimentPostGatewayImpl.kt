package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.converter.ExperimentPostConverter
import com.dobby.backend.infrastructure.converter.ParticipantConverter
import com.dobby.backend.infrastructure.database.repository.ExperimentPostRepository
import org.springframework.stereotype.Component

@Component
class ExperimentPostGatewayImpl(
    private val experimentPostRepository: ExperimentPostRepository
): ExperimentPostGateway {
    override fun save(experimentPost: ExperimentPost) : ExperimentPost {
        val entity = ExperimentPostConverter.toEntity(experimentPost)
        val savedEntity = experimentPostRepository.save(entity)
        return ExperimentPostConverter.toModel(savedEntity)
    }
}
