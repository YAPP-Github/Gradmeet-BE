package com.dobby.backend.infrastructure.database.entity.member

import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "participant")
class ParticipantEntity (
    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "member_id", nullable = false)
    val member: MemberEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "participant_id")
    val id: Long,

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    val gender: GenderType,

    @Column(name = "birth_date", nullable = false)
    val birthDate : LocalDate,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "region", column = Column(name = "basic_region", nullable = false)),
        AttributeOverride(name = "area", column = Column(name = "basic_area", nullable = false))
    )
    val basicAddressInfo: AddressInfo,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "region", column = Column(name = "optional_region", nullable = true)),
        AttributeOverride(name = "area", column = Column(name = "optional_area", nullable = true))
    )
    val additionalAddressInfo: AddressInfo?,

    @Column(name = "prefer_type", nullable = true)
    @Enumerated(EnumType.STRING)
    var preferType: MatchType?,
)

@Embeddable
data class AddressInfo(
    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    val region: Region,

    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false)
    val area: Area
)
