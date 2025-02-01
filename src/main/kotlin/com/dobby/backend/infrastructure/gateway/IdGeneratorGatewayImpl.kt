package com.dobby.backend.infrastructure.gateway

import com.dobby.backend.domain.gateway.IdGeneratorGateway
import com.github.f4b6a3.tsid.TsidCreator
import org.springframework.stereotype.Component

@Component
class IdGeneratorGatewayImpl : IdGeneratorGateway {
    override fun generateId(): String {
        return TsidCreator.getTsid().toString()
    }
}
