package com.dobby.backend.domain.gateway

import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentPost

interface ApplyMethodGateway {
    fun save(applyMethod: ApplyMethod): ApplyMethod
}
