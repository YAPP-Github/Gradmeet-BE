package com.dobby.persistence.identifier

import com.dobby.util.IdGenerator
import com.github.f4b6a3.tsid.TsidCreator
import org.springframework.stereotype.Component

@Component
class TsidIdGenerator : IdGenerator {
    override fun generateId(): String {
        return TsidCreator.getTsid().toString()
    }
}
