package com.dobby.backend.infrastructure.database.entity.enums.experiment

import com.dobby.backend.domain.exception.InvalidRequestValueException

enum class RecruitStatus {
    ALL, OPEN;

    companion object {
        fun fromString(value: String): RecruitStatus {
            return when (value) {
                "ALL" -> ALL
                "OPEN" -> OPEN
                else -> throw InvalidRequestValueException
            }
        }
    }
}
