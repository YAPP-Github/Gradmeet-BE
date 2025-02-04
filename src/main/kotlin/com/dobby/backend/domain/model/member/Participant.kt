package com.dobby.backend.domain.model.member

import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import java.time.LocalDate
import java.time.LocalDateTime

data class Participant(
    val id: String,
    val member: Member,
    val gender: GenderType,
    val birthDate: LocalDate,
    val basicAddressInfo: AddressInfo,
    val additionalAddressInfo: AddressInfo,
    val matchType: MatchType?
) {

    data class AddressInfo(
        val region: Region,
        val area: Area
    )

    companion object {
        fun newParticipant(
            id: String,
            member: Member,
            gender: GenderType,
            birthDate: LocalDate,
            basicAddressInfo: AddressInfo,
            additionalAddressInfo: AddressInfo,
            matchType: MatchType?
        ) = Participant(
            id = id,
            member = member,
            gender = gender,
            birthDate = birthDate,
            basicAddressInfo = basicAddressInfo,
            additionalAddressInfo = additionalAddressInfo,
            matchType = matchType
        )
    }

    fun updateInfo(
        contactEmail: String,
        name: String,
        basicAddressInfo: AddressInfo,
        additionalAddressInfo: AddressInfo?,
        matchType: MatchType?
    ): Participant {
        return this.copy(
            member = member.copy(
                contactEmail = contactEmail,
                name = name,
                updatedAt = LocalDateTime.now()
            ),
            basicAddressInfo = basicAddressInfo,
            additionalAddressInfo = additionalAddressInfo?.copy() ?: AddressInfo(
                region = Region.NONE,
                area = Area.NONE
            ),
            matchType = matchType
        )
    }
}
