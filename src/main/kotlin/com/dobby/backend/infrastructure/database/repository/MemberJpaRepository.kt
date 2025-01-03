package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.infrastructure.database.entity.MemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberEntity, Long> {
}
