package com.dobby.backend.infrastructure.database.entity

import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.ProviderType
import com.dobby.backend.infrastructure.database.entity.enums.RoleType
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDate

@Entity(name = "participants")
class ParticipantEntity (
    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    val gender: GenderType,

    @Column(name = "basic_region", nullable = false)
    @Enumerated(EnumType.STRING)
    val basicRegion: Region,

    @Column(name = "basic_area", nullable = false)
    @Enumerated(EnumType.STRING)
    val basicArea : Area,

    @Column(name = "optional_region", nullable = true)
    @Enumerated(EnumType.STRING)
    val optionalRegion: Region,

    @Column(name = "optional_area", nullable = true)
    @Enumerated(EnumType.STRING)
    val optionalArea: Area,



    id: Long,
    oauthEmail: String,
    contactEmail : String,
    provider: ProviderType,
    role: RoleType,
    name: String,
    birthDate: LocalDate
) : MemberEntity(id, oauthEmail, provider, role, contactEmail, name, birthDate)

