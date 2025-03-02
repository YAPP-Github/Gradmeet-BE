package com.dobby.backend.presentation.api.config.exception

import com.dobby.domain.exception.*
import com.dobby.backend.presentation.api.dto.response.ExceptionResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ExceptionResponseFactory(
    private val exceptionHandler: DobbyExceptionHandler,
) {
    /**
     * DobbyException을 받아 ResponseEntity<ExceptionResponse> 형태로 변환
     * 예외 객체에 정의된 HTTP 상태 코드를 사용
     */
    fun create(exception: DobbyException): ResponseEntity<ExceptionResponse> {
        val httpStatus = getHttpStatus(exception)
        val responseBody = exceptionHandler.convert(exception)
        return ResponseEntity.status(httpStatus).body(responseBody)
    }

    /**
     * 예외 타입에 따라 HttpStatus 매핑
     */
    private fun getHttpStatus(exception: DobbyException): HttpStatus {
        return when (exception) {
            is UnAuthorizedException,
            is AuthenticationTokenNotFoundException,
            is AuthenticationTokenNotValidException,
            is AuthenticationTokenExpiredException -> HttpStatus.UNAUTHORIZED

            is InvalidRequestValueException,
            is EmailDomainNotFoundException,
            is VerifyInfoNotFoundException,
            is CodeNotCorrectException,
            is EmailNotUnivException,
            is CodeExpiredException,
            is EmailFormatInvalidException,
            is EmailAlreadyVerifiedException,
            is TooManyVerificationRequestException,
            is ExperimentAreaOverflowException,
            is ExperimentAreaInCorrectException,
            is ExperimentPostImageSizeException,
            is ExperimentPostRecruitStatusException,
            is ExperimentPostUpdateDateException,
            is ExperimentPostInvalidOnlineRequestException,
            is ExperimentPostTitleException,
            is ExperimentPostRewardException,
            is ExperimentPostContentException,
            is ExperimentPostCountException,
            is ExperimentPostLeadResearcherException -> HttpStatus.BAD_REQUEST

            is PermissionDeniedException -> HttpStatus.FORBIDDEN

            is MemberNotFoundException,
            is ResearcherNotFoundException,
            is ParticipantNotFoundException,
            is MemberConsentNotFoundException,
            is ExperimentPostNotFoundException -> HttpStatus.NOT_FOUND

            is SignupOauthEmailDuplicateException,
            is ContactEmailDuplicateException,
            is SignupUnivEmailDuplicateException -> HttpStatus.CONFLICT

            is ServerException, is UnknownServerErrorException -> HttpStatus.INTERNAL_SERVER_ERROR

            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
    }
}
