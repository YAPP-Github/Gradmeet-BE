package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.EmailDomainNotFoundException
import com.dobby.backend.domain.gateway.UrlGeneratorGateway
import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.util.EmailUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SendMatchingEmailUseCase(
    private val emailGateway: EmailGateway,
    private val urlGeneratorGateway: UrlGeneratorGateway
): UseCase<SendMatchingEmailUseCase.Input, SendMatchingEmailUseCase.Output>{

    data class Input(
        val contactEmail: String,
        val experimentPosts: List<ExperimentPost>,
        val currentDateTime: LocalDateTime
    )

    data class Output(
        val isSuccess: Boolean,
        val message: String
    )

    override fun execute(input: Input): Output {
        validateEmail(input.contactEmail)

        val (title, content) = getFormattedEmail(input.contactEmail, input.experimentPosts)

        return try {
            emailGateway.sendEmail(input.contactEmail, title, content)
            Output(isSuccess = true, message = " Email successfully sent to ${input.contactEmail}")
        } catch (ex: Exception) {
            Output(isSuccess = false, message = "Failed to send to email to ${input.contactEmail}: ${ex.message}")
        }
    }

    private fun validateEmail(email : String){
        if(!EmailUtils.isDomainExists(email)) throw EmailDomainNotFoundException
    }

    private fun getFormattedEmail(memberName: String, experimentPosts: List<ExperimentPost>): Pair<String, String> {
        val todayDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val emailTitle = "[그라밋🔬] $todayDate 오늘의 추천 실험 공고를 확인해보세요!"
        val jobListFormatted = experimentPosts.joinToString("\n\n") { post ->
            val postUrl = urlGeneratorGateway.getExperimentPostUrl(postId = post.id)
            """
        🔹 **${post.title}**
        -  기간: ${post.startDate} ~ ${post.endDate}
        -  위치: ${post.univName ?: "공고참고"} 
        -  보상: ${post.reward}
        -  [공고 확인하기]($postUrl)
        """.trimIndent()
        }

        val content = """
        ${memberName}님과 꼭 맞는 실험 공고를 찾아왔어요 🧚
        * 자세한 실험 내용이나 모집 대상은 공고 내용을 확인해 주세요.

        🔹 **추천 공고 목록** 🔹
        $jobListFormatted

        더 많은 공고를 보려면 [그라밋 웹사이트](${urlGeneratorGateway.getBaseUrl()})를 방문해 주세요!
    """.trimIndent()

        return Pair(emailTitle, content)
    }
}
