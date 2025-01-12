package com.dobby.backend.infrastructure.database.entity.enum

enum class RoleType(
    val roleName: String
) {
    RESEARCHER("ROLE_RESEARCHER"),
    PARTICIPANT("ROLE_PARTICIPANT")
}
