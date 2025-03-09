package com.dobby.usecase.experiment

import com.dobby.usecase.UseCase
import com.dobby.IdGenerator
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.model.experiment.ExperimentPost
import com.dobby.enums.member.GenderType
import com.dobby.enums.MatchType
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.exception.ExperimentPostNotFoundException
import com.dobby.exception.ExperimentPostRecruitStatusException
import com.dobby.exception.ExperimentPostUpdateDateException
import com.dobby.exception.PermissionDeniedException
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
        val recruitStatus: Boolean?,

        val reward: String?,
        val title: String?,
        val content: String?
    )

    data class TargetGroupInfo(
        val startAge: Int?,
        val endAge: Int?,
        val genderType: GenderType?,
        val otherCondition: String?
    )

    data class ApplyMethodInfo(
        val content: String?,
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
            timeRequired = input.timeRequired,
            imageListInfo = input.imageListInfo?.images,
            recruitStatus = input.recruitStatus,
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
        validateModificationPolicy(existingPost, input)
        return existingPost
    }

    private fun validateExistingPost(input: Input): ExperimentPost {
        return experimentPostGateway.findExperimentPostByMemberIdAndPostId(input.memberId, input.experimentPostId)
            ?: throw ExperimentPostNotFoundException
    }

    private fun validatePermission(existingPost: ExperimentPost, input: Input) {
        if(existingPost.member.id != input.memberId) throw PermissionDeniedException
    }

    private fun validateModificationPolicy(existingPost: ExperimentPost, input: Input){
        val today = LocalDate.now()
        if(!existingPost.recruitStatus || existingPost.endDate?.isBefore(today) == true){
            if(input.startDate != existingPost.startDate || input.endDate != existingPost.endDate) {
                throw ExperimentPostUpdateDateException
            }
            if(input.recruitStatus != null) {
                throw ExperimentPostRecruitStatusException
            }
        }
    }
}
