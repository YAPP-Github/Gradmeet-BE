package com.dobby.backend.application.mapper

import com.dobby.backend.application.usecase.experiment.GetExperimentPostsUseCase
import com.dobby.backend.domain.model.experiment.CustomFilter
import com.dobby.backend.domain.model.experiment.Pagination
import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ExperimentMapperTest : BehaviorSpec({

    given("유효한 CustomFilterInput이 주어졌을 때") {
        val input = GetExperimentPostsUseCase.CustomFilterInput(
            matchType = MatchType.ONLINE,
            studyTarget = GetExperimentPostsUseCase.StudyTargetInput(
                gender = GenderType.MALE,
                age = 25
            ),
            locationTarget = GetExperimentPostsUseCase.LocationTargetInput(
                region = Region.SEOUL,
                areas = listOf(Area.GANGNAMGU)
            ),
            recruitStatus = false
        )

        `when`("toDomainFilter 메서드가 호출되면") {
            val result: CustomFilter = ExperimentMapper.toDomainFilter(input)

            then("matchType이 올바르게 매핑되어야 한다") {
                result.matchType shouldBe MatchType.ONLINE
            }

            then("studyTarget이 올바르게 매핑되어야 한다") {
                result.studyTarget?.gender shouldBe GenderType.MALE
                result.studyTarget?.age shouldBe 25
            }

            then("locationTarget이 올바르게 매핑되어야 한다") {
                result.locationTarget?.region shouldBe Region.SEOUL
                result.locationTarget?.areas shouldBe listOf(Area.GANGNAMGU)
            }

            then("recruitStatus이 올바르게 매핑되어야 한다") {
                result.recruitStatus shouldBe false
            }
        }
    }

    given("유효한 PaginationInput이 주어졌을 때") {
        val input = GetExperimentPostsUseCase.PaginationInput(page = 2, count = 10)

        `when`("toDomainPagination 메서드가 호출되면") {
            val result: Pagination = ExperimentMapper.toDomainPagination(input)

            then("page가 올바르게 매핑되어야 한다") {
                result.page shouldBe 2
            }

            then("count가 올바르게 매핑되어야 한다") {
                result.count shouldBe 10
            }
        }
    }
})

