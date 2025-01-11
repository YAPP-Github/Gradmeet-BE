package com.dobby.backend.domain.gateway

import com.amazonaws.services.ec2.model.TargetGroup

interface TargetGroupGateway {
    fun save(targetGroup: TargetGroup): TargetGroup
}
