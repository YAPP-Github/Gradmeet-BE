package com.dobby.backend.domain.gateway

interface UrlGeneratorGateway {
    fun getBaseUrl(): String
    fun getExperimentPostUrl(postId: String): String
}
