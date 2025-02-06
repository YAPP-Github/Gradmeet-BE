package com.dobby.backend.infrastructure.database.repository

import com.dobby.backend.application.model.Pagination
import com.dobby.backend.domain.model.experiment.*
import com.dobby.backend.domain.model.member.Participant
import com.dobby.backend.infrastructure.database.entity.enums.GenderType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType
import com.dobby.backend.infrastructure.database.entity.enums.MatchType.*
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Area.Companion.isAll
import com.dobby.backend.infrastructure.database.entity.enums.areaInfo.Region
import com.dobby.backend.infrastructure.database.entity.enums.experiment.RecruitStatus
import com.dobby.backend.infrastructure.database.entity.experiment.*
import com.dobby.backend.infrastructure.database.entity.member.ParticipantEntity
import com.dobby.backend.infrastructure.database.entity.member.QMemberEntity
import com.dobby.backend.infrastructure.database.entity.member.QParticipantEntity
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.EnumPath
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.NumberPath
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class ExperimentPostCustomRepositoryImpl (
    private val jpaQueryFactory: JPAQueryFactory
) : ExperimentPostCustomRepository {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun findExperimentPostsByCustomFilter(
        customFilter: CustomFilter,
        pagination: Pagination
    ): List<ExperimentPostEntity>? {
        val post = QExperimentPostEntity.experimentPostEntity
        val recruitStatusCondition = when (customFilter.recruitStatus) {
            RecruitStatus.ALL -> null
            RecruitStatus.OPEN -> post.recruitStatus.eq(true)
        }

        return jpaQueryFactory.selectFrom(post)
            .join(post.targetGroup).fetchJoin()
            .join(post.applyMethod).fetchJoin()
            .where(
                matchTypeEq(post, customFilter.matchType),
                genderEq(post, customFilter.studyTarget?.gender),
                ageBetween(post, customFilter.studyTarget?.age),
                regionEq(post, customFilter.locationTarget?.region),
                areasIn(post, customFilter.locationTarget?.areas),
                recruitStatusCondition
            )
            .offset((pagination.page - 1L) * pagination.count)
            .limit(pagination.count.toLong())
            .orderBy(post.createdAt.desc())
            .fetch()
    }

    override fun findExperimentPostsByMemberIdWithPagination(
        memberId: String,
        pagination: Pagination,
        order: String
    ): List<ExperimentPostEntity>? {
        val post = QExperimentPostEntity.experimentPostEntity

        return jpaQueryFactory.selectFrom(post)
            .join(post.member).fetchJoin()
            .where(post.member.id.eq(memberId))
            .offset((pagination.page - 1L) * pagination.count)
            .limit(pagination.count.toLong())
            .orderBy(getOrderClause(order))
            .fetch()
    }

    override fun countExperimentPostsByCustomFilter(customFilter: CustomFilter): Int {
        val post = QExperimentPostEntity.experimentPostEntity
        val recruitStatusCondition = when (customFilter.recruitStatus) {
            RecruitStatus.ALL -> null
            RecruitStatus.OPEN -> post.recruitStatus.eq(true)
        }

        return jpaQueryFactory.select(post.count())
            .from(post)
            .where(
                matchTypeEq(post, customFilter.matchType),
                genderEq(post, customFilter.studyTarget?.gender),
                ageBetween(post, customFilter.studyTarget?.age),
                regionEq(post, customFilter.locationTarget?.region),
                areasIn(post, customFilter.locationTarget?.areas),
                recruitStatusCondition
            )
            .fetchOne()?.toInt() ?: 0
    }

    private fun matchTypeEq(post: QExperimentPostEntity, matchType: MatchType?): BooleanExpression? {
        return matchType?.let {
            if(it == ALL) null else post.matchType.eq(it)
        }
    }

    private fun genderEq(post: QExperimentPostEntity, gender: GenderType?): BooleanExpression? {
        return gender?.let { post.targetGroup.genderType.eq(it) }
    }

    private fun ageBetween(post: QExperimentPostEntity, age: Int?): BooleanExpression? {
        return age?.let {
            post.targetGroup.startAge.loe(it).and(post.targetGroup.endAge.goe(it))
        }
    }

    private fun regionEq(post: QExperimentPostEntity, region: Region?): BooleanExpression? {
        return region?.let { post.region.eq(it) }
    }

    private fun areasIn(post: QExperimentPostEntity, areas: List<Area>?): BooleanExpression? {
        return areas?.takeIf { it.isNotEmpty() && !it.any { area -> area.isAll() } }?.let {
            post.area.`in`(it)
        }
    }

    @Override
    override fun updateExperimentPostStatus(currentDate: LocalDate): Long {
        val experimentPost = QExperimentPostEntity.experimentPostEntity

        return jpaQueryFactory.update(experimentPost)
            .set(experimentPost.recruitStatus, false)
            .where(
                experimentPost.endDate.lt(currentDate)
                    .and(experimentPost.recruitStatus.eq(true))
            )
            .execute()
    }

    private fun getOrderClause(order: String): OrderSpecifier<*> {
        val post = QExperimentPostEntity.experimentPostEntity
        return if (order == "ASC") {
            post.createdAt.asc()
        } else {
            post.createdAt.desc()
        }
    }

    override fun updateExperimentPost(experimentPost: ExperimentPost): ExperimentPost {
        val qExperimentPost = QExperimentPostEntity.experimentPostEntity
        val qApplyMethod = QApplyMethodEntity.applyMethodEntity
        val qTargetGroup = QTargetGroupEntity.targetGroupEntity

       jpaQueryFactory.update(qExperimentPost)
            .set(qExperimentPost.title, experimentPost.title)
            .set(qExperimentPost.reward, experimentPost.reward)
            .set(qExperimentPost.startDate, experimentPost.startDate)
            .set(qExperimentPost.endDate, experimentPost.endDate)
            .set(qExperimentPost.content, experimentPost.content)
            .set(qExperimentPost.count, experimentPost.count)
            .set(qExperimentPost.leadResearcher, experimentPost.leadResearcher)
            .set(qExperimentPost.detailedAddress, experimentPost.detailedAddress)
            .set(qExperimentPost.matchType, experimentPost.matchType)
            .set(qExperimentPost.univName, experimentPost.univName)
            .set(qExperimentPost.region, experimentPost.region)
            .set(qExperimentPost.area, experimentPost.area)
            .set(qExperimentPost.updatedAt, LocalDateTime.now())
            .where(qExperimentPost.id.eq(experimentPost.id))
            .execute()

        jpaQueryFactory.update(qApplyMethod)
            .set(qApplyMethod.content, experimentPost.applyMethod.content)
            .set(qApplyMethod.formUrl, experimentPost.applyMethod.formUrl)
            .set(qApplyMethod.phoneNum, experimentPost.applyMethod.phoneNum)
            .where(qApplyMethod.id.eq(experimentPost.applyMethod.id))
            .execute()

        jpaQueryFactory.update(qTargetGroup)
            .set(qTargetGroup.startAge, experimentPost.targetGroup.startAge)
            .set(qTargetGroup.endAge, experimentPost.targetGroup.endAge)
            .set(qTargetGroup.genderType, experimentPost.targetGroup.genderType)
            .set(qTargetGroup.otherCondition, experimentPost.targetGroup.otherCondition)
            .where(qTargetGroup.id.eq(experimentPost.targetGroup.id))
            .execute()

        updateImages(experimentPostId = experimentPost.id, experimentPost.images)

        val updatedEntity = jpaQueryFactory.selectFrom(qExperimentPost)
            .where(qExperimentPost.id.eq(experimentPost.id))
            .fetchOne() ?: throw IllegalStateException("ExperimentPost not found after update")

        return updatedEntity.toDomain()
    }

    private fun updateImages(experimentPostId: String, images: List<ExperimentImage>) {
        val experimentPost = entityManager.find(ExperimentPostEntity::class.java, experimentPostId)
            ?: throw IllegalStateException("ExperimentPost not found")

        jpaQueryFactory.delete(QExperimentImageEntity.experimentImageEntity)
            .where(QExperimentImageEntity.experimentImageEntity.experimentPost.id.eq(experimentPostId))
            .execute()

        images.forEach { image ->
            val imageEntity = ExperimentImageEntity(
                id = "0",
                imageUrl = image.imageUrl,
                experimentPost = experimentPost
            )
            experimentPost.addImage(imageEntity)
            entityManager.persist(imageEntity)
        }
        entityManager.merge(experimentPost)
    }

    private var lastProcessedTime: LocalDateTime = LocalDate.now().minusDays(1).atTime(8, 1)

    @Override
    override fun findMatchingExperimentPostsForAllParticipants(): Map<String, List<ExperimentPostEntity>> {
        val experimentPost = QExperimentPostEntity.experimentPostEntity
        val targetGroup = QTargetGroupEntity.targetGroupEntity
        val participant = QParticipantEntity.participantEntity
        val member = QMemberEntity.memberEntity

        val currentTime = LocalDateTime.now()

        val todayPosts = jpaQueryFactory.selectFrom(experimentPost)
            .join(experimentPost.targetGroup, targetGroup).fetchJoin()
            .where(
                experimentPost.createdAt.between(lastProcessedTime, currentTime),
                experimentPost.alarmAgree.isTrue
            )
            .fetch()

        return jpaQueryFactory
            .select(participant, member.contactEmail)
            .from(participant)
            .join(participant.member, member)
            .fetch()
            .mapNotNull { tuple ->
                val participantEntity: ParticipantEntity = tuple.get(participant)!!
                val contactEmail: String? = tuple.get(member.contactEmail)
                val birthDate = participantEntity.birthDate

                contactEmail?.let {
                    val currentYear = LocalDate.now().year
                    val participantAge = currentYear - birthDate.year + 1

                    Pair(it, todayPosts.filter { post ->
                        listOf(
                            customGenderEq(post.targetGroup.genderType, participantEntity.gender),
                            customAgeBetween(post.targetGroup.startAge, post.targetGroup.endAge, participantAge),
                            customAddressInfoEq(
                                post.region, post.area,
                                participantEntity.basicAddressInfo.region, participantEntity.basicAddressInfo.area,
                                participantEntity.additionalAddressInfo.region, participantEntity.additionalAddressInfo.area
                            ),
                            customMatchTypeEq(post.matchType, participantEntity.matchType)
                        ).all { it }
                    }.take(10))
                }
            }.toMap()
    }

    private fun customGenderEq(
        postGender: GenderType,
        participantGender: GenderType
    ): Boolean {
        return participantGender == GenderType.ALL || postGender == participantGender || postGender == GenderType.ALL
    }

    private fun customAgeBetween(
        startAge: Int?,
        endAge: Int?,
        participantAge: Int
    ): Boolean {
        if(startAge == null || endAge == null) return true
        return (startAge <= participantAge) && (participantAge <= endAge)
    }

    private fun customAddressInfoEq(
        region: Region?,
        area: Area?,
        basicRegion: Region, basicArea: Area,
        additionalRegion: Region?, additionalArea: Area?
    ): Boolean {
        if(region == null && area == null) return true
        val isBasicMatch = (region == basicRegion) && (area == basicArea)
        val isAdditionalMatch = (additionalRegion != null) && (additionalArea != null)
                && (region == additionalRegion && area == additionalArea)
        return isBasicMatch || isAdditionalMatch
    }

    private fun customMatchTypeEq(
        postMatchType: MatchType?,
        participantMatchType: MatchType?
    ): Boolean {
        if (participantMatchType == ALL || participantMatchType == null)
            return true
        if (postMatchType == null)
            return true
        return postMatchType == participantMatchType
    }

}
