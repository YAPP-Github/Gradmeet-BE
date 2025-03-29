package com.dobby.api.config.exception

import com.dobby.api.dto.response.ExceptionResponse
import com.dobby.exception.AuthenticationTokenExpiredException
import com.dobby.exception.AuthenticationTokenNotFoundException
import com.dobby.exception.AuthenticationTokenNotValidException
import com.dobby.exception.ClientException
import com.dobby.exception.ContactEmailDuplicateException
import com.dobby.exception.DobbyException
import com.dobby.exception.ExperimentPostNotFoundException
import com.dobby.exception.MemberConsentNotFoundException
import com.dobby.exception.MemberNotFoundException
import com.dobby.exception.ParticipantNotFoundException
import com.dobby.exception.PermissionDeniedException
import com.dobby.exception.ResearcherNotFoundException
import com.dobby.exception.ServerException
import com.dobby.exception.SignupOauthEmailDuplicateException
import com.dobby.exception.SignupUnivEmailDuplicateException
import com.dobby.exception.UnAuthorizedException
import com.dobby.exception.UnknownServerErrorException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ExceptionResponseFactory(
    private val exceptionHandler: DobbyExceptionHandler
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

            is ClientException -> HttpStatus.BAD_REQUEST

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
