import com.dobby.backend.application.usecase.experiment.GetExperimentPostCountsByRegionUseCase
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.RecruitStatus
import com.dobby.model.experiment.ExperimentPostStats
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jakarta.persistence.Tuple

class GetExperimentPostCountsByRegionUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val getExperimentPostCountsByRegionUseCase = GetExperimentPostCountsByRegionUseCase(experimentPostGateway)

    given("유효한 지역 이름이 주어졌을 때") {
        val regionData = listOf(
            ExperimentPostStats(region = Region.SEOUL, area = null, count = 5L),
            ExperimentPostStats(region = Region.GYEONGGI, area = null, count = 10L)
        )

        every { experimentPostGateway.countExperimentPosts() } returns 20
        every { experimentPostGateway.countExperimentPostGroupedByRegion() } returns regionData

        `when`("모든 공고를 조회하는 execute가 호출되면") {
            val input = GetExperimentPostCountsByRegionUseCase.Input(region = null, recruitStatus = RecruitStatus.ALL)
            val result = getExperimentPostCountsByRegionUseCase.execute(input)

            then("전체 공고 개수가 반환된다") {
                result.total shouldBe 20
            }

            then("지역별 공고 개수가 반환된다") {
                result.area.size shouldBe 17
                result.area.find { it.name == "SEOUL" }?.count shouldBe 5
                result.area.find { it.name == "GYEONGGI" }?.count shouldBe 10
            }
        }

        every { experimentPostGateway.countExperimentPostsByRecruitStatus(true) } returns 20
        every { experimentPostGateway.countExperimentPostsByRecruitStatusGroupedByRegion(true) } returns regionData

        `when`("모집중인 공고를 조회하는 execute가 호출되면") {
            val input = GetExperimentPostCountsByRegionUseCase.Input(region = null, recruitStatus = RecruitStatus.OPEN)
            val result = getExperimentPostCountsByRegionUseCase.execute(input)

            then("전체 공고 개수가 반환된다") {
                result.total shouldBe 20
            }

            then("지역별 공고 개수가 반환된다") {
                result.area.size shouldBe 17
                result.area.find { it.name == "SEOUL" }?.count shouldBe 5
                result.area.find { it.name == "GYEONGGI" }?.count shouldBe 10
            }
        }
    }

    given("지역이 null로 주어졌을 때") {
        val regionData = listOf(
            ExperimentPostStats(region = Region.SEOUL, area = null, count = 5L),
            ExperimentPostStats(region = Region.GYEONGGI, area = null, count = 10L)
        )

        every { experimentPostGateway.countExperimentPosts() } returns 20
        every { experimentPostGateway.countExperimentPostGroupedByRegion() } returns regionData

        `when`("execute가 호출되면") {
            val input = GetExperimentPostCountsByRegionUseCase.Input(region = null, recruitStatus = RecruitStatus.ALL)
            val result = getExperimentPostCountsByRegionUseCase.execute(input)

            then("전체 공고 개수는 20이어야 한다") {
                result.total shouldBe 20
            }

            then("17개의 지역에 대한 공고 개수를 반환한다") {
                result.area.size shouldBe 17
                result.area[0].name shouldBe Region.SEOUL.toString()
                result.area[0].count shouldBe 5
                result.area[1].name shouldBe Region.GYEONGGI.toString()
                result.area[1].count shouldBe 10
            }
        }
    }

    given("잘못된 파라미터가 주어졌을 때") {
        val invalidRegionName = "INVALID_REGION"

        `when`("execute가 호출되면") {
            val input = GetExperimentPostCountsByRegionUseCase.Input(region = invalidRegionName, recruitStatus = RecruitStatus.ALL)

            then("null이 반환된다") {
                val result = runCatching { getExperimentPostCountsByRegionUseCase.execute(input) }.exceptionOrNull()
                result shouldBe null
            }
        }
    }
})
