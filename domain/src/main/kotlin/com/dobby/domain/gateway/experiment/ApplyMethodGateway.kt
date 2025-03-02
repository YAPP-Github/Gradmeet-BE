package com.dobby.domain.gateway.experiment

import com.dobby.domain.model.experiment.ApplyMethod

interface ApplyMethodGateway {
    fun save(applyMethod: ApplyMethod): ApplyMethod
}
