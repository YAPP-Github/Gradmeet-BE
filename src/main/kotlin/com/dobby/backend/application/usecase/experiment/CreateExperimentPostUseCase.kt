package com.dobby.backend.application.usecase.experiment
import com.dobby.backend.application.usecase.UseCase
import com.dobby.backend.domain.exception.PermissionDeniedException
import com.dobby.backend.domain.gateway.*
import com.dobby.backend.domain.model.experiment.ApplyMethod
import com.dobby.backend.domain.model.experiment.ExperimentPost
import com.dobby.backend.domain.model.experiment.TargetGroup
import com.dobby.backend.domain.model.member.Member
import com.dobby.backend.infrastructure.database.entity.enum.GenderType
import com.dobby.backend.infrastructure.database.entity.enum.MatchType
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.infrastructure.database.entity.enum.TimeSlot
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enum.areaInfo.Region
import java.time.LocalDate

class CreateExperimentPostUseCase(
    private val experimentPostGateway: ExperimentPostGateway,
    private val memberGateway: MemberGateway,
) : UseCase<CreateExperimentPostUseCase.Input, CreateExperimentPostUseCase.Output> {
    data class Input(
        val memberId: Long,
        val targetGroupInfo: TargetGroupInfo,
        val applyMethodInfo: ApplyMethodInfo,
        val imageListInfo: ImageListInfo,

        val startDate: LocalDate,
        val endDate: LocalDate,
        val matchType: MatchType,
        val count: Int, // N 회 참여
        val durationMinutes: TimeSlot,

        val leadResearcher: String,
        val univName: String,
        val region: Region,
        val area: Area,
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
        val otherCondition: String?,
    )

    data class ApplyMethodInfo(
        val content: String,
        val formUrl: String?,
        val phoneNum: String?,
    )

    data class ImageListInfo(
        val images: List<String> = emptyList()
    )

    data class Output(
        val postInfo: PostInfo
    )

    data class PostInfo(
        val postId: Long,
        val title: String,
        val views: Int,
        val school: String,
        val reward: String,
        val startDate: LocalDate,
        val endDate: LocalDate
    )

    override fun execute(input: Input): Output {
        val member = memberGateway.getById(input.memberId)

        if (member.role != RoleType.RESEARCHER) {
            throw PermissionDeniedException()
        }
        val targetGroup = createTargetGroup(input.targetGroupInfo)
        val applyMethod = createApplyMethod(input.applyMethodInfo)
        val experimentPost = createExperimentPost(member, input, targetGroup, applyMethod)
        val savedExperimentPost = experimentPostGateway.save(experimentPost)

        return Output(
            PostInfo(
                postId = savedExperimentPost.id,
                title = savedExperimentPost.title,
                views = savedExperimentPost.views,
                school = savedExperimentPost.univName,
                startDate = savedExperimentPost.startDate,
                endDate = savedExperimentPost.endDate,
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
    }

    private fun createApplyMethod(applyMethodInfo: CreateExperimentPostUseCase.ApplyMethodInfo): ApplyMethod {
        return ApplyMethod(
                id = 0L,
                phoneNum = applyMethodInfo.phoneNum,
                formUrl = applyMethodInfo.formUrl,
                content = applyMethodInfo.content
            )
    }

    private fun createExperimentPost(
        member: Member,
        input: CreateExperimentPostUseCase.Input,
        targetGroup: TargetGroup,
        applyMethod: ApplyMethod
    ): ExperimentPost {
        return ExperimentPost(
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
            durationMinutes = input.durationMinutes,
            count = input.count,
            matchType = input.matchType,
            univName = input.univName,
            region = input.region,
            area = input.area,
            detailedAddress = input.detailedAddress,
            alarmAgree = input.alarmAgree,
            images = emptyList() // 이미지 업로드 보류
        )
    }

