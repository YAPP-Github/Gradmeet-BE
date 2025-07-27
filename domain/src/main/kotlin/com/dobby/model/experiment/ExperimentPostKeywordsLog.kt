package com.dobby.model.experiment

import com.dobby.model.experiment.keyword.ExperimentPostKeywords
import com.dobby.model.member.Member
import com.dobby.util.TimeProvider
import java.time.LocalDateTime

data class ExperimentPostKeywordsLog(
    val id: String,
    val member: Member,
    val response: ExperimentPostKeywords,
    val createdAt: LocalDateTime
) {
    companion object {
        fun newExperimentPostKeywordsLog(
            id: String,
            member: Member,
            response: ExperimentPostKeywords
        ) = ExperimentPostKeywordsLog(
            id = id,
            member = member,
            response = response,
            createdAt = TimeProvider.currentDateTime()
        )
    }
}
