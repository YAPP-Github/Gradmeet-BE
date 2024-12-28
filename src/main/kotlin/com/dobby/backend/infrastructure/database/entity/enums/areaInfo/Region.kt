package com.dobby.backend.infrastructure.database.entity.enums.areaInfo

enum class Region(val displayName: String) {
    SEOUL("서울"),
    GYEONGGI("경기"),
    INCHEON("인천"),
    GANGWON("강원"),
    DAEJEON("대전"),
    SEJONG("세종"),
    CHUNGNAM("충남"),
    CHUNGBUK("충북"),
    BUSAN("부산"),
    ULSAN("울산"),
    GYEONGNAM("경남"),
    GYEONGBUK("경북"),
    DAEGU("대구"),
    GWANGJU("광주"),
    JEONNAM("전남"),
    JEONBUK("전북"),
    JEJU("제주");

    companion object {
        fun fromDisplayName(name : String): Region? {
            return entries.find { it.displayName == name }
        }
    }
}