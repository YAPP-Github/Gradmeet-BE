package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.experiment.CreatePostUseCase
import com.dobby.backend.application.usecase.experiment.GetResearcherInfoUseCase
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PostService(
    private val createPostUseCase: CreatePostUseCase,
    private val getResearcherInfoUseCase: GetResearcherInfoUseCase
) {
    @Transactional
    fun createNewExperimentPost(input: CreatePostUseCase.Input): CreatePostUseCase.Output {
        return createPostUseCase.execute(input)
    }

    fun getDefaultInfo(): GetResearcherInfoUseCase.Output {
        return getResearcherInfoUseCase.execute(Unit)
    }
}
