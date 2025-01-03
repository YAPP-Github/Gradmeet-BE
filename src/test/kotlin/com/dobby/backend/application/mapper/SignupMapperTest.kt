package com.dobby.backend.application.mapper

import com.dobby.backend.infrastructure.database.entity.enum.*
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import com.dobby.backend.presentation.api.dto.request.ParticipantSignupRequest
import org.springframework.test.context.ActiveProfiles
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import com.dobby.backend.presentation.api.dto.request.AddressInfo as DtoAddressInfo

@ActiveProfiles("test")
class SignupMapperTest : BehaviorSpec({
    given("주소 dto를 AddressInfo 엔티티로 변환할 때") {
        `when`("toAddressInfoDto 메서드가 호출되면") {
            val dtoAddressInfo = DtoAddressInfo(region = Region.SEOUL, area = Area.SEOUL_ALL)
            val result = SignupMapper.toAddressInfo(dtoAddressInfo)

            then("올바른 AddressInfo 객체가 반환되어야 한다") {
                result.region shouldBe Region.SEOUL
                result.area shouldBe Area.SEOUL_ALL
            }
        }

        `when`("toMember 메서드가 호출되면") {
            val request = ParticipantSignupRequest(
                oauthEmail = "test@example.com",
                provider = ProviderType.GOOGLE,
                contactEmail = "contact@example.com",
                name = "Test User",
                birthDate = LocalDate.of(2002, 11, 21),
                basicAddressInfo = DtoAddressInfo(region = Region.SEOUL, area = Area.SEOUL_ALL),
                additionalAddressInfo = null,
                preferType = MatchType.HYBRID,
                gender = GenderType.FEMALE
            )
            val result = SignupMapper.toMember(request)

            then("올바른 MemberEntity 객체가 반환되어야 한다") {
                result.oauthEmail shouldBe "test@example.com"
                result.provider shouldBe ProviderType.GOOGLE
                result.contactEmail shouldBe "contact@example.com"
                result.name shouldBe "Test User"
                result.birthDate shouldBe LocalDate.of(2002, 11, 21)
                result.role shouldBe RoleType.PARTICIPANT
                result.status shouldBe MemberStatus.ACTIVE
            }
        }

        `when`("toParticipant 메서드가 호출되면") {
            val request = ParticipantSignupRequest(
                oauthEmail = "test@example.com",
                provider = ProviderType.GOOGLE,
                contactEmail = "contact@example.com",
                name = "Test User",
                birthDate = LocalDate.of(2002, 11, 21),
                basicAddressInfo = DtoAddressInfo(region = Region.SEOUL, area = Area.SEOUL_ALL),
                additionalAddressInfo = DtoAddressInfo(region = Region.GYEONGGI, area = Area.GWANGMYEONGSI),
                preferType = MatchType.HYBRID,
                gender = GenderType.FEMALE
            )
            val member = SignupMapper.toMember(request)
            val result = SignupMapper.toParticipant(member, request)

            then("올바른 ParticipantEntity 객체가 반환되어야 한다") {
                result.member.oauthEmail shouldBe "test@example.com"
                result.basicAddressInfo.region shouldBe Region.SEOUL
                result.basicAddressInfo.area shouldBe Area.SEOUL_ALL
                result.additionalAddressInfo?.region shouldBe Region.GYEONGGI
                result.additionalAddressInfo?.area shouldBe Area.GWANGMYEONGSI
            }
        }
    }
})