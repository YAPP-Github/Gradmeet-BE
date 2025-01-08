package com.dobby.backend.infrastructure.database.entity.member

import AuditingEntity
import com.dobby.backend.domain.model.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import jakarta.persistence.*

@Entity(name = "member")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role_type")
class MemberEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "oauth_email", length = 100, nullable = false, unique = true)
    val oauthEmail: String,

    @Column(name = "oauth_provider", nullable = false)
    @Enumerated(EnumType.STRING)
    val provider: ProviderType,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.HOLD,

    @Column(name = "role", nullable = true)
    @Enumerated(EnumType.STRING)
    val role: RoleType?,

    @Column(name = "contact_email", length = 100, nullable = true)
    val contactEmail: String?,

    @Column(name = "name", length = 10, nullable = true)
    val name: String?,
) : AuditingEntity() {

    fun toDomain() = Member(
        memberId = id,
        name = name,
        oauthEmail = oauthEmail,
        contactEmail = contactEmail,
        provider = provider,
        status = status,
        role = role
    )

    companion object {
        fun fromDomain(member: Member) = with(member) {
            MemberEntity(
                id = memberId,
                oauthEmail = oauthEmail,
                provider = provider,
                status = status,
                role = role,
                contactEmail = contactEmail,
                name = name
            )
        }
    }
}
