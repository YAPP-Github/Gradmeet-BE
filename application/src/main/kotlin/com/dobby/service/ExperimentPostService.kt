package com.dobby.service

import com.dobby.usecase.experiment.*
import com.dobby.exception.ExperimentAreaInCorrectException
import com.dobby.exception.ExperimentAreaOverflowException
import com.dobby.exception.InvalidRequestValueException
import com.dobby.gateway.CacheGateway
import com.dobby.enums.areaInfo.Area
import com.dobby.enums.experiment.RecruitStatus
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ExperimentPostService(
    private val createExperimentPostUseCase: CreateExperimentPostUseCase,
    private val updateExperimentPostUseCase: UpdateExperimentPostUseCase,
    private val getExperimentPostsUseCase: GetExperimentPostsUseCase,
    private val getExperimentPostDetailUseCase: GetExperimentPostDetailUseCase,
    private val getExperimentPostCountsByRegionUseCase: GetExperimentPostCountsByRegionUseCase,
    private val getExperimentPostCountsByAreaUseCase: GetExperimentPostCountsByAreaUseCase,
    private val getExperimentPostApplyMethodUseCase: GetExperimentPostApplyMethodUseCase,
    private val generateExperimentPostPreSignedUrlUseCase: GenerateExperimentPostPreSignedUrlUseCase,
    private val updateExpiredExperimentPostUseCase: UpdateExpiredExperimentPostUseCase,
    private val getExperimentPostForUpdateUseCase: GetExperimentPostDetailForUpdateUseCase,
    private val deleteExperimentPostUseCase: DeleteExperimentPostUseCase,
    private val updateExperimentPostRecruitStatusUseCase: UpdateExperimentPostRecruitStatusUseCase,
    private val getExperimentPostTotalCountByCustomFilterUseCase: GetExperimentPostTotalCountByCustomFilterUseCase,
    private val getMyExperimentPostsUseCase: GetMyExperimentPostsUseCase,
    private val getMyExperimentPostTotalCountUseCase: GetMyExperimentPostTotalCountUseCase,
    private val cacheGateway: CacheGateway,
) {
    @Transactional
    fun createNewExperimentPost(input: CreateExperimentPostUseCase.Input): CreateExperimentPostUseCase.Output {
        evictExperimentPostCountsCaches()
        return createExperimentPostUseCase.execute(input)
    }

    @Transactional
    fun updateExperimentPost(input: UpdateExperimentPostUseCase.Input): UpdateExperimentPostUseCase.Output{
        return updateExperimentPostUseCase.execute(input)
    }

    @Transactional
    fun getExperimentPostDetailForUpdate(input: GetExperimentPostDetailForUpdateUseCase.Input): GetExperimentPostDetailForUpdateUseCase.Output {
        return getExperimentPostForUpdateUseCase.execute(input)
    }

    @Transactional
    fun getExperimentPosts(input: GetExperimentPostsUseCase.Input): List<GetExperimentPostsUseCase.Output> {
        validateFilter(input)
        validateSortOrder(input.pagination.order)
        return getExperimentPostsUseCase.execute(input)
    }

    @Transactional
    fun getExperimentPostDetail(input: GetExperimentPostDetailUseCase.Input): GetExperimentPostDetailUseCase.Output {
        return getExperimentPostDetailUseCase.execute(input)
    }

    @Transactional
    fun getExperimentPostApplyMethod(input: GetExperimentPostApplyMethodUseCase.Input): GetExperimentPostApplyMethodUseCase.Output {
        return getExperimentPostApplyMethodUseCase.execute(input)
    }

    @Transactional
    fun updateExpiredExperimentPosts(input: UpdateExpiredExperimentPostUseCase.Input): UpdateExpiredExperimentPostUseCase.Output  {
        return updateExpiredExperimentPostUseCase.execute(input)
    }

    @Transactional
    fun updateExperimentPostRecruitStatus(input: UpdateExperimentPostRecruitStatusUseCase.Input): UpdateExperimentPostRecruitStatusUseCase.Output {
        return updateExperimentPostRecruitStatusUseCase.execute(input)
    }

    fun getExperimentPostCounts(input: Any): Any {
        if (input is GetExperimentPostCountsByRegionUseCase.Input && input.region == null) {
            getCachedExperimentPostCounts(input.recruitStatus)?.let { return it }
        }

        val output = when (input) {
            is GetExperimentPostCountsByRegionUseCase.Input -> getExperimentPostCountsByRegionUseCase.execute(input)
            is GetExperimentPostCountsByAreaUseCase.Input -> getExperimentPostCountsByAreaUseCase.execute(input)
            else -> throw IllegalArgumentException("Invalid input type: ${input::class.java.simpleName}")
        }

        if (input is GetExperimentPostCountsByRegionUseCase.Input && input.region == null) {
            cacheExperimentPostCounts(input.recruitStatus, output)
        }

        return output
    }

    private fun getCachedExperimentPostCounts(recruitStatus: RecruitStatus): GetExperimentPostCountsByRegionUseCase.Output? {
        val cacheKey = "experimentPostCounts:$recruitStatus"
        return cacheGateway.getObject(cacheKey, GetExperimentPostCountsByRegionUseCase.Output::class.java)
    }

    private fun cacheExperimentPostCounts(recruitStatus: RecruitStatus, output: Any) {
        val cacheKey = "experimentPostCounts:$recruitStatus"
        cacheGateway.setObject(cacheKey, output)
    }

    private fun validateFilter(input: GetExperimentPostsUseCase.Input) {
        val locationInfo = input.customFilter.locationTarget ?: return

        locationInfo.areas?.let { validateLocationAreaCount(it) }
        validateRegion(locationInfo)
    }

    private fun validateLocationAreaCount(areas: List<Area>) {
        if (areas.size > 5) throw ExperimentAreaOverflowException
    }

    private fun validateRegion(locationInfo: GetExperimentPostsUseCase.LocationTargetInput){
        val region = locationInfo.region?: return
        val validAreas = Area.findByRegion(region).map { it.name }

        if(locationInfo.areas?.map {it.name }?.any {it !in validAreas } == true) {
            throw ExperimentAreaInCorrectException
        }
    }

    fun generatePreSignedUrl(input: GenerateExperimentPostPreSignedUrlUseCase.Input): GenerateExperimentPostPreSignedUrlUseCase.Output {
        return generateExperimentPostPreSignedUrlUseCase.execute(input)
    }

    fun getExperimentPostTotalCount(input: GetExperimentPostTotalCountByCustomFilterUseCase.Input): Int {
        return getExperimentPostTotalCountByCustomFilterUseCase.execute(input)
    }

    @Transactional
    fun getMyExperimentPosts(input: GetMyExperimentPostsUseCase.Input): List<GetMyExperimentPostsUseCase.Output> {
        validateSortOrder(input.pagination.order)
        return getMyExperimentPostsUseCase.execute(input)
    }

    fun getMyExperimentPostsCount(input: GetMyExperimentPostTotalCountUseCase.Input): GetMyExperimentPostTotalCountUseCase.Output {
        return getMyExperimentPostTotalCountUseCase.execute(GetMyExperimentPostTotalCountUseCase.Input(input.memberId))
    }

    private fun validateSortOrder(sortOrder: String): String {
        return when (sortOrder) {
            "ASC", "DESC" -> sortOrder
            else -> throw InvalidRequestValueException
        }
    }

    @Transactional
    fun deleteExperimentPost(input: DeleteExperimentPostUseCase.Input) {
        deleteExperimentPostUseCase.execute(input)
        evictExperimentPostCountsCaches()
    }

    private fun evictExperimentPostCountsCaches() {
        listOf("ALL", "OPEN").forEach { cacheGateway.evict("experimentPostCounts:$it") }
    }
}
