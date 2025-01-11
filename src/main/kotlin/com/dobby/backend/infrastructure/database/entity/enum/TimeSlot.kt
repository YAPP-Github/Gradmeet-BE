package com.dobby.backend.infrastructure.database.entity.enum

enum class TimeSlot(
    val timeSlotName : String
) {
    LESS_30M("30분 미만"),
    ABOUT_30M("약 30분"),
    ABOUT_1H("약 1시간"),
    ABOUT_1H30M("약 1시간 30분"),
    ABOUT_2H("약 2시간"),
    ABOUT_2H30M("약 2시간 30분"),
    ABOUT_3H("약 3시간"),
    ABOUT_3H30M("약 3시간 30분"),
    ABOUT_4H("약 4시간")
}
