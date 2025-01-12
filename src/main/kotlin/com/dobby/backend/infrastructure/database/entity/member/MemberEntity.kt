package com.dobby.backend.infrastructure.database.entity.member

import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.entity.experiment.ExperimentPostEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "member")
class MemberEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long = 0L,

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
    val name: String,

    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    val experimentPosts: List<ExperimentPostEntity> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime
) {

    fun toDomain() = Member(
        id = id,
        name = name,
        oauthEmail = oauthEmail,
        contactEmail = contactEmail,
        provider = provider,
        status = status,
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(member: Member) = with(member) {
            MemberEntity(
                id = id,
                oauthEmail = oauthEmail,
                provider = provider,
                status = status,
                role = role,
                contactEmail = contactEmail,
                name = name,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
