package com.dobby.backend.application.service

import com.dobby.backend.application.usecase.expirementUseCase.CreatePostUseCase
import com.dobby.backend.domain.exception.PermissionDeniedException
import com.dobby.backend.domain.gateway.MemberGateway
import com.dobby.backend.infrastructure.database.entity.enum.RoleType
import com.dobby.backend.util.AuthenticationUtils
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PostService(
    private val createPostUseCase: CreatePostUseCase,
) {
    @Transactional
    fun createNewExperimentPost(input: CreatePostUseCase.Input): CreatePostUseCase.Output {
        return createPostUseCase.execute(input)
    }
}
