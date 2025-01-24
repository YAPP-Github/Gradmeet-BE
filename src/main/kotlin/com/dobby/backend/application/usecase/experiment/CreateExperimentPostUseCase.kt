package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.ErrorCode
import com.dobby.backend.domain.exception.ExperimentPostException
import com.dobby.backend.domain.exception.PermissionDeniedException
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentImage
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.RoleType
import com.dobby.backend.infrastructure.database.entity.enums.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import java.time.LocalDate
import java.time.LocalDateTime

class CreateExperimentPostUseCase(
    private val experimentPostGateway: ExperimentPostGateway,
    private val memberGateway: MemberGateway
) : UseCase<CreateExperimentPostUseCase.Input, CreateExperimentPostUseCase.Output> {

    data class Input(
        val memberId: Long,
        val targetGroupInfo: TargetGroupInfo,
        val applyMethodInfo: ApplyMethodInfo,
        val imageListInfo: ImageListInfo,

        val startDate: LocalDate?,
        val endDate: LocalDate?,
        val matchType: MatchType,
        val count: Int, // N 회 참여
        val timeRequired: TimeSlot?,

        val leadResearcher: String,
        val univName: String?,
        val region: Region?,
        val area: Area?,
        val detailedAddress: String?,

        val reward: String,
        val title: String,
        val content: String,
        val alarmAgree: Boolean
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
        val member = memberGateway.getById(input.memberId)
        validate(input, member)

        val targetGroup = createTargetGroup(input.targetGroupInfo)
        val applyMethod = createApplyMethod(input.applyMethodInfo)
        var experimentPost = createExperimentPost(
            member,
            input,
            targetGroup,
            applyMethod
        )

        val experimentImages = input.imageListInfo.images.map { imageUrl ->
            ExperimentImage(
                id = 0L,
                experimentPost = null,
                imageUrl = imageUrl
            )
        }

        experimentPost.updateImages(experimentImages)
        val savedExperimentPost = experimentPostGateway.save(experimentPost)

        return Output(
            PostInfo(
                postId = savedExperimentPost.id,
                title = savedExperimentPost.title,
                views = savedExperimentPost.views,
                univName = savedExperimentPost.univName,
                durationInfo = DurationInfo(
                    startDate = savedExperimentPost.startDate,
                    endDate = savedExperimentPost.endDate
                ),
                reward = savedExperimentPost.reward
            )
        )
    }

    private fun createTargetGroup(targetGroupInfo: TargetGroupInfo): TargetGroup {
        return TargetGroup(
            id = 0L,
            startAge = targetGroupInfo.startAge,
            endAge = targetGroupInfo.endAge,
            genderType = targetGroupInfo.genderType,
            otherCondition = targetGroupInfo.otherCondition
        )
    }

    private fun createApplyMethod(applyMethodInfo: ApplyMethodInfo): ApplyMethod {
        return ApplyMethod(
            id = 0L,
            phoneNum = applyMethodInfo.phoneNum,
            formUrl = applyMethodInfo.formUrl,
            content = applyMethodInfo.content
        )
    }

    private fun createExperimentPost(
        member: Member,
        input: Input,
        targetGroup: TargetGroup,
        applyMethod: ApplyMethod,
    ): ExperimentPost {
        return  ExperimentPost(
            member = member,
            leadResearcher = member.name,
            id = 0L,
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            views = 0,
            title = input.title,
            content = input.content,
            reward = input.reward,
            startDate = input.startDate,
            endDate = input.endDate,
            timeRequired = input.timeRequired,
            count = input.count,
            matchType = input.matchType,
            univName = input.univName,
            region = input.region,
            area = input.area,
            detailedAddress = input.detailedAddress,
            alarmAgree = input.alarmAgree,
            recruitStatus = true,
            images = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
        )
    }

    private fun validate(input: Input, member: Member){
        validateMemberRole(member)
        validateImageListSize(input.imageListInfo)
        validateOnlineMatchType(input)
    }

    private fun validateMemberRole(member :Member){
        if(member.role != RoleType.RESEARCHER)
            throw PermissionDeniedException()
    }

    private fun validateImageListSize(imageListInfo: ImageListInfo){
        if(imageListInfo.images.size > 3)
            throw ExperimentPostException(ErrorCode.EXPERIMENT_POST_IMAGE_SIZE_LIMIT);
    }

    private fun validateOnlineMatchType(input: Input){
        if(input.matchType == MatchType.ONLINE){
            if(input.univName != null || input.region != null || input.area != null) {
                throw ExperimentPostException(ErrorCode.EXPERIMENT_POST_INVALID_ONLINE_REQUEST)
            }
        }
    }
}
