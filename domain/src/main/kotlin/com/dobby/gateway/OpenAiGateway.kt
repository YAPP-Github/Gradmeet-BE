package com.dobby.gateway

import com.dobby.model.experiment.keyword.ExperimentPostKeywords

interface OpenAiGateway {
    fun extractKeywords(text: String): ExperimentPostKeywords
}
