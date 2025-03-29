package com.dobby.api.dto.response.member

import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.model.member.Participant
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "주소 정보 반환 DTO")
data class AddressInfoResponse(
    @Schema(description = "지역")
    val region: Region,

    @Schema(description = "지역 상세")
    val area: Area
) {
    companion object {
        fun fromDomain(basicAddressInfo: Participant.AddressInfo): AddressInfoResponse {
            return AddressInfoResponse(
                region = basicAddressInfo.region,
                area = basicAddressInfo.area
            )
        }
    }
}
