package com.dobby.backend.domain.enums.member

enum class WithdrawalReasonType(
    val reasonType: String
) {
    RESEARCH_STOPPED("RESEARCH_STOPPED"),
    SECURITY_CONCERN("SECURITY_CONCERN"),
    NO_NECESSARY_FUNCTION("NO_NECESSARY_FUNCTION"),
    TOO_MANY_EMAILS("TOO_MANY_EMAILS"),
    INCONVENIENT_SITE("INCONVENIENT_SITE"),
    OTHER("OTHER")
}
