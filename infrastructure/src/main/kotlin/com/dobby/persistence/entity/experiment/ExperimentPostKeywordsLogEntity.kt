package com.dobby.persistence.entity.experiment

import com.dobby.model.experiment.ExperimentPostKeywordsLog
import com.dobby.persistence.entity.member.MemberEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity(name = "experiment_post_keywords_log")
class ExperimentPostKeywordsLogEntity(
    @Id
    @Column(name = "experiment_post_keywords_log_id", columnDefinition = "CHAR(13)")
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: MemberEntity,

    @Column(columnDefinition = "TEXT", nullable = false)
    val response: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
) {
    fun toDomain() = ExperimentPostKeywordsLog(
        id = id,
        member = member.toDomain(),
        response = response,
        createdAt = createdAt
    )

    companion object {
        fun fromDomain(experimentPostKeywordsLog: ExperimentPostKeywordsLog) = with(experimentPostKeywordsLog) {
            ExperimentPostKeywordsLogEntity(
                id = id,
                member = MemberEntity.fromDomain(member),
                response = response,
                createdAt = createdAt
            )
        }
    }
}
