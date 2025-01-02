package com.dobby.backend.infrastructure.database.entity

import AuditingEntity
import com.dobby.backend.domain.model.Member
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "member")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role_type")
class MemberEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "oauth_email", length = 100, nullable = false, unique = true)
    val oauthEmail : String,

    @Column(name = "oauth_provider", nullable = false)
    @Enumerated(EnumType.STRING)
    val provider : ProviderType,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MemberStatus = MemberStatus.HOLD,

    @Column(name = "role", nullable = true)
    @Enumerated(EnumType.STRING)
    val role: RoleType?,

    @Column(name = "contact_email", length = 100, nullable = true)
    val contactEmail : String?,

    @Column(name = "name", length = 10, nullable = true)
    val name : String?,

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD:src/main/kotlin/com/dobby/backend/infrastructure/database/entity/MemberEntity.kt
    @Column(name = "birth_date", nullable = false)
    val birthDate : LocalDate,
) : AuditingEntity() {
    fun toDomain() = Member(
        memberId = id,
        name = name,
        oauthEmail = oauthEmail,
        contactEmail = contactEmail,
        provider = provider,
        status = status,
        role = role,
        birthDate = birthDate
    )

    companion object {
        fun fromDomain(member: Member) = with(member) {
            MemberEntity(
                id = memberId,
                name = name,
                oauthEmail = oauthEmail,
                contactEmail = contactEmail,
                provider = provider,
                status = status,
                role = role,
                birthDate = birthDate
            )
        }
    }
}
=======
    @Column(name = "birth_date", nullable = true)
    val birthDate : LocalDate?,
) : AuditingEntity()
>>>>>>> dc4d52e ([YS-31] feat: 구글 OAuth 로그인 구현 (#13)):src/main/kotlin/com/dobby/backend/infrastructure/database/entity/Member.kt
=======
<<<<<<< HEAD:src/main/kotlin/com/dobby/backend/infrastructure/database/entity/Member.kt
    @Column(name = "birth_date", nullable = true)
    val birthDate : LocalDate?,
) : AuditingEntity()
=======
=======
>>>>>>> e59675c (test: fix test due to changed domain)
    @Column(name = "birth_date", nullable = false)
    val birthDate : LocalDate,
) : AuditingEntity() {

    fun toDomain() = Member(
        memberId = id,
        name = name,
        oauthEmail = oauthEmail,
        contactEmail = contactEmail,
        provider = provider,
        status = status,
        role = role,
        birthDate = birthDate
    )

    companion object {
        fun fromDomain(member: Member) = with(member) {
            MemberEntity(
                id = memberId,
                name = name,
                oauthEmail = oauthEmail,
                contactEmail = contactEmail,
                provider = provider,
                status = status,
                role = role,
                birthDate = birthDate
            )
        }
    }
}
<<<<<<< HEAD
>>>>>>> da69998 (refact: rename entity):src/main/kotlin/com/dobby/backend/infrastructure/database/entity/MemberEntity.kt
>>>>>>> 6c4313b (refact: rename entity)
=======
>>>>>>> e59675c (test: fix test due to changed domain)
