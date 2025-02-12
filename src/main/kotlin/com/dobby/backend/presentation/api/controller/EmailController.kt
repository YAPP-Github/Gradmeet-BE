package com.dobby.backend.presentation.api.controller

import com.dobby.backend.application.service.EmailService
import com.dobby.backend.presentation.api.dto.request.member.EmailSendRequest
import com.dobby.backend.presentation.api.dto.request.member.EmailVerificationRequest
import com.dobby.backend.presentation.api.dto.response.member.EmailSendResponse
import com.dobby.backend.presentation.api.dto.response.member.EmailVerificationResponse
import com.dobby.backend.presentation.api.mapper.EmailMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "이메일 인증 API - /v1/emails")
@RestController
@RequestMapping("/v1/emails")
class EmailController(
    private val emailService: EmailService
) {
    @PostMapping("/send")
    @Operation(
        summary = "학교 메일 코드 전송 API- 연구자 회원가입 과정",
        description = "연구자 회원가입 시, 학교 메일 인증 코드를 전송하는 API입니다."
    )
    suspend fun sendCode(@RequestBody @Valid emailSendRequest: EmailSendRequest)
            : EmailSendResponse {
        val input = EmailMapper.toEmailCodeSendUseCaseInput(emailSendRequest)
        val output = emailService.sendEmail(input)
        return EmailMapper.toEmailSendResponse(output)
    }

    @PostMapping("/verify")
    @Operation(
        summary = "학교 메일 코드 인증 API- 연구자 회원가입 과정",
        description = "연구자 회원가입 시, 코드를 인증하는 API입니다."
    )
    fun verifyCode(@RequestBody @Valid emailVerificationRequest: EmailVerificationRequest)
            : EmailVerificationResponse {
        val input = EmailMapper.toEmailVerificationUseCaseInput(emailVerificationRequest)
        val output = emailService.verifyCode(input)
        return EmailMapper.toEmailVerificationResponse(output)
    }
}
