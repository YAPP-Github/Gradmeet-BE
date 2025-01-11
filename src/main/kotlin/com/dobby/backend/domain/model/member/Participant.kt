package com.dobby.backend.domain.model.member

import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import java.time.LocalDate

data class Participant(
    val id: Long,
    val member: Member,
    val gender: GenderType,
    val birthDate: LocalDate,
    val basicAddressInfo: AddressInfo,
    val additionalAddressInfo: AddressInfo?,
    val preferType: MatchType?
) {

    data class AddressInfo(
        val region: Region,
        val area: Area
    )

    companion object {
        fun newParticipant(
            member: Member,
            gender: GenderType,
            birthDate: LocalDate,
            basicAddressInfo: AddressInfo,
            additionalAddressInfo: AddressInfo?,
            preferType: MatchType?
        ) = Participant(
            id = 0,
            member = member,
            gender = gender,
            birthDate = birthDate,
            basicAddressInfo = basicAddressInfo,
            additionalAddressInfo = additionalAddressInfo,
            preferType = preferType
        )
    }
}

