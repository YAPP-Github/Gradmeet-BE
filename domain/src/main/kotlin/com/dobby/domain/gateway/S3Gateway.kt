package com.dobby.domain.gateway

interface S3Gateway {
    fun getExperimentPostPreSignedUrl(fileName: String): String
}
