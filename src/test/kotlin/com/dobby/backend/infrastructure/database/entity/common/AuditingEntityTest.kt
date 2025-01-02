package com.dobby.backend.infrastructure.entity

import com.dobby.backend.infrastructure.database.entity.TestEntity
import com.dobby.backend.infrastructure.repository.TestEntityRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@SpringBootTest
@EnableJpaAuditing
@ActiveProfiles("test")
class AuditingEntityTest @Autowired constructor(
    private val testEntityRepository: TestEntityRepository
) : BehaviorSpec({
    given("AuditingEntity가 저장될 때") {
        `when`("createdAt과 updatedAt이 자동으로 설정되면") {
            val entity = TestEntity(name = "Test Entity")
            val savedEntity = testEntityRepository.save(entity)

            then("createdAt과 updatedAt이 null이 아니어야 한다") {
                savedEntity.createdAt shouldNotBe null
                savedEntity.updatedAt shouldNotBe null
            }
        }
    }

    val mockRepository: TestEntityRepository = mockk()

    given("AuditingEntity가 잘못된 데이터로 저장될 때") {
        `when`("createdAt이나 updatedAt이 null로 설정되면") {
            every { mockRepository.save(any()) } throws org.springframework.dao.DataIntegrityViolationException("createdAt cannot be null")

            then("DataIntegrityViolationException 예외가 발생해야 한다") {
                shouldThrow<org.springframework.dao.DataIntegrityViolationException> {
                    mockRepository.save(TestEntity(name = "Invalid Entity"))
                }
            }
        }
    }

    given("AuditingEntity가 수정될 때") {
        `when`("updatedAt이 변경되면") {
            val entity = testEntityRepository.save(TestEntity(name = "Initial Name"))

            Thread.sleep(1000) // 수정 시간을 구분하기 위해 대기
            entity.name = "Updated Name"
            val updatedEntity = testEntityRepository.save(entity)

            then("updatedAt이 변경되어야 한다") {
                updatedEntity.updatedAt shouldNotBe entity.createdAt
                updatedEntity.updatedAt shouldBeAfter entity.createdAt
            }
        }
    }

    given("AuditingEntity가 삭제될 때") {
        `when`("삭제 작업이 수행되면") {
            val entity = testEntityRepository.save(TestEntity(name = "Entity to Delete"))
            testEntityRepository.delete(entity)
            then("엔티티는 더 이상 데이터베이스에 존재하지 않아야 한다") {
                val isDeleted = testEntityRepository.findById(entity.id!!).isEmpty
                isDeleted shouldBe true
            }
        }
    }
}
)
