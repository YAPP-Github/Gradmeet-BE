package com.dobby.backend.infrastructure.database.entity.enum

enum class TimeSlot(
    val timeSlotName : String
) {
    LESS_30M("LESS_30M"),
    ABOUT_30M("ABOUT_30M"),
    ABOUT_1H("ABOUT_1H"),
    ABOUT_1H30M("ABOUT_1H30M"),
    ABOUT_2H("ABOUT_2H"),
    ABOUT_2H30M("ABOUT_2H30M"),
    ABOUT_3H("ABOUT_3H"),
    ABOUT_3H30M("ABOUT_3H30M"),
    ABOUT_4H("ABOUT_4H")
}
