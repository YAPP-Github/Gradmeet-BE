package com.dobby.gateway.experiment

import com.dobby.model.experiment.keyword.ExperimentPostKeyword

interface ExperimentKeywordExtractionGateway {
    fun extractKeywords(text: String): ExperimentPostKeyword
}
