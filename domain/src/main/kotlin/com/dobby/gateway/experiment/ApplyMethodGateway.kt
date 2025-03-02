package com.dobby.gateway.experiment

import com.dobby.model.experiment.ApplyMethod

interface ApplyMethodGateway {
    fun save(applyMethod: ApplyMethod): ApplyMethod
}
