package com.dobby.backend.domain.gateway.experiment

import com.dobby.backend.domain.model.experiment.ApplyMethod

interface ApplyMethodGateway {
    fun save(applyMethod: ApplyMethod): ApplyMethod
}
