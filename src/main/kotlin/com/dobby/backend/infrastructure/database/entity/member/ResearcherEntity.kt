package com.dobby.backend.infrastructure.database.entity.member

import jakarta.persistence.*

@Entity
@Table(name = "researcher")
class ResearcherEntity (
    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "member_id", nullable = false)
    val member: MemberEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "researcher_id")
    val id: Long,

    @Column(name = "univ_email", length = 100, nullable = false)
    val univEmail : String,

    @Column(name = "email_verified", nullable = false)
    var emailVerified: Boolean = false,

    @Column(name = "univ_name", length = 100, nullable = false)
    val univName : String,

    @Column(name = "major", length = 10, nullable = false)
    val major : String,

    @Column(name = "lab_info", length = 100, nullable = true)
    val labInfo : String?,
)
