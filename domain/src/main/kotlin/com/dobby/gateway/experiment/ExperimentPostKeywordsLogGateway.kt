package com.dobby.gateway.experiment

import com.dobby.model.experiment.ExperimentPostKeywordsLog

interface ExperimentPostKeywordsLogGateway {
    fun save(experimentPostKeywordsLog: ExperimentPostKeywordsLog): ExperimentPostKeywordsLog
}
