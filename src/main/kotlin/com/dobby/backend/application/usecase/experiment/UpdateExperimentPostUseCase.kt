package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.mapper.ExperimentMapper
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import java.time.LocalDate

class UpdateExperimentPostUseCase (
    private val experimentPostGateway: ExperimentPostGateway
) : UseCase<UpdateExperimentPostUseCase.Input, UpdateExperimentPostUseCase.Output> {
    data class Input(
        val experimentPostId: Long,
        val memberId: Long,
        val targetGroupInfo: TargetGroupInfo?,
        val applyMethodInfo: ApplyMethodInfo?,
        val imageListInfo: ImageListInfo?,

        val startDate: LocalDate?,
        val endDate: LocalDate?,
        val matchType: MatchType?,
        val count: Int?,
        val timeRequired: TimeSlot?,

        val leadResearcher: String?,
        val univName: String?,
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
        val postId: Long,
        val title: String,
        val views: Int,
        val univName: String?,
        val reward: String?,
        val durationInfo: DurationInfo?,
    )

    data class DurationInfo(
        val startDate: LocalDate?,
        val endDate: LocalDate?
    )

    override fun execute(input: Input): Output {
        val existingPost = validate(input)
        val experimentPost = ExperimentMapper.toDomain(input, existingPost)
        val updatedPost = experimentPostGateway.updateExperimentPost(experimentPost)

        return Output(
            postInfo = PostInfo(
                postId = updatedPost.id,
                title = updatedPost.title,
                views = updatedPost.views,
                univName = updatedPost.univName,
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
