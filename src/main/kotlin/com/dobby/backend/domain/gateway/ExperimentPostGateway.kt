package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.model.experiment.ExperimentPost

interface ExperimentPostGateway {
    fun save(experimentPost: ExperimentPost): ExperimentPost
}
