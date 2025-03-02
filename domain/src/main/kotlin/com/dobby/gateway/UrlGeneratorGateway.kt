package com.dobby.gateway

interface UrlGeneratorGateway {
    fun getBaseUrl(): String
    fun getExperimentPostUrl(postId: String): String
}
