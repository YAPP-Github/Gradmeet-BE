package com.dobby.backend.infrastructure.database.entity

import com.dobby.backend.infrastructure.database.entity.enum.ProviderType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import jakarta.persistence.*
import java.time.LocalDate

@Entity(name = "researcher")
@DiscriminatorValue("RESEARCHER")
<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD:src/main/kotlin/com/dobby/backend/infrastructure/database/entity/ResearcherEntity.kt
class ResearcherEntity (
=======
=======
<<<<<<< HEAD:src/main/kotlin/com/dobby/backend/infrastructure/database/entity/Researcher.kt
>>>>>>> 6c4313b (refact: rename entity)
class Researcher (
=======
class ResearcherEntity (
>>>>>>> e59675c (test: fix test due to changed domain)
    @OneToOne
    @JoinColumn(name = "member_id", nullable = false)
    val member: MemberEntity,

<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> dc4d52e ([YS-31] feat: 구글 OAuth 로그인 구현 (#13)):src/main/kotlin/com/dobby/backend/infrastructure/database/entity/Researcher.kt
=======
=======
class ResearcherEntity (
>>>>>>> da69998 (refact: rename entity):src/main/kotlin/com/dobby/backend/infrastructure/database/entity/ResearcherEntity.kt
>>>>>>> 6c4313b (refact: rename entity)
=======
>>>>>>> e59675c (test: fix test due to changed domain)
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
