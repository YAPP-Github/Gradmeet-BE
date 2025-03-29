package com.dobby.persistence.entity.member

import com.dobby.enums.MatchType
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.member.GenderType
import com.dobby.model.member.Participant
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "participant")
class ParticipantEntity(
    @Id
    @Column(name = "participant_id", columnDefinition = "CHAR(13)")
    val id: String,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "member_id", nullable = false)
    val member: MemberEntity,

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    val gender: GenderType,

    @Column(name = "birth_date", nullable = false)
    val birthDate: LocalDate,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "region", column = Column(name = "basic_region", nullable = false)),
        AttributeOverride(name = "area", column = Column(name = "basic_area", nullable = false))
    )
    val basicAddressInfo: AddressInfo,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "region", column = Column(name = "additional_region", nullable = false)),
        AttributeOverride(name = "area", column = Column(name = "additional_area", nullable = false))
    )
    val additionalAddressInfo: AddressInfo,

    @Column(name = "match_type", nullable = true)
    @Enumerated(EnumType.STRING)
    var matchType: MatchType?
) {

    fun toDomain() = Participant(
        id = id,
        member = member.toDomain(),
        gender = gender,
        birthDate = birthDate,
        basicAddressInfo = basicAddressInfo.toDomain(),
        additionalAddressInfo = additionalAddressInfo.toDomain(),
        matchType = matchType
    )

    companion object {
        fun fromDomain(participant: Participant) = with(participant) {
            ParticipantEntity(
                id = id,
                member = MemberEntity.fromDomain(member),
                gender = gender,
                birthDate = birthDate,
                basicAddressInfo = AddressInfo.fromDomain(basicAddressInfo),
                additionalAddressInfo = AddressInfo.fromDomain(additionalAddressInfo),
                matchType = matchType
            )
        }
    }
}

@Embeddable
data class AddressInfo(
    @Enumerated(EnumType.STRING)
    @Column(name = "region", nullable = false)
    val region: Region,

    @Enumerated(EnumType.STRING)
    @Column(name = "area", nullable = false)
    val area: Area
) {
    fun toDomain() = Participant.AddressInfo(
        region = region,
        area = area
    )

    companion object {
        fun fromDomain(addressInfo: Participant.AddressInfo) = AddressInfo(
            region = addressInfo.region,
            area = addressInfo.area
        )
    }
}
