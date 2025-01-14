import com.dobby.backend.application.usecase.experiment.GetExperimentPostCountsByAreaUseCase
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import jakarta.persistence.Tuple
import java.security.InvalidParameterException

class GetExperimentPostCountsByAreaUseCaseTest : BehaviorSpec({
    val experimentPostGateway = mockk<ExperimentPostGateway>()
    val getExperimentPostCountsByAreaUseCase = GetExperimentPostCountsByAreaUseCase(experimentPostGateway)

    given("유효한 지역 이름이 주어졌을 때") {
        val regionName = "SEOUL"
        val region = Region.fromDisplayName(regionName)

        val regionData = listOf(
            mockk<Tuple>().apply {
                every { get(0, Area::class.java) } returns Area.SEOUL_ALL
                every { get(1, Long::class.java) } returns 5L
            },
            mockk<Tuple>().apply {
                every { get(0, Area::class.java) } returns Area.GEUMCHEONGU
                every { get(1, Long::class.java) } returns 10L
            }
        )

        every { experimentPostGateway.countExperimentPostsByRegion(region) } returns 15
        every { experimentPostGateway.countExperimentPostByRegionGroupedByArea(region) } returns regionData

        `when`("execute가 호출되면") {
            val input = GetExperimentPostCountsByAreaUseCase.Input(regionName)
            val result = getExperimentPostCountsByAreaUseCase.execute(input)

            then("총 공고 개수가 반환된다") {
                result.total shouldBe 15
            }

            then("지역별 공고 개수가 반환된다") {
                result.area.size shouldBe 26
                result.area[0].name shouldBe Area.SEOUL_ALL.displayName
                result.area[0].count shouldBe 5
                result.area[1].name shouldBe Area.GEUMCHEONGU.displayName
                result.area[1].count shouldBe 10
            }
        }
    }

    given("잘못된 지역 이름이 주어졌을 때") {
        val invalidRegionName = "INVALID_REGION"

        `when`("execute가 호출되면") {
            val input = GetExperimentPostCountsByAreaUseCase.Input(invalidRegionName)

            then("예외가 발생한다") {
                val exception = runCatching { getExperimentPostCountsByAreaUseCase.execute(input) }.exceptionOrNull()
                exception shouldBe InvalidParameterException()
            }
        }
    }
})
