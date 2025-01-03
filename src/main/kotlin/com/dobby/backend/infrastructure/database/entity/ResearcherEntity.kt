package com.dobby.backend.infrastructure.database.entity

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "researcher")
@DiscriminatorValue("RESEARCHER")
<<<<<<< HEAD:src/main/kotlin/com/dobby/backend/infrastructure/database/entity/ResearcherEntity.kt
class ResearcherEntity (
=======
class Researcher (
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

>>>>>>> dc4d52e ([YS-31] feat: 구글 OAuth 로그인 구현 (#13)):src/main/kotlin/com/dobby/backend/infrastructure/database/entity/Researcher.kt
    @Column(name = "univ_email", length = 100, nullable = false)
    val univEmail : String,

    @Column(name = "email_verified", nullable = false)
    var emailVerified: Boolean = false,

    @Column(name = "univ_name", length = 100, nullable = false)
    val univName : String,

    @Column(name = "major", length = 10, nullable = false)
    val major : String,

    @Column(name = "lab_info", length = 100, nullable = true)
    val labInfo : String,

    id: Long,
    oauthEmail: String,
    provider: ProviderType,
    contactEmail: String,
    name: String,
    birthDate: LocalDate
) : MemberEntity(
    id= id,
    oauthEmail = oauthEmail,
    provider = provider,
    contactEmail= contactEmail,
    role = RoleType.RESEARCHER,
    name = name,
    birthDate = birthDate
)
