package com.dobby.api.dto.request.member

import com.dobby.api.dto.response.member.AddressInfoResponse
import com.dobby.enums.MatchType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateParticipantInfoRequest(
    @Email(message= "연락 받을 이메일이 유효하지 않습니다.")
    @NotBlank(message = "연락 받을 이메일은 공백일 수 없습니다.")
    @Schema(description = "연락 받을 이메일")
    val contactEmail: String,

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Schema(description = "이름")
    val name : String,

    @NotBlank(message = "거주 지역은 공백일 수 없습니다.")
    @Schema(description = "기본 주소 정보")
    val basicAddressInfo: AddressInfoResponse,

    @Schema(description = "추가 주소 정보")
    val additionalAddressInfo: AddressInfoResponse?,

    @Schema(description = "선호 실험 진행 방식")
    val matchType: MatchType?,

    @NotNull(message = "광고성 정보 수신 동의 여부는 필수입니다.")
    @Schema(description = "광고성 정보 이메일/SMS 수신 동의 여부")
    var adConsent: Boolean,

    @NotNull(message = "개인정보 이용 동의는 필수입니다.")
    @Schema(description = "개인정보 수정 및 이용 동의/실험 추천 혜택 동의 여부")
    var matchConsent: Boolean
)
