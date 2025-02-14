package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.experiment.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import java.time.LocalDate

class UpdateExperimentPostUseCase (
    private val experimentPostGateway: ExperimentPostGateway,
    private val idGenerator: IdGenerator
) : UseCase<UpdateExperimentPostUseCase.Input, UpdateExperimentPostUseCase.Output> {
    data class Input(
        val experimentPostId: String,
        val memberId: String,
        val targetGroupInfo: TargetGroupInfo?,
        val applyMethodInfo: ApplyMethodInfo?,
        val imageListInfo: ImageListInfo?,

        val startDate: LocalDate?,
        val endDate: LocalDate?,
        val matchType: MatchType?,
        val count: Int?,
        val timeRequired: TimeSlot?,

        val leadResearcher: String?,
        val place: String?,
        val region: Region?,
        val area: Area?,
        val detailedAddress : String?,

        val reward: String?,
        val title: String?,
        val content: String?
    )

    data class TargetGroupInfo(
        val startAge: Int?,
        val endAge: Int?,
        val genderType: GenderType,
        val otherCondition: String?
    )

    data class ApplyMethodInfo(
        val content: String,
        val formUrl: String?,
        val phoneNum: String?
    )

    data class ImageListInfo(
        val images: List<String> = mutableListOf()
    )

    data class Output(
        val postInfo: PostInfo
    )

    data class PostInfo(
        val postId: String,
        val title: String,
        val views: Int,
        val place: String?,
        val reward: String?,
        val durationInfo: DurationInfo?,
    )

    data class DurationInfo(
        val startDate: LocalDate?,
        val endDate: LocalDate?
    )

    override fun execute(input: Input): Output {
        val existingPost = validate(input)

        val targetGroup = existingPost.targetGroup.update(
            startAge = input.targetGroupInfo?.startAge,
            endAge = input.targetGroupInfo?.endAge,
            genderType = input.targetGroupInfo?.genderType,
            otherCondition = input.targetGroupInfo?.otherCondition
        )

        val applyMethod = existingPost.applyMethod.update(
            phoneNum = input.applyMethodInfo?.phoneNum,
            formUrl = input.applyMethodInfo?.formUrl,
            content = input.applyMethodInfo?.content
        )

        val experimentPost = existingPost.update(
            applyMethod = applyMethod,
            targetGroup = targetGroup,
            title = input.title,
            reward = input.reward,
            startDate = input.startDate,
            endDate = input.endDate,
            content = input.content,
            count = input.count,
            leadResearcher = input.leadResearcher,
            detailedAddress = input.detailedAddress,
            matchType = input.matchType,
            place = input.place,
            region = input.region,
            area = input.area,
            imageListInfo = input.imageListInfo?.images,
            idGenerator = idGenerator
        )
        val updatedPost = experimentPostGateway.save(experimentPost)

        return Output(
            postInfo = PostInfo(
                postId = updatedPost.id,
                title = updatedPost.title,
                views = updatedPost.views,
                place = updatedPost.place,
                reward = updatedPost.reward,
                durationInfo = DurationInfo(
                    startDate = updatedPost.startDate,
                    endDate = updatedPost.endDate
                )
            )
        )
    }

    private fun validate(input: Input): ExperimentPost {
        val existingPost = validateExistingPost(input)
        validatePermission(existingPost, input)
        validateNotExpired(existingPost)
        validateImageCount(input)
        return existingPost
    }

    private fun validateExistingPost(input: Input): ExperimentPost {
        return experimentPostGateway.findExperimentPostByMemberIdAndPostId(input.memberId, input.experimentPostId)
            ?: throw ExperimentPostNotFoundException
    }

    private fun validatePermission(existingPost: ExperimentPost, input: Input) {
        if(existingPost.member.id != input.memberId) throw PermissionDeniedException
    }

    private fun validateNotExpired(existingPost: ExperimentPost){
        if (!existingPost.recruitStatus) throw ExperimentPostUpdateDateException
    }

    private fun validateImageCount(input: Input) {
        input.imageListInfo?.let {
            if(it.images.size > 3) {
                throw ExperimentPostImageSizeException
            }
        }
    }
}
