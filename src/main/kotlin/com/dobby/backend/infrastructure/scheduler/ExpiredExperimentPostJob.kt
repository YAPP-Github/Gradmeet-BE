package com.dobby.backend.infrastructure.scheduler

import com.dobby.backend.infrastructure.database.repository.ExperimentPostCustomRepository
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ExpiredExperimentPostJob(
    private val experimentPostCustomRepository: ExperimentPostCustomRepository
) : Job{
    override fun execute(context: JobExecutionContext) {
        val todayDate = LocalDate.now()
        experimentPostCustomRepository.markExpiredExperimentsAsDone(todayDate)
    }

}
