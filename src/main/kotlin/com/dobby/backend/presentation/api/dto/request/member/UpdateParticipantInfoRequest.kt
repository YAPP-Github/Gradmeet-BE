package com.dobby.backend.presentation.api.dto.request.member

import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.presentation.api.dto.response.member.AddressInfoResponse
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

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
    var basicAddressInfo: AddressInfoResponse,

    @Schema(description = "추가 주소 정보")
    var additionalAddressInfo: AddressInfoResponse?,

    @Schema(description = "선호 실험 진행 방식")
    var matchType: MatchType?,
)
