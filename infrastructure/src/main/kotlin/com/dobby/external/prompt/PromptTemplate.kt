package com.dobby.external.prompt

data class PromptTemplate(
    val description: String,
    val input: Map<String, String>,
    val extract_items: List<String>,
    val output_format: Map<String, Any>,
    val conditions: List<String>
)
