package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.Participant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipantRepository: JpaRepository<Participant, Long> {
}