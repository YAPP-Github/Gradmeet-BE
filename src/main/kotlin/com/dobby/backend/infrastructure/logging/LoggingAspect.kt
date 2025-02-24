package com.dobby.backend.infrastructure.logging

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.URLDecoder
import java.util.UUID

@Aspect
@Component
class LoggingAspect {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Suppress("EmptyFunctionBlock")
    @Pointcut("execution(* com.dobby.backend.presentation.api.controller..*Controller.*(..))")
    private fun allController() {}

    @Around("allController()")
    fun logRequest(joinPoint: ProceedingJoinPoint): Any? {
        val taskId = UUID.randomUUID().toString().substring(0, 8)
        setTaskId(taskId)

        val request = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request
        val method = request?.method ?: "UNKNOWN_METHOD"
        val requestURI = request?.requestURI ?: "UNKNOWN_URI"
        val decodedURI = URLDecoder.decode(requestURI, "UTF-8")

        val args = joinPoint.args.map { SensitiveDataMasker.mask(it) }

        log.info("[{}] {} -> {} args={}", taskId, method, decodedURI, args)

        return joinPoint.proceed()
    }

    @AfterReturning(
        value = "execution(* com.dobby.backend.presentation.api.controller..*Controller.*(..))",
        returning = "result"
    )
    fun logResponse(joinPoint: JoinPoint, result: Any?) {
        val taskId = getTaskId() ?: UUID.randomUUID().toString().substring(0, 8)
        val methodSignature = joinPoint.signature as MethodSignature
        val controllerMethodName = methodSignature.method.name

        log.info("[{}] <- method: {}, result: {}", taskId, controllerMethodName, SensitiveDataMasker.mask(result))
    }

    /**
     * 요청 컨텍스트에 taskId 저장
     */
    private fun setTaskId(taskId: String) {
        RequestContextHolder.getRequestAttributes()?.setAttribute("taskId", taskId, 0)
    }

    /**
     * 요청 컨텍스트에서 taskId 가져오기
     */
    private fun getTaskId(): String? {
        return RequestContextHolder.getRequestAttributes()?.getAttribute("taskId", 0) as? String
    }
}
