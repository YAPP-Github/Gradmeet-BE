package com.dobby.backend.presentation.api.dto.request.member

import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.member.ProviderType
import com.dobby.backend.presentation.api.dto.response.member.AddressInfoResponse
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class ParticipantSignupRequest(
    @Email(message = "OAuth 이메일이 유효하지 않습니다.")
    @NotBlank(message = "OAuth 이메일은 공백일 수 없습니다.")
    @Schema(description = "OAuth 이메일")
    val oauthEmail: String,

    @NotBlank(message = "OAuth provider은 공백일 수 없습니다.")
    @Schema(description = "OAuth provider")
    val provider: ProviderType,

    @Email(message= "연락 받을 이메일이 유효하지 않습니다.")
    @NotBlank(message = "연락 받을 이메일은 공백일 수 없습니다.")
    @Schema(description = "연락 받을 이메일")
    val contactEmail: String,

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Schema(description = "이름")
    val name : String,

    @NotBlank(message = "성별은 공백일 수 없습니다.")
    @Schema(description = "성별")
    val gender: GenderType,

    @Past @NotNull(message = "생년월일은 공백일 수 없습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "생년월일")
    val birthDate: LocalDate,

    @NotBlank(message = "거주 지역은 공백일 수 없습니다.")
    @Schema(description = "기본 주소 정보")
    var basicAddressInfo: AddressInfoResponse,

    @Schema(description = "추가 주소 정보")
    var additionalAddressInfo: AddressInfoResponse?,

    @Schema(description = "선호 실험 진행 방식")
    var matchType: MatchType?,
)
