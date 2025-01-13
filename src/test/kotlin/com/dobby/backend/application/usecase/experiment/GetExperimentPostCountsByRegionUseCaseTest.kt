import com.dobby.backend.application.usecase.experiment.GetExperimentPostCountsByRegionUseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jakarta.persistence.Tuple

class GetExperimentPostCountsByRegionUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val getExperimentPostCountsByRegionUseCase = GetExperimentPostCountsByRegionUseCase(experimentPostGateway)

    given("유효한 지역 정보가 주어졌을 때") {
        val regionData = listOf(
            mockk<Tuple>().apply {
                every { get(0, Region::class.java) } returns Region.SEOUL
                every { get(1, Long::class.java) } returns 5L
            },
            mockk<Tuple>().apply {
                every { get(0, Region::class.java) } returns Region.GYEONGGI
                every { get(1, Long::class.java) } returns 10L
            }
        )

        every { experimentPostGateway.countExperimentPosts() } returns 20
        every { experimentPostGateway.countExperimentPostGroupedByRegion() } returns regionData

        `when`("execute가 호출되면") {
            val input = GetExperimentPostCountsByRegionUseCase.Input(region = null)
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
            mockk<Tuple>().apply {
                every { get(0, Region::class.java) } returns Region.SEOUL
                every { get(1, Long::class.java) } returns 5L
            },
            mockk<Tuple>().apply {
                every { get(0, Region::class.java) } returns Region.GYEONGGI
                every { get(1, Long::class.java) } returns 10L
            }
        )

        every { experimentPostGateway.countExperimentPosts() } returns 20
        every { experimentPostGateway.countExperimentPostGroupedByRegion() } returns regionData

        `when`("execute가 호출되면") {
            val input = GetExperimentPostCountsByRegionUseCase.Input(region = null)
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
            val input = GetExperimentPostCountsByRegionUseCase.Input(region = invalidRegionName)

            then("null이 반환된다") {
                val result = runCatching { getExperimentPostCountsByRegionUseCase.execute(input) }.exceptionOrNull()
                result shouldBe null
            }
        }
    }
})
