package com.dobby.model.member

import com.dobby.enums.member.GenderType
import com.dobby.enums.MatchType
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.util.TimeProvider
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
                updatedAt = TimeProvider.currentDateTime()
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
