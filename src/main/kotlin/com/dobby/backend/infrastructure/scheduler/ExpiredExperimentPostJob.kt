package com.dobby.backend.infrastructure.scheduler

import com.dobby.backend.application.service.ExperimentPostService
import com.dobby.backend.application.usecase.experiment.UpdateExpiredExperimentPostUseCase
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ExpiredExperimentPostJob(
    private val experimentPostService: ExperimentPostService
) : Job{
    companion object {
        private val logger : Logger = LoggerFactory.getLogger(ExpiredExperimentPostJob::class.java)
    }

    override fun execute(context: JobExecutionContext) {
        val input = UpdateExpiredExperimentPostUseCase.Input(
            LocalDate.now()
        )
        val output = experimentPostService.updateExpiredExperimentPosts(input)
        logger.info("${output.affectedRowsCount} expired posts have been updated during scheduling jobs.")
    }

}
