package com.dobby.backend.application.usecase.member.email

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.EmailTemplateLoader
import com.dobby.backend.domain.exception.ContactEmailDuplicateException
import com.dobby.backend.domain.exception.EmailDomainNotFoundException
import com.dobby.backend.domain.gateway.UrlGeneratorGateway
import com.dobby.backend.domain.gateway.email.EmailGateway
import com.dobby.backend.domain.gateway.member.MemberConsentGateway
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.util.EmailUtils
import com.dobby.backend.util.RetryUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SendMatchingEmailUseCase(
    private val emailGateway: EmailGateway,
    private val urlGeneratorGateway: UrlGeneratorGateway,
    private val memberGateway: MemberGateway,
    private val memberConsentGateway: MemberConsentGateway,
    private val emailTemplateLoader: EmailTemplateLoader
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
        val member = memberGateway.findByContactEmail(input.contactEmail)
            ?: throw ContactEmailDuplicateException

        val consentDate = memberConsentGateway.findByMemberId(member.id)!!.matchConsentedAt!!
            .format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))

        val title = getEmailTitle(member.name)
        val matchingPostList = formatPostList(input.experimentPosts)
        val content = emailTemplateLoader.loadMatchingTemplate(member.name, matchingPostList, consentDate)

        return try {
            RetryUtils.retryWithBackOff {
                emailGateway.sendEmail(input.contactEmail, title, content, true)
            }
            Output(isSuccess = true, message = " Email successfully sent to ${input.contactEmail}")
        } catch (ex: Exception) {
            Output(isSuccess = false, message = "Failed to send to email to ${input.contactEmail}: ${ex.message}")
        }
    }

    private fun validateEmail(email : String){
        if(!EmailUtils.isDomainExists(email)) throw EmailDomainNotFoundException
    }

    private fun getEmailTitle(memberName: String): String {
        return "[그라밋] $memberName 님에게 딱 맞는 오늘의 실험 공고! 참여하면 보상이 기다려요 💰"
    }

    private fun formatPostList(experimentPosts: List<ExperimentPost>): List<Map<String, String>> {
        val groupedPosts = experimentPosts.groupBy { it.place ?: "비대면" }

        return groupedPosts.flatMap { (place, posts) ->
            val sortedPosts = posts.sortedBy { it.createdAt }
            sortedPosts.mapIndexed { index, post ->
                val postUrl = urlGeneratorGateway.getExperimentPostUrl(post.id)
                mapOf(
                    "title" to post.title,
                    "place" to if (index == 0) place else "",
                    "uploadDate" to LocalDate.now().toString(),
                    "reward" to post.reward,
                    "postUrl" to postUrl
                )
            }
        }
    }

}
