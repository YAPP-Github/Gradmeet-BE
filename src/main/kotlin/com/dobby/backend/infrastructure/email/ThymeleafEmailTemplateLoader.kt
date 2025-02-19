package com.dobby.backend.infrastructure.email

import com.dobby.backend.domain.EmailTemplateLoader
import com.dobby.backend.infrastructure.config.properties.EmailProperties
import com.dobby.backend.infrastructure.config.properties.TemplateProperties
import org.springframework.stereotype.Component
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Component
class ThymeleafEmailTemplateLoader(
    private val templateEngine: TemplateEngine,
    private val templateProperties: TemplateProperties
) : EmailTemplateLoader {

    override fun loadVerificationTemplate(code: String): String {
        val context = Context().apply {
            setVariable("authCode", code)
        }
        return templateEngine.process(templateProperties.codeTemplate, context)
    }

    override fun loadMatchingTemplate(
        memberName: String,
        experimentPosts: List<Map<String, String>>,
        consentDate: String
    ): String {
        val context = Context().apply {
            setVariable("memberName", memberName)
            setVariable("matchingPosts", experimentPosts)
            setVariable("consentDate", consentDate)
        }
        return templateEngine.process(templateProperties.matchingTemplate, context)
    }
}

