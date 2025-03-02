package com.dobby.backend.infrastructure.database.entity.experiment

import com.dobby.model.experiment.ApplyMethod
import jakarta.persistence.*

@Entity(name = "apply_method")
class ApplyMethodEntity (
    @Id
    @Column(name = "apply_method_id", columnDefinition = "CHAR(13)")
    val id: String,

    @Column(name = "phone_num", length = 50)
    var phoneNum: String?,

    @Column(name = "form_url", length = 100)
    var formUrl: String?,

    @Column(name = "content", nullable = false, length = 200)
    var content: String,
) {
    fun toDomain(): ApplyMethod = ApplyMethod(
        id = id,
        phoneNum = phoneNum,
        formUrl = formUrl,
        content = content
    )

    companion object {
        fun fromDomain(applyMethod: ApplyMethod) = with(applyMethod) {
            ApplyMethodEntity(
                id = id,
                phoneNum = phoneNum,
                formUrl = formUrl,
                content = content
            )
        }
    }
}
