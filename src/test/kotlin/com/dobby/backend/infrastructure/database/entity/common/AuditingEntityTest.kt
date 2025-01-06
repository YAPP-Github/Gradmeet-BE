package com.dobby.backend.infrastructure.database.entity.common

import com.dobby.backend.infrastructure.database.entity.TestEntity
import com.dobby.backend.infrastructure.repository.TestEntityRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.date.shouldBeAfter
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
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
            then("createdAt과 updatedAt이 동일해야 한다") {
                savedEntity.createdAt shouldBe savedEntity.updatedAt
            }
        }
    }

    given("AuditingEntity의 setter 메서드를 호출할 때") {
        `when`("setCreatedAt과 setUpdatedAt을 호출하면") {
            val entity = TestEntity(name = "Test Entity")

            then("setter 메서드가 정상적으로 호출된다") {
                val now = LocalDateTime.now()
                entity.createdAt = now
                entity.updatedAt = now

                entity.createdAt shouldBe now
                entity.updatedAt shouldBe now
            }
        }
    }

    given("AuditingEntity의 Getter 메서드를 호출할 때") {
        `when`("createdAt과 updatedAt이 설정되었다면") {
            val entity = TestEntity(name = "Test Entity")
            val now = LocalDateTime.now()

            entity.createdAt = now
            entity.updatedAt = now

            then("getCreatedAt과 getUpdatedAt이 정상적으로 값을 반환해야 한다") {
                entity.createdAt shouldBe now
                entity.updatedAt shouldBe now
            }
        }

        `when`("createdAt과 updatedAt이 설정되지 않았다면") {
            val entity = testEntityRepository.save(TestEntity(name = "Test Entity"))

            then("getCreatedAt과 getUpdatedAt은 null이 아니어야 한다") {
                entity.createdAt shouldNotBe null
                entity.updatedAt shouldNotBe null
            }

            then("getCreatedAt과 getUpdatedAt이 저장 시점의 값이어야 한다") {
                entity.updatedAt shouldBe entity.createdAt
            }
        }
    }

    given("AuditingEntity가 수정될 때") {
        `when`("updatedAt이 변경되면") {
            val entity = testEntityRepository.save(TestEntity(name = "Initial Name"))

            Thread.sleep(1000) // 수정 시간을 구분하기 위해 대기
            entity.name = "Updated Name"
            val updatedEntity = testEntityRepository.save(entity)

            then("updatedAt, createdAt은 null이 아니어야 한다") {
                updatedEntity.updatedAt shouldNotBe null
                updatedEntity.createdAt shouldNotBe null
            }
            then("updatedAt은 createdAt 이후의 시각이어야 한다") {
                updatedEntity.updatedAt shouldNotBe entity.createdAt
                updatedEntity.updatedAt shouldBeAfter entity.createdAt
            }
        }
    }

    given("AuditingEntity가 삭제될 때") {
        `when`("삭제 작업이 수행되면") {
            val entity = testEntityRepository.save(TestEntity(name = "Entity to Delete"))
            testEntityRepository.delete(entity)
            then("해당 엔티티는 더 이상 데이터베이스에 존재하지 않아야 한다") {
                val isDeleted = testEntityRepository.findById(entity.id!!).isEmpty
                isDeleted shouldBe true
            }
        }
    }
}
)
