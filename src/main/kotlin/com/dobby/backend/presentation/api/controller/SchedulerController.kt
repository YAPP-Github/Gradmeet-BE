package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.SchedulerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "스케줄러 수동 트리거 API - /v1/scheduler")
@RestController
@RequestMapping("/v1/scheduler")
class SchedulerController(
    private val schedulerService: SchedulerService
) {
    @GetMapping("/email-trigger")
    @Operation(
        summary = "매칭 공고 이메일 전송 수동 트리거 API",
        description = "데모데이용 테스트 API입니다. 실제 비즈니스 로직이 아니기에, 추후 조정될 여지가 있습니다."
    )
    fun triggerSendMatchingEmailJob() {
        schedulerService.triggerSendMatchingEmailJob()
    }

    @PutMapping("/member-consent")
    @Operation(
        summary = "회원 알림 동의 비활성화 API",
        description = "데모데이용 테스트 API입니다."
    )
    fun updateMemberConsent(
        @RequestParam memberId: String
    ) {
        schedulerService.updateMemberConsent(memberId)
    }
}
