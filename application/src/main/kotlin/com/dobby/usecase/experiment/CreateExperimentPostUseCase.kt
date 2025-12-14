package com.dobby.usecase.experiment

import com.dobby.enums.MatchType
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.areaInfo.Region
import com.dobby.enums.experiment.TimeSlot
import com.dobby.enums.member.GenderType
import com.dobby.enums.member.RoleType
import com.dobby.exception.PermissionDeniedException
import com.dobby.gateway.experiment.ExperimentPostGateway
import com.dobby.gateway.member.MemberGateway
import com.dobby.model.experiment.ApplyMethod
import com.dobby.model.experiment.ExperimentImage
import com.dobby.model.experiment.ExperimentPost
import com.dobby.model.experiment.TargetGroup
import com.dobby.model.member.Member
import com.dobby.usecase.UseCase
import com.dobby.util.IdGenerator
import java.time.LocalDate

class CreateExperimentPostUseCase(
    private val experimentPostGateway: ExperimentPostGateway,
    private val memberGateway: MemberGateway,
    private val idGenerator: IdGenerator
) : UseCase<CreateExperimentPostUseCase.Input, CreateExperimentPostUseCase.Output> {

    data class Input(
        val memberId: String,
        val targetGroupInfo: TargetGroupInfo,
        val applyMethodInfo: ApplyMethodInfo,
        val imageListInfo: ImageListInfo,

        val startDate: LocalDate?,
        val endDate: LocalDate?,
        val matchType: MatchType,
        val count: Int, // N 회 참여
        val timeRequired: TimeSlot?,

        val leadResearcher: String,
        val isOnCampus: Boolean,
        val place: String?,
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
        val postId: String,
        val title: String,
        val views: Int,
        val timeRequired: TimeSlot?,
        val count: Int?,
        val isOnCampus: Boolean,
        val place: String?,
        val reward: String?,
        val durationInfo: DurationInfo?
    )

    data class DurationInfo(
        val startDate: LocalDate?,
        val endDate: LocalDate?
    )

    override fun execute(input: Input): Output {
        val member = memberGateway.getById(input.memberId)
        validateMemberRole(member)

        val targetGroup = TargetGroup.newTargetGroup(
            id = idGenerator.generateId(),
            startAge = input.targetGroupInfo.startAge,
            endAge = input.targetGroupInfo.endAge,
            genderType = input.targetGroupInfo.genderType,
            otherCondition = input.targetGroupInfo.otherCondition
        )

        val applyMethod = ApplyMethod.newApplyMethod(
            id = idGenerator.generateId(),
            phoneNum = input.applyMethodInfo.phoneNum,
            formUrl = input.applyMethodInfo.formUrl,
            content = input.applyMethodInfo.content
        )

        val experimentPost = ExperimentPost.newExperimentPost(
            id = idGenerator.generateId(),
            member = member,
            targetGroup = targetGroup,
            applyMethod = applyMethod,
            leadResearcher = input.leadResearcher,
            title = input.title,
            content = input.content,
            reward = input.reward,
            startDate = input.startDate,
            endDate = input.endDate,
            timeRequired = input.timeRequired,
            count = input.count,
            matchType = input.matchType,
            isOnCampus = input.isOnCampus,
            place = input.place,
            region = input.region,
            area = input.area,
            detailedAddress = input.detailedAddress,
            alarmAgree = input.alarmAgree,
            recruitStatus = true
        )

        val experimentImages = input.imageListInfo.images.map { imageUrl ->
            ExperimentImage.newExperimentImage(
                id = idGenerator.generateId(),
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
                timeRequired = savedExperimentPost.timeRequired,
                count = savedExperimentPost.count,
                isOnCampus = savedExperimentPost.isOnCampus,
                place = savedExperimentPost.place,
                durationInfo = DurationInfo(
                    startDate = savedExperimentPost.startDate,
                    endDate = savedExperimentPost.endDate
                ),
                reward = savedExperimentPost.reward
            )
        )
    }
    private fun validateMemberRole(member: Member) {
        if (member.role != RoleType.RESEARCHER) {
            throw PermissionDeniedException
        }
    }
}
