package com.dobby.backend.infrastructure.database.entity

import AuditingEntity
import com.dobby.backend.infrastructure.database.entity.enum.MemberStatus
import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "member")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role_type")
open class Member (
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

    @Column(name = "birth_date", nullable = true)
    val birthDate : LocalDate?,
) : AuditingEntity()
