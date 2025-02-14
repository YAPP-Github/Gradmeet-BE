package com.dobby.backend.application.usecase.experiment

import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.*
import com.dobby.backend.domain.IdGenerator
import com.dobby.backend.domain.gateway.experiment.ExperimentPostGateway
import com.dobby.backend.domain.gateway.member.MemberGateway
import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentImage
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enums.member.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.member.RoleType
import com.dobby.backend.infrastructure.database.entity.enums.experiment.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
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
        val place: String?,
        val reward: String?,
        val durationInfo: DurationInfo?,
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
                place = savedExperimentPost.place,
                durationInfo = DurationInfo(
                    startDate = savedExperimentPost.startDate,
                    endDate = savedExperimentPost.endDate
                ),
                reward = savedExperimentPost.reward
            )
        )
    }
    private fun validateMemberRole(member :Member){
        if(member.role != RoleType.RESEARCHER)
            throw PermissionDeniedException
    }
}
