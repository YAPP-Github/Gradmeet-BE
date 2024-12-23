package com.dobby.backend.presentation.api.dto.apiPayload

import com.dobby.backend.presentation.api.dto.apiPayload.code.status.SuccessStatus
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("isSuccess", "code", "message", "result")
data class ApiResponse<T>(
    @JsonProperty("isSuccess") val isSuccess : Boolean,
    val code: String,
    val message: String,
    @JsonInclude(JsonInclude.Include.NON_NULL) val result: T? = null
) {
    companion object {
        // 성공한 경우의 응답
        fun <T> onSuccess(result : T): ApiResponse<T> {
            return ApiResponse(true, SuccessStatus.OK.code, SuccessStatus.OK.message, result)
        }
        // 실패한 경우의 응답
        fun <T> onFailure(code : String, message : String, data:T? = null): ApiResponse<T> {
            return ApiResponse(false, code, message, data)
        }
    }
}
