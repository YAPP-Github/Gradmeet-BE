package com.dobby.backend.infrastructure.database.entity.experiment

import jakarta.persistence.*

@Entity(name = "apply_method")
class ApplyMethodEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long,

    @Column(name = "phone_num", length = 11)
    val phoneNum: Long,

    @Column(name = "form_url")
    val formUrl: String,

    // TODO: content 길이 제한
    @Column(name = "content", nullable = false)
    val content: String,
) {
}
