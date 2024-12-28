package com.dobby.backend.infrastructure.database.entity

import com.dobby.backend.infrastructure.database.entity.common.AuditingEntity
import com.dobby.backend.infrastructure.database.entity.enums.RoleType
import com.dobby.backend.infrastructure.database.entity.enums.ProviderType
import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "members")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class MemberEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "oauth_email", length = 100, nullable = false, unique = true)
    val oauthEmail : String,

    @Column(name = "oauth_provider", nullable = false)
    @Enumerated(EnumType.STRING)
    val provider : ProviderType,

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    val role: RoleType,

    @Column(name = "contact_email", length = 100, nullable = false)
    val contactEmail : String,

    @Column(name = "name", length = 10, nullable = false)
    val name : String,

    @Column(name = "birth_date", nullable = false)
    val birthDate : LocalDate
) : AuditingEntity()
