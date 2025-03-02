package com.dobby.gateway

interface S3Gateway {
    fun getExperimentPostPreSignedUrl(fileName: String): String
}
