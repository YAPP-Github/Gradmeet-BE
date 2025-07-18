package com.dobby.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "openai")
data class OpenAiProperties(
    var api: Api = Api()
) {
    data class Api(
        var key: String = ""
    )
}
