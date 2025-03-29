package com.dobby.persistence.entity.member

import com.dobby.enums.member.MemberStatus
import com.dobby.enums.member.ProviderType
import com.dobby.enums.member.RoleType
import com.dobby.model.member.Member
import com.dobby.persistence.entity.experiment.ExperimentPostEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "member")
class MemberEntity(
    @Id
    @Column(name = "member_id", columnDefinition = "CHAR(13)")
    val id: String,

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

    @Column(name = "contact_email", length = 100, nullable = true, unique = true)
    val contactEmail: String?,

    @Column(name = "name", length = 10, nullable = true)
    val name: String,

    @OneToMany(mappedBy = "member", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    val experimentPosts: List<ExperimentPostEntity> = mutableListOf(),

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "deleted_at")
    val deletedAt: LocalDateTime?
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
        updatedAt = updatedAt,
        deletedAt = deletedAt
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
                updatedAt = updatedAt,
                deletedAt = deletedAt
            )
        }
    }
}
