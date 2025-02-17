package com.dobby.backend.application.aop

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class AopForNewTransaction {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun callNewTransaction(operation: () -> Any?): Any? {
        return operation()
    }
}
