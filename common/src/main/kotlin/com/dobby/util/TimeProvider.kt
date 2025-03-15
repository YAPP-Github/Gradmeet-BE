package com.dobby.util

import java.time.LocalDateTime

object TimeProvider {
    fun currentDateTime(): LocalDateTime = LocalDateTime.now()
}
