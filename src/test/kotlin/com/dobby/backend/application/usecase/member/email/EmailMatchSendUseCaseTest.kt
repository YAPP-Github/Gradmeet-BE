package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.*
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region

import com.dobby.backend.domain.exception.EmailDomainNotFoundException
import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate
import java.time.LocalDateTime

class EmailMatchSendUseCaseTest : BehaviorSpec({

    val emailGateway = mockk<EmailGateway>()
    val emailMatchSendUseCase = EmailMatchSendUseCase(emailGateway)

    given("이메일 매칭 발송을 실행할 때") {

        `when`("유효한 이메일 주소와 야뿌 실험 공고 리스트가 주어지면") {
            val contactEmail = "user@example.com"
            val validMember = Member(
                id = "1",
                role = RoleType.RESEARCHER,
                contactEmail = "christer10@naver.com",
                oauthEmail = "christer10@naver.com",
                name = "신수정",
                provider = ProviderType.NAVER,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val experimentPosts = listOf(
                ExperimentPost(
                    id = "1",
                    member = validMember,
                    targetGroup = TargetGroup(
                        id = "1",
                        startAge = 20,
                        endAge = 30,
                        genderType = GenderType.FEMALE,
                        otherCondition = "야뿌이셨던 여성분"
                    ),
                    applyMethod = ApplyMethod(
                        id = "1",
                        content = "구글폼 보고 참여해주세요 :)",
                        formUrl = "google.form.co.kr",
                        phoneNum = "010-5729-7754"
                    ),
                    images = mutableListOf(),
                    title = "야뿌 최고 먹짱 실험자 모집합니다",
                    content = "YAPP에서 가장 치킨 잘 먹는 사람",
                    views = 10,
                    startDate = LocalDate.of(2025, 2, 3),
                    endDate = LocalDate.of(2025, 2, 10),
                    matchType = MatchType.OFFLINE,
                    count = 35,
                    timeRequired = TimeSlot.LESS_30M,
                    leadResearcher = "야뿌 랩실 서버 25기 신수정",
                    univName = "이화여자대학교",
                    region = Region.SEOUL,
                    area = Area.SEOUL_ALL,
                    detailedAddress = "ECC B123호",
                    reward = "얍 지각면제권 100장",
                    alarmAgree = true,
                    createdAt = LocalDateTime.now().minusHours(23).plusMinutes(59),
                    updatedAt = LocalDateTime.now().minusHours(23).plusMinutes(59),
                    recruitStatus = true
                )
            )
            val input = EmailMatchSendUseCase.Input(contactEmail, experimentPosts, LocalDateTime.now())

            every { emailGateway.sendEmail(any(), any(), any()) } returns Unit

            then("이메일 전송이 성공해야 한다") {
                val output = emailMatchSendUseCase.execute(input)

                output.isSuccess shouldBe true
                verify(exactly = 1) { emailGateway.sendEmail(contactEmail, any(), any()) }
            }
        }

        `when`("유효하지 않은 이메일 도메인이 입력되면") {
            val invalidEmail = "invalid-email@unknown.com"
            val validMember = Member(
                id = "2",
                role = RoleType.RESEARCHER,
                contactEmail = "christer10@naver.com",
                oauthEmail = "christer10@naver.com",
                name = "신수정",
                provider = ProviderType.NAVER,
                status = MemberStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

            val experimentPosts = listOf(
                ExperimentPost(
                    id = "2",
                    member = validMember,
                    targetGroup = TargetGroup(
                        id = "2",
                        startAge = 20,
                        endAge = 30,
                        genderType = GenderType.FEMALE,
                        otherCondition = "야뿌이셨던 여성분"
                    ),
                    applyMethod = ApplyMethod(
                        id = "2",
                        content = "구글폼 보고 참여해주세요 :)",
                        formUrl = "google.form.co.kr",
                        phoneNum = "010-5729-7754"
                    ),
                    images = mutableListOf(),
                    title = "야뿌 최고 먹짱 실험자 모집합니다",
                    content = "YAPP에서 가장 치킨 잘 먹는 사람",
                    views = 10,
                    startDate = LocalDate.of(2025, 2, 3),
                    endDate = LocalDate.of(2025, 2, 10),
                    matchType = MatchType.OFFLINE,
                    count = 35,
                    timeRequired = TimeSlot.LESS_30M,
                    leadResearcher = "야뿌 랩실 서버 25기 신수정",
                    univName = "이화여자대학교",
                    region = Region.SEOUL,
                    area = Area.SEOUL_ALL,
                    detailedAddress = "ECC B123호",
                    reward = "얍 지각면제권 100장",
                    alarmAgree = true,
                    createdAt = LocalDateTime.now().minusHours(23).plusMinutes(59),
                    updatedAt = LocalDateTime.now().minusHours(23).plusMinutes(59),
                    recruitStatus = true
                )
            )
            val input = EmailMatchSendUseCase.Input(invalidEmail, experimentPosts, LocalDateTime.now())

            then("EmailDomainNotFoundException 예외가 발생해야 한다") {
                val exception = shouldThrow<EmailDomainNotFoundException> {
                    emailMatchSendUseCase.execute(input)
                }

                exception shouldBe EmailDomainNotFoundException
            }
        }
    }
})

