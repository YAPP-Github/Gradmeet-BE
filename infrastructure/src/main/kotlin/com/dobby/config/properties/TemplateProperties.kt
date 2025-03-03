package com.dobby.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "file")
class TemplateProperties {
    var codeTemplate: String = ""
    var matchingTemplate: String = ""
}
