package com.dobby.backend.domain.model

data class Member(
    val memberId: Long,
    val name: String,
    val email: String,
) {

    companion object {
        fun newMember(
            memberId: Long,
            name: String,
            email: String,
        ) = Member(
            memberId = memberId,
            name = name,
            email = email,
        )
    }
}
