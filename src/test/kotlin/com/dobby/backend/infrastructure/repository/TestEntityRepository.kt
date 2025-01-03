package com.dobby.backend.infrastructure.repository

import com.dobby.backend.infrastructure.database.entity.TestEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TestEntityRepository: JpaRepository<TestEntity, Long>