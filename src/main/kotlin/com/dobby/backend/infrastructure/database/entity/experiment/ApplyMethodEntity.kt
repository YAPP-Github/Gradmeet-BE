package com.dobby.backend.infrastructure.database.entity.experiment

import jakarta.persistence.*

@Entity(name = "apply_method")
class ApplyMethodEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "phone_num", length = 50)
    val phoneNum: String,

    @Column(name = "form_url", length = 100)
    val formUrl: String,

    @Column(name = "content", nullable = false, length = 200)
    val content: String,
) {
}
