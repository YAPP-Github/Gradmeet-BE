package com.dobby.backend.presentation.api.dto.response.experiment

import com.dobby.backend.domain.enums.member.GenderType
import com.dobby.backend.domain.enums.MatchType
import com.dobby.backend.domain.enums.experiment.TimeSlot
import com.dobby.backend.domain.enums.areaInfo.Area
import com.dobby.backend.domain.enums.areaInfo.Region
import com.dobby.backend.domain.enums.member.MemberStatus
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@Schema(description = "실험 공고 상세 정보 조회 응답 DTO")
data class ExperimentPostDetailResponse(
    @Schema(description = "실험 공고 ID", example = "01HGXN4H4PXNRH4")
    val experimentPostId: String,

    @Schema(description = "실험 공고 제목", example = "테스트 실험 공고")
    val title: String,

    @Schema(description = "업로드 날짜", example = "2025-01-01")
    val uploadDate: LocalDate,

    @Schema(description = "업로더 이름", example = "야뿌")
    val uploaderName: String,

    @Schema(description = "업로더 활성 상태", example = true.toString())
    val isUploaderActive: Boolean,

    @Schema(description = "조회수", example = "123")
    val views: Int,

    @Schema(description = "모집 상태 여부", example = "true")
    val recruitStatus: Boolean,

    @Schema(description = "요약 정보", example = "실험 공고의 요약(주요) 정보")
    val summary: SummaryResponse,

    @Schema(description = "실험 대상 그룹")
    val targetGroup: TargetGroupResponse,

    @Schema(description = "주소")
    val address: AddressResponse,

    @Schema(description = "내용", example = "실험 게시물 내용입니다.")
    val content: String,

    @Schema(description = "이미지 목록", example = "[\"https://bucket/image1.webp\", \"https://bucket/image2.webp\"]")
    val imageList: List<String>,

    @Schema(description = "글쓴이 여부", example = "true")
    val isAuthor: Boolean,

    @Schema(description = "알림 동의 여부", example = "true")
    val alarmAgree: Boolean
) {
    @Schema(description = "실험 공고 요약 정보")
    data class SummaryResponse(
        @Schema(description = "시작 날짜 (nullable)", example = "2025-02-01", nullable = true)
        val startDate: LocalDate?,

        @Schema(description = "종료 날짜 (nullable)", example = "2025-03-01", nullable = true)
        val endDate: LocalDate?,

        @Schema(description = "책임 연구자", example = "김연구")
        val leadResearcher: String,

        @Schema(description = "매칭 방식", example = "ONLINE")
        val matchType: MatchType,

        @Schema(description = "보상", example = "상품권 5만원")
        val reward: String,

        @Schema(description = "참여 횟수", example = "10")
        val count: Int?,

        @Schema(description = "소요 시간 (nullable)", example = "ABOUT_1H", nullable = true)
        val timeRequired: TimeSlot?
    )

    @Schema(description = "실험 대상 그룹 응답 DTO")
    data class TargetGroupResponse(
        @Schema(description = "최소 연령", example = "20")
        val startAge: Int?,

        @Schema(description = "최대 연령", example = "30")
        val endAge: Int?,

        @Schema(description = "성별 타입", example = "MALE")
        val genderType: GenderType,

        @Schema(description = "기타 조건", example = "흡연자 제외")
        val otherCondition: String?
    )

    @Schema(description = "주소 응답 DTO")
    data class AddressResponse(
        @Schema(description = "장소", example = "건국대학교 1층")
        val place: String?,

        @Schema(description = "지역", example = "SEOUL")
        val region: Region?,

        @Schema(description = "구/군", example = "GWANGJINGU")
        val area: Area?,

        @Schema(description = "상세 주소 (nullable)", example = "1동 101호", nullable = true)
        val detailedAddress: String?
    )
}
