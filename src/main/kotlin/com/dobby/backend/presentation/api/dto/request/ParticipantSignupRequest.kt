package com.dobby.backend.presentation.api.dto.request

import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

data class ParticipantSignupRequest(
    @Email(message = "OAuth 이메일이 유효하지 않습니다.")
    @NotBlank(message = "OAuth 이메일은 공백일 수 없습니다.")
    val oauthEmail: String,

    @NotBlank(message = "OAuth provider은 공백일 수 없습니다.")
    val provider: ProviderType,

    @Email(message= "연락 받을 이메일이 유효하지 않습니다.")
    @NotBlank(message = "연락 받을 이메일은 공백일 수 없습니다.")
    val contactEmail: String,

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    val name : String,

    @NotBlank(message = "성별은 공백일 수 없습니다.")
    val gender: GenderType,

    @NotBlank(message = "생년월일은 공백일 수 없습니다.")
    val birthDate: LocalDate,

    @NotBlank(message = "거주 지역은 공백일 수 없습니다.")
    var basicAddressInfo: AddressInfo,

    // 추가 활동 정보
    var additionalAddressInfo: AddressInfo?,

    // 선호 실험 진행 방식
    var preferType: MatchType,
)

data class AddressInfo(
    @NotBlank(message = "시/도 정보는 공백일 수 없습니다.")
    val region: Region,

    @NotBlank(message = "시/군/구 정보는 공백일 수 없습니다.")
    val area: Area
)