package com.dobby.backend.domain.gateway

interface S3Gateway {
    fun getExperimentPostPreSignedUrl(fileName: String): String
}
