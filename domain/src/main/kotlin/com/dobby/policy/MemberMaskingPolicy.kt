package com.dobby.policy

class MemberMaskingPolicy {
    companion object {
        fun maskSensitiveData(id: String): String = "Deleted_$id"
        fun maskName(): String = "ExMember"
    }
}
