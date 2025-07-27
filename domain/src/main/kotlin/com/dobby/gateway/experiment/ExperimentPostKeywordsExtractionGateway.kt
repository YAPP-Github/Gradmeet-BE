package com.dobby.gateway.experiment

import com.dobby.model.experiment.keyword.ExperimentPostKeywords

interface ExperimentPostKeywordsExtractionGateway {
    fun extractKeywords(text: String): ExperimentPostKeywords
}
