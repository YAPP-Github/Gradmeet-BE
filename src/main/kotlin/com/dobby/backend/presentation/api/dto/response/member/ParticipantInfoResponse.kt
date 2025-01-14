package com.dobby.backend.presentation.api.dto.response.member

import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "공고 등록 시, 자동 입력에 필요한 연구자 정보 반환 DTO")
data class ParticipantInfoResponse(
    @Schema(description = "사용자 기본 정보")
    val memberInfo: MemberResponse,

    @Schema(description = "성별")
    val gender: GenderType,

    @Schema(description = "생년월일")
    val birthDate: LocalDate,

    @Schema(description = "기본 주소 정보")
    val basicAddressInfo: AddressInfoResponse,

    @Schema(description = "추가 주소 정보")
    val additionalAddressInfo: AddressInfoResponse?,

    @Schema(description = "매칭 선호 타입")
    val matchType: MatchType?,
)
