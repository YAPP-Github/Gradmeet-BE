package com.dobby.backend.domain.model

import com.dobby.backend.util.generateULID

data class Member(
    val memberId: String,
    val name: String,
    val email: String,
) {

    companion object {
        fun newMember(
            name: String,
            email: String,
        ) = Member(
            memberId = generateULID(),
            name = name,
            email = email,
        )
    }
}
