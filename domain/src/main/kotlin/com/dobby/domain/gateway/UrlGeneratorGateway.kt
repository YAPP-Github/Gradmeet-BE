package com.dobby.domain.gateway

interface UrlGeneratorGateway {
    fun getBaseUrl(): String
    fun getExperimentPostUrl(postId: String): String
}
