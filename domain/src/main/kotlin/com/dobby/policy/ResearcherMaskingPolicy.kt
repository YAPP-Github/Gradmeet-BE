package com.dobby.policy

class ResearcherMaskingPolicy {
    companion object {
        fun maskSensitiveData(id: String): String = "Deleted_${id}"
        fun maskMajor(): String = "ExMajor"
    }
}
